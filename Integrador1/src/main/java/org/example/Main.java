package org.example;

import org.example.connection.ConnectionManagerSingleton;
import org.example.csv.CsvImporter;
import org.example.dao.ClienteDAO;
import org.example.dao.ProductoDAO;
import org.example.dto.ClienteFacturadoDTO;
import org.example.dto.ProductoRecaudadoDTO;
import org.example.factory.DAOFactory;
import org.example.factory.DbEngine;
import org.example.loader.DataLoader;
import org.example.loader.DataResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {

    public static void main(String[] args) throws SQLException {

        Connection  conn = ConnectionManagerSingleton.getInstance().getConnection();
        CsvImporter csvImporter = new CsvImporter();
        DAOFactory factory = DAOFactory.getInstance(DbEngine.MYSQL, conn);

        factory.getSchemaDAO().dropTables();
        factory.getSchemaDAO().createTables();

        DataLoader loader = new DataLoader(conn, factory, csvImporter);
        DataResult resultado = loader.loadAllData();
        System.out.println(resultado);

        // Devuelve el producto mas recaudador. Punto 3 del TPE
        ProductoDAO productoDAO = factory.getProductoDAO();
        ProductoRecaudadoDTO productoMasRecaudador = productoDAO.getProductoMayorRecaudacion();

        System.out.println("3) Producto que más recaudó:");
        if (productoMasRecaudador != null) {
            System.out.printf("   ID: %d | Nombre: %s | Valor: $%.2f | Recaudación: $%.2f%n",
                    productoMasRecaudador.idProducto(),
                    productoMasRecaudador.nombre(),
                    productoMasRecaudador.valor(),
                    productoMasRecaudador.recaudacion());
        } else {
            System.out.println("   No hay registros.");
        }
        System.out.println();

        // Devuelve una lista ordenada de mayor a menor gasto de clientes. Punto 4 del TPE
        ClienteDAO clienteImp = factory.getClienteDAO();
        List<ClienteFacturadoDTO> listaClientes = clienteImp.getClientsOrderedByBilling();

        System.out.println("4) Clientes ordenados por facturación:");
        for (ClienteFacturadoDTO c : listaClientes) {
            System.out.printf("   ID: %-3d | Nombre: %-22s | Email: %-32s | Total: $%.2f%n",
                    c.idCliente(),
                    c.nombre(),
                    c.email(),
                    c.gastoTotal());
        }




    }

}