# 工程说明
emay-service 是以spring-boot为基础搭建的基础服务框架；
emay-service 作为api项目独立部署，未集成web模块；
emay-service 可以与 [emay-web](http://172.16.11.138/component/future/emay-web) 配合搭建完整的前后端分离web体系；

# 启动说明
1. 在IDE里面能直接Run EmayApplication
2. 打为jar包后，使用 java -jar emay-service 启动
3. 打为docker image 后，使用docker 命令启动
4. 工程必须搭配redis、mysql使用

# 其他
1. 系统账号密码 admin/qwe123
2. TODO 自身优雅关机以及在docker中优雅关机