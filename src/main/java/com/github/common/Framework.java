package com.github.common;

import java.util.Map;

/**
 * @Desc:
 * @Author: xiaobobo
 * @Date: Created by 2019/10/21 9:06 AM
 */
public interface Framework {

    /**
     * 暴露服务
     *
     * @param port 服务端口
     * @throws Exception
     */
    void export(final Map<String, Object> serviceMap, int port);

    /**
     * 引用服务
     *
     * @param <T> 接口泛型
     * @param interfaceClass 接口类型
     * @param host 服务器主机名
     * @param port 服务器端口
     * @return 远程服务
     * @throws Exception
     */
    <T> T refer(final Class<T> interfaceClass, final String host, final int port);

}
