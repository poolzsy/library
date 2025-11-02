package com.lilac.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lilac.constant.ExceptionConstant;
import com.lilac.constant.SystemConstant;
import com.lilac.domain.dto.AddUserDTO;
import com.lilac.domain.dto.UserDTO;
import com.lilac.domain.dto.UserLoginDTO;
import com.lilac.domain.entity.LoginUser;
import com.lilac.domain.entity.Role;
import com.lilac.domain.entity.User;
import com.lilac.domain.entity.UserRole;
import com.lilac.domain.vo.PageVO;
import com.lilac.domain.vo.UserVO;
import com.lilac.enums.HttpsCodeEnum;
import com.lilac.exception.SystemException;
import com.lilac.mapper.UserMapper;
import com.lilac.service.RoleService;
import com.lilac.service.UserRoleService;
import com.lilac.service.UserService;
import com.lilac.utils.BeanCopyUtils;
import com.lilac.utils.JwtUtils;
import com.lilac.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author lilac
 */
@SuppressWarnings({"all"})
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisCache redisCache;

    /**
     * 登录
     *
     * @param loginDTO 用户信息
     * @return 登录结果
     */
    @Override
    public String login(UserLoginDTO loginDTO) {
        // 参数校验
        if (Objects.isNull(loginDTO) || !StringUtils.hasText(loginDTO.getUserName()) || !StringUtils.hasText(loginDTO.getPassword())) {
            throw new SystemException(HttpsCodeEnum.USER_OR_PASSWORD_ERROR);
        }
        // 解密密码
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword());
        // 调用认证管理器进行认证
        Authentication authenticate;
        try {
            // 调用认证管理器进行认证
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new SystemException(HttpsCodeEnum.USER_OR_PASSWORD_ERROR);
        }
        // 获取userId，生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtils.createJWT(userId);

        // 存入redis
        redisCache.setCacheObject(SystemConstant.USER_LOGIN_KEY + userId, loginUser);
        return jwt;
    }

    /**
     * 注册
     *
     * @param loginDTO 包含加密密码的DTO
     */
    @Override
    public void register(UserLoginDTO loginDTO) {
        // 使用BCrypt对明文密码进行哈希处理
        String hashedPassword = passwordEncoder.encode(loginDTO.getPassword());
        // 创建用户实体并存入数据库
        User newUser = new User();
        newUser.setUserName(loginDTO.getUserName());
        newUser.setPassword(hashedPassword);
        newUser.setType(SystemConstant.DEFAULT_USER_TYPE);
        newUser.setStatus(SystemConstant.DEFAULT_STATUS);
        try {
            userMapper.save(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new SystemException(HttpsCodeEnum.USER_EXIST);
        }
    }

    /**
     * 根据id查询角色
     *
     * @param id 用户id
     */
    @Override
    public Map<String, Object> selectById(Integer id) {
        // 查询用户
        User user = userMapper.selectById(id);
        if (user == null){
            throw new SystemException(HttpsCodeEnum.RESOURCE_NOT_FOUND);
        }
        UserVO userVO = BeanCopyUtils.copyBean(user, UserVO.class);
        // 获取用户角色列表
        List<Integer> roleIds = userRoleService.listUserRole(user.getId()).stream().map(UserRole::getRoleId).collect(Collectors.toList());
        // 获取角色列表
        List<Role> roles = roleService.listAll();
        Map<String, Object> data = new HashMap<>();
        data.put("user", userVO);
        data.put("roles", roles);
        data.put("roleIds", roleIds);
        return data;
    }

    /**
     * 新增用户信息
     *
     * @param userSaveDTO
     */
    @Override
    @Transactional
    public void save(AddUserDTO addUserDTO) {
        if (userMapper.findByUserName(addUserDTO.getUserName()) != null) {
            throw new SystemException(HttpsCodeEnum.USER_EXIST);
        } else {
            User newUser = BeanCopyUtils.copyBean(addUserDTO, User.class);
            setPassword(newUser);
            newUser.setType(SystemConstant.DEFAULT_USER_TYPE);
            newUser.setStatus(SystemConstant.DEFAULT_STATUS);
            userMapper.save(newUser);

            // 关联角色
            List<Integer> roleIds = newUser.getRoleIds();
            associateUserRoles(newUser.getId(), roleIds);
        }
    }

    /**
     * 删除用户
     *
     * @param id
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        // 判断id是否存在
        if (Objects.isNull(id) || id <= 0) {
            throw new SystemException(HttpsCodeEnum.DATA_NOT_EMPTY, ExceptionConstant.ID_ERROR);
        }
        // 删除用户角色关联
        userRoleService.removeByUserId(id);
        userMapper.deleteById(id);
    }

    /**
     * 修改用户信息
     *
     * @param userDTO
     */
    @Override
    @Transactional
    public void update(User user) {
        User existingUser = userMapper.findByUserName(user.getUserName());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new SystemException(HttpsCodeEnum.USER_EXIST);
        }
        User newUser = BeanCopyUtils.copyBean(user, User.class);
        // 只有当传入的密码不为空时才更新密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userMapper.update(newUser);

        // 修改用户角色关联，先删在存
        userRoleService.removeByUserId(user.getId());
        List<Integer> roleIds = newUser.getRoleIds();
        associateUserRoles(user.getId(), roleIds);
    }

    /**
     * 分页查询用户
     *
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageVO page(UserDTO userDTO) {
        PageHelper.startPage(userDTO.getPageNum(), userDTO.getPageSize());
        List<User> userList = userMapper.pageList(userDTO);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        List<UserVO> userVOList = BeanCopyUtils.copyBeanList(pageInfo.getList(), UserVO.class);
        return new PageVO(pageInfo.getTotal(), userVOList);
    }

    /**
     * 设置默认密码
     */
    private void setPassword(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(SystemConstant.DEFAULT_PASSWORD));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
    }

    /**
     * 批量关联用户角色
     */
    private void associateUserRoles(Integer userId, List<Integer> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            List<UserRole> userRoleList = roleIds.stream()
                    .map(roleId -> new UserRole(userId, roleId))
                    .collect(Collectors.toList());
            userRoleService.saveUserRole(userRoleList);
        }
    }
}