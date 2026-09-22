# Integrador 2

Configuración de JPA (Hibernate) contra MySQL.

## Requisitos

- JDK 24+
- Maven
- Docker y Docker Compose (solo para la base de datos)

## Levantar la base de datos

La base de datos corre en un contenedor MySQL, la aplicación corre local (fuera de Docker).

```bash
docker compose up -d
```

**Para que el comando funcione tiene que estar abierto Docker Desktop**

Esto levanta MySQL 8.0 en `localhost:3307` con:

| Variable | Valor |
|---|---|
| Base de datos | `db-tp-integrador-2` |
| Usuario | `app_user` |
| Password | `12345` |
| Root password | `securepassword` |

Estas credenciales coinciden con las que usa `persistence.xml` / `JPAUtil`.

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
