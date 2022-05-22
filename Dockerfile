# Docker image for springboot file run
# VERSION 0.0.1
# 基础镜像使用java
FROM java:8
# 作者
LABEL maintainer="coldairance" \
      email="2645868345@qq.com"
# 将jar包添加到容器中并更名为app.jar
ADD blog-controller-0.0.1-SNAPSHOT.jar app.jar
# 运行jar包
RUN bash -c 'touch /app.jar'
# 路径
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]