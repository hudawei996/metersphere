FROM metersphere/fabric8-java-alpine-openjdk8-jre

LABEL maintainer="FIT2CLOUD <support@fit2cloud.com>"

ARG MS_VERSION=v1.18
ARG DEPENDENCY=backend/target/dependency

COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

RUN mv /app/lib/ms-jmeter-core-*.jar /app/lib/ms-jmeter-core.jar
RUN mv /app/jmeter /opt/
RUN mkdir -p /opt/jmeter/lib/junit

ENV JAVA_CLASSPATH=/app:/app/lib/ms-jmeter-core-*.jar:/app/lib/*
ENV JAVA_MAIN_CLASS=io.metersphere.Application
ENV AB_OFF=true
ENV MS_VERSION=${MS_VERSION}
ENV JAVA_OPTIONS="-Dfile.encoding=utf-8 -Djava.awt.headless=true"

CMD ["/deployments/run-java.sh"]
