# Integrador Arqui

Ejercicio integrador de JDBC (esquema, carga de CSV y consultas). La consigna original está en
[`src/main/java/org/example/README.md`](src/main/java/org/example/README.md).

## Requisitos

- JDK 21+
- Maven
- Docker y Docker Compose (solo para la base de datos)


## Levantar la base de datos

La base de datos corre en un contenedor MySQL, la aplicación corre local (fuera de Docker).

```bash
docker compose up -d
```

`-d` Levanta los contenedores, pero los manda a correr en segundo plano, liberando la terminal inmediatamente

**Para que el comando funcione tiene que estar abierto Docker Deskopt**

Esto levanta MySQL 8.0 en `localhost:3306` con:

| Variable | Valor |
|---|---|
| Base de datos | `db_integrador_tp1` |
| Usuario | `app_user` |
| Password | `12345` |
| Root password | `securepassword` |

Estas credenciales coinciden con las que usa `ConnectionManagerSingleton`

Para bajar la base:

```bash
docker compose down
```

Para bajarla y borrar los datos persistidos:

```bash
docker compose down -v
```

## Correr la aplicación

Con la base ya levantada:

```bash
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```

O directamente desde el IDE, ejecutando `org.example.Main`.
