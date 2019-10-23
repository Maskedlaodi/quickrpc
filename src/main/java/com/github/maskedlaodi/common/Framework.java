package com.github.maskedlaodi.common;

import java.util.Map;

public interface Framework {

    void export(final Map<String, Object> serviceMap, int port);

    <T> T refer(final Class<T> interfaceClass, final String host, final int port);

}
