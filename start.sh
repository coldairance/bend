mvn clean
# 打包
mvn package -Dmaven.test.skip=true
# 删除旧镜像
docker rm -f blog-end
# 拷贝Dockerfile 到jar包处
/cp Dockerfile blog-controller/target/
# 进入
cd blog-controller/target
# 制作镜像
docker build -t blog-end
# 启动镜像
docker run -d -p 8081:8081 blog-end
