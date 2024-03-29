FROM --platform=$BUILDPLATFORM maven:3-eclipse-temurin-8 as builder


WORKDIR /sources
COPY ./ /sources

# store maven dependencies so next build doesn't have to download them again
RUN --mount=type=cache,target=/root/.m2/repository \
	mvn -B dependency:go-offline 



RUN --mount=type=cache,target=/root/.m2/repository \
	mvn -B package -DskipTests
RUN mkdir /application && \
	cp target/csw-harvester*.jar /application/csw-harvester.jar

WORKDIR /application

# Extract spring boot JAR layers
RUN --mount=type=cache,target=/root/.m2/repository \
	java -Djarmode=layertools -jar csw-harvester.jar extract


FROM eclipse-temurin:8-jre as finalImage

LABEL vendor="GeoCat B.V."
LABEL org.opencontainers.image.source https://github.com/GeoCat/csw-harvester



# Check the file application.properties for a description of the environment variables that can be customized.
# The property names can be translated to environment varibles passing them to upper case and replacing the dots
# with underscores. For example harvester.jdbc.url -> HARVESTER_JDBC_URL
COPY extra/certs/ /certs
COPY extra/bin /usr/local/bin

RUN chmod -R +x /usr/local/bin && \
	import_certs.sh && \
	mkdir -p /opt/csw-harvester
WORKDIR /opt/csw-harvester
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application/ ./

EXPOSE 9999
CMD [ "java", "org.springframework.boot.loader.JarLauncher" ]
#ENTRYPOINT exec java $JAVA_OPTS -jar ingester.jar
