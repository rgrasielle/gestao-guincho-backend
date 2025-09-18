# Usa uma imagem base oficial do Java 17. É uma base segura e otimizada.
FROM eclipse-temurin:17-jdk-jammy

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos do Maven Wrapper para dentro do container
# Isso otimiza o build, aproveitando o cache de dependências do Docker
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Dá permissão de execução para o script do Maven Wrapper
RUN chmod +x ./mvnw

# Baixa todas as dependências do projeto. Se o pom.xml não mudar, o Docker reutiliza essa camada.
RUN ./mvnw dependency:go-offline

# Copia o código-fonte da sua aplicação
COPY src ./src

# Constrói a aplicação, gerando o arquivo .jar
RUN ./mvnw clean package -DskipTests

# Expõe a porta que a sua aplicação Spring Boot usa
EXPOSE 8080

# Comando final para iniciar a sua aplicação quando o container ligar
# ATENÇÃO: Verifique se o nome do arquivo .jar está correto!
ENTRYPOINT ["java", "-jar", "target/gestao-guincho-0.0.1-SNAPSHOT.jar"]