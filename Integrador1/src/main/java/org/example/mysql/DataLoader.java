package org.example.mysql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class DataLoader {

    private static final String DATA_PACKAGE = "org/example/data/";
    private final Connection connection;

    public DataLoader(Connection connection) {
        this.connection = connection;
    }

    public void loadAllData() throws SQLException, IOException {
        boolean previousAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);

        try {
            int clientes = loadClientes();
            int productos = loadProductos();
            int facturas = loadFacturas();
            int detalles = loadFacturasProductos();
            connection.commit();

            System.out.println("Clientes cargados: " + clientes);
            System.out.println("Productos cargados: " + productos);
            System.out.println("Facturas cargadas: " + facturas);
            System.out.println("Detalles de facturas cargados: " + detalles);
        } catch (SQLException | IOException | RuntimeException exception) {
            connection.rollback();
            throw exception;
        } finally {
            connection.setAutoCommit(previousAutoCommit);
        }
    }

    private int loadClientes() throws SQLException, IOException {
        String sql = "INSERT INTO cliente (idCliente, nombre, email) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), email = VALUES(email)";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             CSVParser parser = parseCsv("clientes.csv")) {
            int count = 0;
            for (CSVRecord row : parser) {
                statement.setInt(1, Integer.parseInt(row.get("idCliente")));
                statement.setString(2, row.get("nombre"));
                statement.setString(3, row.get("email"));
                statement.addBatch();
                count++;
            }
            statement.executeBatch();
            return count;
        }
    }

    private int loadProductos() throws SQLException, IOException {
        String sql = "INSERT INTO producto (idProducto, nombre, valor) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), valor = VALUES(valor)";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             CSVParser parser = parseCsv("productos.csv")) {
            int count = 0;
            for (CSVRecord row : parser) {
                statement.setInt(1, Integer.parseInt(row.get("idProducto")));
                statement.setString(2, row.get("nombre"));
                statement.setFloat(3, Float.parseFloat(row.get("valor")));
                statement.addBatch();
                count++;
            }
            statement.executeBatch();
            return count;
        }
    }

    private int loadFacturas() throws SQLException, IOException {
        String sql = "INSERT INTO factura (idFactura, idCliente) VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE idCliente = VALUES(idCliente)";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             CSVParser parser = parseCsv("facturas.csv")) {
            int count = 0;
            for (CSVRecord row : parser) {
                statement.setInt(1, Integer.parseInt(row.get("idFactura")));
                statement.setInt(2, Integer.parseInt(row.get("idCliente")));
                statement.addBatch();
                count++;
            }
            statement.executeBatch();
            return count;
        }
    }

    private int loadFacturasProductos() throws SQLException, IOException {
        String sql = "INSERT INTO factura_producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE cantidad = VALUES(cantidad)";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             CSVParser parser = parseCsv("facturas-productos.csv")) {
            int count = 0;
            for (CSVRecord row : parser) {
                statement.setInt(1, Integer.parseInt(row.get("idFactura")));
                statement.setInt(2, Integer.parseInt(row.get("idProducto")));
                statement.setInt(3, Integer.parseInt(row.get("cantidad")));
                statement.addBatch();
                count++;
            }
            statement.executeBatch();
            return count;
        }
    }

    private CSVParser parseCsv(String fileName) throws IOException {
        Reader reader = openReader(fileName);
        return CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build()
                .parse(reader);
    }

    private Reader openReader(String fileName) throws IOException {
        String resourcePath = DATA_PACKAGE + fileName;
        InputStream input = DataLoader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (input != null) {
            return new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        }

        Path sourcePath = Path.of("src", "main", "java", "org", "example", "data", fileName);
        if (Files.exists(sourcePath)) {
            return Files.newBufferedReader(sourcePath, StandardCharsets.UTF_8);
        }

        throw new IOException("No se encontró el archivo CSV: " + fileName);
    }
}
