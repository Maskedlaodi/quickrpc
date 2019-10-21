package com.github.common.impl;


import com.github.common.Framework;

import java.util.Map;

/**
/**
 * @Desc:
 * @Author: xiaobobo
 * @Date: Created by 2019/10/21 9:57 AM
 */
public class FrameworkAop implements Framework {

    private FrameworkImpl                   framework = new FrameworkImpl();

    @Override
    public void export(Map<String, Object> serviceMap, int port) {

    }

    @Override
    public <T> T refer(Class<T> interfaceClass, String host, int port) {
        final long begin = System.currentTimeMillis();
        try {
            return framework.refer(interfaceClass, host, port);
        } finally {
            final long end = System.currentTimeMillis();
            System.out.println(">>>>>>>>>>> " + interfaceClass.toString() + " spend time: " + (end - begin) + " <<<<<<<<<<<<<");
        }
    }
}
