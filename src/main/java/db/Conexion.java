package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private Connection conexion;

    public Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                conexion = DriverManager.getConnection("jdbc:sqlite:dblib.db");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException("Error al cargar el driver de SQLite", e);
            }
        }
        return conexion;
    }
}
