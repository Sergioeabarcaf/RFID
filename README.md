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

# Ejecucion del proyecto

## Levantar db mysql con docker

La db se levanta en el puerto 3306 de la maquina
~~~
docker-compose up -d
~~~

## Empaquetar jar

Se puede empaquetar el jar utilizando

~~~
mvn clean package
~~~

Esto empaquetara el jar en el directorio ```target```

## Iniciar la aplicacion

Podemos utilizar el jar generado en el paso anterior:

~~~
IMPINJ_READER_HOSTNAME=192.168.10.138 java -jar rfid-etl-mysql-0.0.1-SNAPSHOT.jar
~~~

Se pueden utilizar todas las variables de entorno mencionadas en el documento, para fijar ubicacion remota de la base de datos.

## Iniciar la aplicacion en modo desarrollador (opcional)

Alternativamente podemos iniciar la aplicacion directamente en el ambiente de desarrollo, utilizando el plugin de Spring Boot para maven:

~~~
mvn clean spring-boot:run
~~~

Esto tambien admite el uso de todas las variables de entorno descritas en el documento (Basta agregarlas antes del comando).

# Endpoints para controlar el comportamiento del lector

Se definen los siguientes puntos de acceso en el servicio:

* ```GET http://localhost:8080/read/start```: Inicia el lector en modo lectura (Persistiendo los tags leidos en la db mysql)
* ```GET http://localhost:8080/read/stop```: Detiene el modo de lectura.
* ```GET http://localhost:8080/write/start```: Inicia el lector en modo escritura (Se fija un epc aleatorio en los tags leidos)
* ```GET http://localhost:8080/write/stop```: Detiene el modo de escritura.
