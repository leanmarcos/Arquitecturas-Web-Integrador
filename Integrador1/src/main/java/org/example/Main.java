package org.example;

import org.example.connection.ConnectionManagerSingleton;
import org.example.csv.CsvImporter;
import org.example.factory.DAOFactory;
import org.example.factory.DbEngine;
import org.example.mysql.DataLoader;

import java.sql.Connection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Connection  conn = ConnectionManagerSingleton.getInstance().getConnection();
        CsvImporter csvImporter = new CsvImporter();
        DAOFactory factory = DAOFactory.getInstance(DbEngine.MYSQL, conn);

        DataLoader loader = new DataLoader(conn, factory, csvImporter);
    }
}