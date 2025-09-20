package com.lilac.aspect;

import com.lilac.annotation.AutoFill;
import com.lilac.enums.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.lilac.constant.AutoFillConstant;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现自动填充功能
 *
 * @author lilac
 */
@Aspect
@Slf4j
@Component
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.lilac.mapper.*.*(..)) && @annotation(com.lilac.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");

        // 获取当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        // 获取当前被拦截的方法的参数——实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        // 约定实体对象是第一个参数
        Object entity = args[0];

        // 准备赋值的数据
        LocalDateTime now = LocalDateTime.now();

        // 根据不同的操作类型，为对应的属性通过反射来赋值
        if (operationType == OperationType.INSERT) {
            // 公共字段赋值
            try {
                Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setDelFlag = entity.getClass().getMethod(AutoFillConstant.SET_DEL_FLAG, String.class);

                // 通过反射为对象属性赋值
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setDelFlag.invoke(entity, "0");
            } catch (Exception e) {
                log.error("公共字段自动填充[INSERT]失败", e);
            }
        } else if (operationType == OperationType.UPDATE) {
            // 为公共字段赋值
            try {
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);

                setUpdateTime.invoke(entity, now);
            } catch (Exception e) {
                log.error("公共字段自动填充[UPDATE]失败", e);
            }
        }
    }
}
