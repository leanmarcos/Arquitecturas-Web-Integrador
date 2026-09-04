package org.example;

import java.sql.Connection;
import java.sql.SQLException;

import org.example.connection.ConnectionManagerSingleton;
import org.example.csv.CsvImporter;
import org.example.factory.DAOFactory;
import org.example.factory.DbEngine;
import org.example.mysql.DataLoader;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        Connection  conn = ConnectionManagerSingleton.getInstance().getConnection();
        CsvImporter csvImporter = new CsvImporter();
        DAOFactory factory = DAOFactory.getInstance(DbEngine.MYSQL, conn);

        factory.getSchemaDAO().createTables();

        DataLoader loader = new DataLoader(conn, factory, csvImporter);
        loader.loadAllData();
    }
}