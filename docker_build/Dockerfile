FROM maven:3-jdk-8
RUN apt-get update; apt-get install -y netcdf-bin
VOLUME /code
WORKDIR /code
CMD mvn test