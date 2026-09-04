package org.example.dao;

import java.sql.SQLException;

public interface SchemaDAO {

    void createTables() throws SQLException;
    void dropTables() throws SQLException;

}
