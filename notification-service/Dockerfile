FROM eclipse-temurin:17-jdk-jammy as deps
ARG DIRECTORY

WORKDIR /build

COPY pom.xml pom.xml
WORKDIR /build/$DIRECTORY
COPY --chmod=0755 $DIRECTORY/mvnw mvnw
COPY ./$DIRECTORY/.mvn/ .mvn/

WORKDIR /build/$DIRECTORY
RUN --mount=type=bind,source=$DIRECTORY/pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests

FROM deps as package

WORKDIR /build/$DIRECTORY

COPY ./$DIRECTORY/src ./src

RUN --mount=type=bind,source=$DIRECTORY/pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests &&  \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

RUN mkdir -p /apps
COPY ./$DIRECTORY/entrypoint.sh /apps/entrypoint.sh
RUN chmod +x /apps/entrypoint.sh

CMD ["/apps/entrypoint.sh"]