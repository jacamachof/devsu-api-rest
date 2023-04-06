Buen día.

Para desarrollar el proyecto usé PostgreSQL v14, sin embargo, el código usa JPQL y JPA para consumir la base de datos por lo que puede correr en cualquier base de datos relacional soportada por Hibernate, solo se debe cambiar el JPA dialect en application-local.yml e importar el driver en el archivo pom.xml. Tanto el archivo BaseDatos.sql como el proyecto pueden correr en h2 (embedded database).

Con respecto al funcionamiento de la API. Implementé un comportamiento para el atributo "Estado". En este caso, si el Cliente a usar tiene el estado "Inactivo", el cliente no podrá realizar movimientos, crear o actualizar cuentas pero aún podrá consultar las cuentas, su información personal y movimientos. El estado se puede cambiar actualizando la información del cliente, lo mismo aplica para las cuentas.

El código está en inglés pero todos los mensjes de error y valores almacenados en la base de datos están en español redactadas de manera legible para que se entiendan los resultados de la API, pueden encontrar los textos en /resources/messages.properties

Por temas de tiempo, solo programe unas pocas pruebas unitarias y una prueba integral (en Java).

Para correr el proyecto, el perfíl de Spring Boot debe ser local. No se requiere configuración adicional para correr las pruebas unitarias.