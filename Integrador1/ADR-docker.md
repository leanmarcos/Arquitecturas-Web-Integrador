Decisión: Dockerizar solo la base de datos (MySQL), no la aplicación.

Justificación: Dockerizar solo la DB evita instalar MySQL localmente (garantizando la misma versión para todo el 
equipo) y evita tiempos de reconstrucción de imagen en cada cambio de código. 