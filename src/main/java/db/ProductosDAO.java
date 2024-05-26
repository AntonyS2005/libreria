package db;
import modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductosDAO {

    public void agregarProducto(String nombre, double costo, double precio, int stock) {
        Conexion con = new Conexion();
        try (Connection conexion = con.getConexion();
             PreparedStatement stmt = conexion.prepareStatement("INSERT INTO Productos (nombre, costo, precio, stock) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, nombre);
            stmt.setDouble(2, costo);
            stmt.setDouble(3, precio);
            stmt.setInt(4, stock);
            stmt.executeUpdate();
            System.out.println("Producto agregado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al agregar el producto: " + e.getMessage());
        }
    }

    public void editarProducto(int id, String nombre, double costo, double precio, int stock) {
        Conexion con = new Conexion();
        try (Connection conexion = con.getConexion();
             PreparedStatement stmt = conexion.prepareStatement("UPDATE Productos SET nombre = ?, costo = ?, precio = ?, stock = ? WHERE id = ?")) {
            stmt.setString(1, nombre);
            stmt.setDouble(2, costo);
            stmt.setDouble(3, precio);
            stmt.setInt(4, stock);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            System.out.println("Producto editado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al editar el producto: " + e.getMessage());
        }
    }

    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        Conexion con = new Conexion();
        try (Connection conexion = con.getConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Productos")) {
            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("costo"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener los productos: " + e.getMessage());
        }
        return productos;
    }

    public List<Producto> buscarProductoPorNombre(String nombre) {
        List<Producto> productos = new ArrayList<>();
        Conexion con = new Conexion();
        try (Connection conexion = con.getConexion();
             PreparedStatement stmt = conexion.prepareStatement("SELECT * FROM Productos WHERE nombre LIKE ?")) {
            stmt.setString(1, "%" + nombre + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getDouble("costo"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    );
                    productos.add(producto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al buscar productos por nombre: " + e.getMessage());
        }
        return productos;
    }

    public void eliminarProducto(int id) {
        Conexion con = new Conexion();
        try (Connection conexion = con.getConexion();
             PreparedStatement stmt = conexion.prepareStatement("DELETE FROM Productos WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Producto eliminado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar el producto: " + e.getMessage());
        }
    }
}
