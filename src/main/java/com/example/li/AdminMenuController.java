package com.example.li;

import db.ProductosDAO;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modelo.Producto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AdminMenuController {

    @FXML
    private Button agregarButton;

    @FXML
    private Button editarButton;

    @FXML
    private Button verStockButton;

    @FXML
    private Button cerrarSesionButton;

    @FXML
    private TextField buscarField;

    @FXML
    private ComboBox<Producto> resultadosBusqueda;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField costoField;

    @FXML
    private TextField precioField;

    @FXML
    private TextField stockField;

    @FXML
    private TextField nombreEditarField;

    @FXML
    private TextField costoEditarField;

    @FXML
    private TextField precioEditarField;

    @FXML
    private TextField stockEditarField;

    @FXML
    private TextField buscarStockField;

    @FXML
    private ComboBox<Producto> resultadosBusquedaStock;
    private ProductosDAO productosDAO;

    @FXML
    private Label detallesProductoLabel;

    Tools tools = new Tools();
    public void salir(ActionEvent event){
        tools.salir(event);
    }
    public void menu(ActionEvent event){
        tools.Menu(event);
    }
    public AdminMenuController() throws SQLException {
        productosDAO = new ProductosDAO();
    }

    @FXML
    private void initialize() {
        if (resultadosBusqueda != null) {
            resultadosBusqueda.setOnAction(event -> handleSeleccionProductoAction());
        }
        if (resultadosBusquedaStock != null) {
            resultadosBusquedaStock.setOnAction(event -> handleVerProductoStockAction());
        }
    }

    @FXML
    private void handleAgregarButtonAction() throws IOException {
        cargarFXML("AgregarProducto.fxml");
    }

    @FXML
    private void handleEditarButtonAction() throws IOException {
        cargarFXML("EditarProducto.fxml");
    }

    @FXML
    private void handleVerStockButtonAction() throws IOException {
        cargarFXML("VerStock.fxml");
    }

    @FXML
    private void handleCerrarSesionButtonAction() throws IOException {
        cargarFXML("Login.fxml");
    }

    @FXML
    private void handleAgregarProductoAction() throws SQLException {
        String nombre = nombreField.getText();
        double costo = Double.parseDouble(costoField.getText());
        double precio = Double.parseDouble(precioField.getText());
        int stock = Integer.parseInt(stockField.getText());

        productosDAO.agregarProducto(nombre, costo, precio, stock);
        mostrarInformacion("Éxito", "Producto agregado correctamente.");
    }

    @FXML
    private void handleBuscarProductoAction() throws SQLException {
        String nombre = buscarField.getText();
        List<Producto> productos = productosDAO.buscarProductoPorNombre(nombre);
        resultadosBusqueda.getItems().clear();
        resultadosBusqueda.getItems().addAll(productos);
    }

    @FXML
    private void handleSeleccionProductoAction() {
        Producto producto = resultadosBusqueda.getSelectionModel().getSelectedItem();
        if (producto != null) {
            nombreEditarField.setText(producto.getNombre());
            costoEditarField.setText(String.valueOf(producto.getCosto()));
            precioEditarField.setText(String.valueOf(producto.getPrecio()));
            stockEditarField.setText(String.valueOf(producto.getStock()));
        }
    }

    @FXML
    private void handleEditarProductoAction() throws SQLException {
        Producto producto = resultadosBusqueda.getSelectionModel().getSelectedItem();
        if (producto != null) {
            String nombre = nombreEditarField.getText();
            double costo = Double.parseDouble(costoEditarField.getText());
            double precio = Double.parseDouble(precioEditarField.getText());
            int stock = Integer.parseInt(stockEditarField.getText());

            productosDAO.editarProducto(producto.getId(), nombre, costo, precio, stock);
            mostrarInformacion("Éxito", "Producto editado correctamente.");
        }
    }

    @FXML
    private void handleBuscarStockAction() throws SQLException {
        String nombre = buscarStockField.getText();
        List<Producto> productos = productosDAO.buscarProductoPorNombre(nombre);
        resultadosBusquedaStock.getItems().clear();
        resultadosBusquedaStock.getItems().addAll(productos);
    }

    @FXML
    private void handleVerProductoStockAction() {
        Producto producto = resultadosBusquedaStock.getSelectionModel().getSelectedItem();
        if (producto != null) {
            detallesProductoLabel.setText("Nombre: " + producto.getNombre() +
                    "\nCosto: " + producto.getCosto() +
                    "\nPrecio: " + producto.getPrecio() +
                    "\nStock: " + producto.getStock());
        }
    }

    private void cargarFXML(String fxml) throws IOException {
        Stage stage = (Stage) agregarButton.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource(fxml));
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    @FXML
    private void handleEliminarProductoAction() throws SQLException {
        Producto producto = resultadosBusqueda.getSelectionModel().getSelectedItem();
        if (producto != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de que desea eliminar el producto seleccionado?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                productosDAO.eliminarProducto(producto.getId());
                mostrarInformacion("Éxito", "Producto eliminado correctamente.");
            }
        } else {
            mostrarError("Error", "Seleccione un producto para eliminar.");
        }
    }

    @FXML
    private void handleUsuariosButtonAction() throws IOException {
        cargarFXML("Usuarios.fxml");
    }

}

