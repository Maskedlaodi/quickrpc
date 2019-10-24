package com.github.maskedlaodi.common.impl;


import com.github.maskedlaodi.common.Framework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FrameworkAop implements Framework {

    private FrameworkImpl                   framework = new FrameworkImpl();

    private static Logger                   logger = LoggerFactory.getLogger(FrameworkAop.class);

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
            logger.info("\n>>>>>>>>>>> {} spend time: {} <<<<<<<<<<<<<", interfaceClass.toString(), (end - begin));
        }
    }
}
