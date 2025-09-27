package com.lilac.utils;

import java.util.List;

/**
 * BeanCopy工具类
 *
 * @author lilac
 */
public class BeanCopyUtils {
    private BeanCopyUtils() {
    }

    /**
     * 拷贝对象
     *
     */
    public static <T> T copyBean(Object source, Class<T> clazz) {
        T target = null;
        try {
            target = clazz.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * 拷贝列表
     *
     */
    public static <O,T> List<T> copyBeanList(List<O> list, Class<T> clazz) {
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .toList();
    }
}
