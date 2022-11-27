package me.cosmic.networkcore.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private String host = "byrd.bloom.host";
    private String port = "3306";
    private String database = "s25473_NetworkCore";
    private String username = "u25473_NamweZTnPM";
    private String password = "6stqByvYURcYNwHKNzpLJOqQ";

    private Connection connection;

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() throws SQLException {
        if (!isConnected())
            connection = DriverManager.getConnection("jdbc:mysql://" +
                            host + ":" + port + "/" + database + "?useSSL=false",
                    username, password);
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

}
