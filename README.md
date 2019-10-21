### qicuk-rpc
> 致力于快速搭建 `rpc` 框架

### 启动rpc
#### rpc-服务端
* 1.项目中引入mvn架包
* 2.在需要发布服务的业务service类上添加`@Service`注解
* 3.在项目启动方法中加入一下代码
```
int port = 8181;
Map map = SysUtil.getService(String ... strs);
Framework rpc = new FrameworkAop();
rpc.export(map, port);
```
> - port：服务发布端口号；
> - strs：需要扫描的包地址；

#### rpc-消费端
* 1.项目中引入mvn架包
* 2.在调用服务
```
Framework rpc = new FrameworkAop();
Class<T> interfaceClass = rpc.refer(Class<T> interfaceClass, String host, int port);
.
.
.
```
> - interfaceClass：服务接口；
> - host：服务地址；
> - port：端口；