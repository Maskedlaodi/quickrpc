package com.github.maskedlaodi.common.impl;


import com.github.maskedlaodi.common.Framework;

import java.util.Map;

public class FrameworkAop implements Framework {

    private FrameworkImpl                   framework = new FrameworkImpl();

    @Override
    public void export(Map<String, Object> serviceMap, int port) {
        framework.export(serviceMap, port);
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
