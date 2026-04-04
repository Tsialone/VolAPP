FROM eclipse-temurin:21-jdk-alpine

# 1. Installer Maven en premier (change jamais = toujours en cache)
RUN apt-get update && apt-get install -y maven

WORKDIR /app

# 2. Copier SEULEMENT pom.xml d'abord
COPY pom.xml .

# 3. Télécharger les dépendances séparément (cachées si pom.xml ne change pas)
RUN mvn dependency:go-offline -B

# 4. Copier le code source APRÈS
COPY src ./src

# 5. Build (sans re-télécharger les deps)
RUN mvn clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/dev-0.0.1-SNAPSHOT.jar"]