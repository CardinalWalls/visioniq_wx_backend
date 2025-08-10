FROM cqwk-docker.pkg.coding.net/devops/private/wk-vpac:builder as builder
ARG MODULE="vpac-main"
WORKDIR /data/app
COPY . /data/app/
RUN mvn clean package -P prod,to-docker -pl !vpac-codetool  \
    && cp ${MODULE}/target/${MODULE}.jar /data/${MODULE}.jar && cd /data \
    && java -Djarmode=tools -jar ${MODULE}.jar extract --layers --launcher --destination complete

FROM cqwk-docker.pkg.coding.net/devops/public/jdk:21
ENV MODULE="vpac-main"
WORKDIR /data/app
#第三方依赖
COPY --from=builder /data/complete/dependencies/ ./
#spring boot启动相关
COPY --from=builder /data/complete/spring-boot-loader/ ./
#snapshot 依赖包，一般为第二方依赖
COPY --from=builder /data/complete/snapshot-dependencies/ ./
#配置文件
COPY config ./config
#项目中的依赖模块
COPY --from=builder /data/complete/application/ ./

VOLUME /root/logs
#Java21 默认G1CG，可推荐使用ZGC，-Xmx1024m -XX:+UseZGC -XX:+ZGenerational
#ENV JAVA_OPTS="-Xms1024m -Xmx1024m -XX:MetaspaceSize=128m "
EXPOSE 60606
ENTRYPOINT java ${JAVA_OPTS} -XX:+UseContainerSupport -XX:MaxRAMPercentage=80.0 \
-XX:+UseStringDeduplication -D_=${MODULE} "-Dserver.port=60606" "org.springframework.boot.loader.launch.JarLauncher"



#docker build -t cqwk-docker.pkg.coding.net/devops/private/wk-vpac .
#docker push cqwk-docker.pkg.coding.net/devops/private/wk-vpac
#docker run --rm -d --network cqwk -p 60606:60606 -e JAVA_OPTS="-Xmx768m -XX:+UseZGC -XX:+ZGenerational" -v /root/logs:/root/logs --privileged cqwk-docker.pkg.coding.net/devops/private/wk-vpac
