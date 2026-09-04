# Código mermaid original

![`src/main/resources/docs/integrador1-diagram.png`](src/main/resources/docs/integrador1.diagram.png)

flowchart TD

    %% =====================================================
    %% MAIN
    %% =====================================================

    MAIN["Main<br/><br/>Sincroniza<br/>y muestra resultado final"]


    %% =====================================================
    %% RESOURCES / CSV
    %% =====================================================

    subgraph RESOURCES["Resources"]
        DATA["data/"]
        CSV_CLIENTES["clientes.csv"]
        CSV_PRODUCTOS["productos.csv"]
        CSV_FACTURAS["facturas.csv"]
        CSV_FP["factura_producto.csv"]

        DATA --> CSV_CLIENTES
        DATA --> CSV_PRODUCTOS
        DATA --> CSV_FACTURAS
        DATA --> CSV_FP
    end


    %% =====================================================
    %% CSV
    %% =====================================================

    subgraph CSV_LAYER["csv/"]
        CSV_IMPORTER["CsvImporter<br/><br/>import(path)<br/>Lee CSV<br/>Devuelve List"]
    end

    CSV_CLIENTES --> CSV_IMPORTER
    CSV_PRODUCTOS --> CSV_IMPORTER
    CSV_FACTURAS --> CSV_IMPORTER
    CSV_FP --> CSV_IMPORTER


    %% =====================================================
    %% LOADER
    %% =====================================================

    subgraph LOADER["loader/"]
        DATA_LOADER["DataLoader<br/><br/>Carga CSVs<br/>Crea Entities<br/>Obtiene DAOs<br/>Ejecuta insertBatch()"]
        DATA_RESULT["DataResult<br/><br/>Formato del resultado"]
    end

    MAIN -->|"ejecuta"| DATA_LOADER

    CSV_IMPORTER -->|"datos"| DATA_LOADER

    DATA_LOADER -->|"resultado"| DATA_RESULT
    DATA_RESULT -->|"resultado final"| MAIN


    %% =====================================================
    %% ENTITIES
    %% =====================================================

    subgraph ENTITY["entity/"]
        CLIENTE["Cliente"]
        PRODUCTO["Producto"]
        FACTURA["Factura"]
        FACTURA_PRODUCTO["FacturaProducto"]
    end

    DATA_LOADER -->|"crea"| CLIENTE
    DATA_LOADER -->|"crea"| PRODUCTO
    DATA_LOADER -->|"crea"| FACTURA
    DATA_LOADER -->|"crea"| FACTURA_PRODUCTO


    %% =====================================================
    %% FACTORY
    %% =====================================================

    subgraph FACTORY["factory/"]
        DB_ENGINE["DbEngine<br/><br/>Enum<br/>MySQL"]

        DAO_FACTORY["DAOFactory<br/><br/>Clase abstracta<br/><br/>getClienteDAO()<br/>getProductoDAO()<br/>getFacturaDAO()<br/>getFacturaProductoDAO()<br/>getSchemaDAO()"]

        MYSQL_FACTORY["MySQLFactory<br/><br/>Implementación de DAOFactory"]
    end

    DB_ENGINE -->|"selecciona DB"| DAO_FACTORY

    DAO_FACTORY -->|"DB = MySQL"| MYSQL_FACTORY

    DATA_LOADER -->|"solicita DAO"| DAO_FACTORY


    %% =====================================================
    %% DAO INTERFACES
    %% =====================================================

    subgraph DAO["dao/"]
        CLIENTE_DAO["ClienteDAO<br/><br/>CRUD<br/>insertBatch()"]
        PRODUCTO_DAO["ProductoDAO<br/><br/>CRUD<br/>insertBatch()"]
        FACTURA_DAO["FacturaDAO<br/><br/>CRUD<br/>insertBatch()"]
        FACTURA_PRODUCTO_DAO["FacturaProductoDAO<br/><br/>CRUD<br/>insertBatch()"]
        SCHEMA_DAO["SchemaDAO<br/><br/>createTable()<br/>dropTable()"]
    end


    %% =====================================================
    %% MYSQL IMPLEMENTATIONS
    %% =====================================================

    subgraph MYSQL_DAO["mysql/"]
        MYSQL_CLIENTE["MySqlClienteDAO"]
        MYSQL_PRODUCTO["MySqlProductoDAO"]
        MYSQL_FACTURA["MySqlFacturaDAO"]
        MYSQL_FACTURA_PRODUCTO["MySqlFacturaProductoDAO"]
        MYSQL_SCHEMA["MySqlSchemaDAO"]
    end


    %% Factory returns concrete implementations
    MYSQL_FACTORY -->|"devuelve"| MYSQL_CLIENTE
    MYSQL_FACTORY -->|"devuelve"| MYSQL_PRODUCTO
    MYSQL_FACTORY -->|"devuelve"| MYSQL_FACTURA
    MYSQL_FACTORY -->|"devuelve"| MYSQL_FACTURA_PRODUCTO
    MYSQL_FACTORY -->|"devuelve"| MYSQL_SCHEMA


    %% Implementations implement interfaces
    MYSQL_CLIENTE -.->|"implementa"| CLIENTE_DAO
    MYSQL_PRODUCTO -.->|"implementa"| PRODUCTO_DAO
    MYSQL_FACTURA -.->|"implementa"| FACTURA_DAO
    MYSQL_FACTURA_PRODUCTO -.->|"implementa"| FACTURA_PRODUCTO_DAO
    MYSQL_SCHEMA -.->|"implementa"| SCHEMA_DAO


    %% DataLoader uses DAOs
    DATA_LOADER -->|"insertBatch()"| CLIENTE_DAO
    DATA_LOADER -->|"insertBatch()"| PRODUCTO_DAO
    DATA_LOADER -->|"insertBatch()"| FACTURA_DAO
    DATA_LOADER -->|"insertBatch()"| FACTURA_PRODUCTO_DAO


    %% =====================================================
    %% CONNECTION
    %% =====================================================

    subgraph CONNECTION["connection/"]
        CONNECTION_MANAGER["ConnectionManagerSingleton<br/><br/>URL<br/>USER<br/>PASSWORD<br/><br/>Singleton<br/>Devuelve Connection"]
    end


    MYSQL_CLIENTE --> CONNECTION_MANAGER
    MYSQL_PRODUCTO --> CONNECTION_MANAGER
    MYSQL_FACTURA --> CONNECTION_MANAGER
    MYSQL_FACTURA_PRODUCTO --> CONNECTION_MANAGER
    MYSQL_SCHEMA --> CONNECTION_MANAGER


    %% =====================================================
    %% DATABASE
    %% =====================================================

    subgraph DATABASE["Base de datos"]
        MYSQL["MySQL"]
    end

    CONNECTION_MANAGER -->|"Connection"| MYSQL