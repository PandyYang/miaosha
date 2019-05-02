在application.propertis中配置相关的属性  
```
server.port=8090
mybatis.mapper-locations=classpath:mapping/*.xml

spring.datasource.name=miaosha
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/miaosha
spring.datasource.username=root
spring.datasource.password=root

#使用druid数据源
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```  
打开sql.sql文件 导入相关的sql语句
启动  
App  

在浏览器中打开登录注册页面进行相关的操作  
