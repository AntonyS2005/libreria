package db;

import db.Conexion;
import modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentasDAO {
    private Conexion conexion;

    public VentasDAO() {
        this.conexion = new Conexion();
    }

    public List<Producto> buscarProducto(String nombre) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Productos WHERE nombre LIKE ?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("costo"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                ));
            }
        }
        return productos;
    }

    public void actualizarStock(int id, int nuevoStock) throws SQLException {
        String sql = "UPDATE Productos SET stock = ? WHERE id = ?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
}
