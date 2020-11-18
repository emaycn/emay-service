# web接口服务

## 1. 编译

采用spring profile + maven profile 来区分开发环境(dev)、测试环境(test)、生产环境(prod)。    
使用maven进行编译、打包、安装时，需要使用-P+环境名来确保打包正确的环境配置文件。    
例如：mvn clean package -Ptest

## 2. 部署

采用jib直接打包成docker image。    
命令如下(注意-P是 1.编译中提到的环境参数)：    
- 打包成镜像，并部署到本地镜像库： mvn compile jib:dockerBuild -Ptest
- 打包成镜像，并提交到远程镜像库： mvn compile jib:build -Ptest -DsendCredentialsOverHttp=true

## 3. docker启动注意

因为docker文件都在容器内，所以比如log文件、上传文件等都需要挂在到宿主机，或者干脆使用文件系统，具体请参考docker使用方式。

docker run --name emay-service -p 8081:8081 -v /nfsdata:/nfsdata -v /opt/logs/$APP:/logs  --log-opt max-size=5g --log-opt max-file=3 -d emay-service:1.2.5

## 4. 前端
前端配合emay-vue使用