# RFID Java Connector

Este proyecto funciona como puente entre el lector RFID Speedway de Impinj, utiliza el SDK provisto por Impinj y Spring Boot.

## Instalar librerias del SDK

Para usar la libreria con maven se utiliza:

~~~
mvn install:install-file -Dfile=OctaneSDKJava-1.26.0.0-jar-with-dependencies.jar -Dpackaging=jar -DgroupId=com.impinj.octane -Dversion=1.26.0.0 -DartifactId=octane-sdk
~~~

## Configurar host de lector

El proyecto utiliza la variable de entorno ```IMPINJ_READER_HOSTNAME``` para configurar el hostname del lector, si no se fija esta variable se utiliza el default ```169.254.1.1```.

## Configurar credenciales y host mysql

El proyecto utiliza Mysql como almacenamiento de lecturas de los tags, para permitir conexiones a un servidor externo se utiliza la variable de entorno ```MYSQL_HOST```, cuyo default es ```localhost```.

Tambien es posible configurar el username con la variable ```MYSQL_USERNAME```y la password con ```MYSQL_PASSWORD```, ambas son ```rfid``` por defecto.

Finalmente, la base de datos utilizada es manejada por la variable ```MYSQL_DATABASE```, cuyo default es ```rfid```.
