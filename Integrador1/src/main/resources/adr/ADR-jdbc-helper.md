# Por qué no se abstrajo el prepare/bind/execute/catch de JDBC en un helper

Se evaluó la implementación de una capa de abstracción intermedia (mediante un JdbcHelper o Template Method
funcional) para encapsular la preparación de statements, ejecución y captura de SQLException. Sin embargo, para
esta entrega se priorizó mantener el flujo de JDBC explícito en cada DAO. Meter una capa de abstracción encima de
eso —por más liviana que sea— esconde exactamente el flujo que la consigna pide entender y practicar: cómo se
prepara un statement, cómo se bindean los parámetros, cómo se ejecuta y cómo se maneja el error, método por
método, sin intermediarios.
