package com.example.li;

import db.VentasDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import modelo.Producto;

import java.sql.SQLException;
import java.util.Optional;

public class VentasController {
    @FXML
    private TextField txtBuscarProducto, txtCantidad, txtDineroDado;
    @FXML
    private TableView<Producto> tblProductos;
    @FXML
    private TableView<Producto> tblCarrito;
    @FXML
    private TableColumn<Producto, String> colNombreProducto;
    @FXML
    private TableColumn<Producto, Double> colPrecioProducto;
    @FXML
    private TableColumn<Producto, Integer> colStockProducto;
    @FXML
    private TableColumn<Producto, String> colNombreCarrito;
    @FXML
    private TableColumn<Producto, Double> colPrecioCarrito;
    @FXML
    private TableColumn<Producto, Integer> colCantidadCarrito;
    @FXML
    private Label lblTotal, lblVuelto;

    private ObservableList<Producto> listaProductos;
    private ObservableList<Producto> carrito;
    private VentasDAO ventasDAO;

    public VentasController() {
        this.listaProductos = FXCollections.observableArrayList();
        this.carrito = FXCollections.observableArrayList();
        this.ventasDAO = new VentasDAO();
    }

    @FXML
    public void initialize() {
        colNombreProducto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colPrecioProducto.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecio()).asObject());
        colStockProducto.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());

        colNombreCarrito.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colPrecioCarrito.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecio()).asObject());
        colCantidadCarrito.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());

        tblProductos.setItems(listaProductos);
        tblCarrito.setItems(carrito);

        tblCarrito.setOnMouseClicked(this::handleEditOrRemove);
    }

    @FXML
    public void buscarProducto(ActionEvent event) {
        String nombre = txtBuscarProducto.getText();
        try {
            listaProductos.clear();
            listaProductos.addAll(ventasDAO.buscarProducto(nombre));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void agregarProducto(ActionEvent event) {
        Producto productoSeleccionado = tblProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= productoSeleccionado.getStock() && cantidad > 0) {
                Producto productoCarrito = new Producto(
                        productoSeleccionado.getId(),
                        productoSeleccionado.getNombre(),
                        productoSeleccionado.getCosto(),
                        productoSeleccionado.getPrecio(),
                        cantidad
                );
                carrito.add(productoCarrito);
                actualizarTotal();
            } else {
                mostrarAlerta("Cantidad inválida", "La cantidad ingresada no es válida.");
            }
        }
    }

    private void handleEditOrRemove(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Producto productoSeleccionado = tblCarrito.getSelectionModel().getSelectedItem();
            if (productoSeleccionado != null) {
                mostrarDialogoEditarOEliminar(productoSeleccionado);
            }
        }
    }

    private void mostrarDialogoEditarOEliminar(Producto producto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Editar o Eliminar Producto");
        alert.setHeaderText("¿Qué deseas hacer?");
        ButtonType buttonTypeEditar = new ButtonType("Editar");
        ButtonType buttonTypeEliminar = new ButtonType("Eliminar");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeEditar, buttonTypeEliminar, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeEditar) {
            editarProducto(producto);
        } else if (result.get() == buttonTypeEliminar) {
            carrito.remove(producto);
            actualizarTotal();
        }
    }

    private void editarProducto(Producto producto) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(producto.getStock()));
        dialog.setTitle("Editar Cantidad");
        dialog.setHeaderText("Editar cantidad del producto: " + producto.getNombre());
        dialog.setContentText("Cantidad:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(cantidadStr -> {
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad > 0) {
                producto.setStock(cantidad);
                tblCarrito.refresh();
                actualizarTotal();
            } else {
                mostrarAlerta("Cantidad inválida", "La cantidad ingresada no es válida.");
            }
        });
    }

    private void actualizarTotal() {
        double total = carrito.stream().mapToDouble(p -> p.getPrecio() * p.getStock()).sum();
        lblTotal.setText(String.valueOf(total));
    }

    @FXML
    public void calcularVuelto(ActionEvent event) {
        double total = Double.parseDouble(lblTotal.getText());
        double dineroDado = Double.parseDouble(txtDineroDado.getText());
        double vuelto = dineroDado - total;
        lblVuelto.setText(String.valueOf(vuelto));
    }

    @FXML
    public void venderProductos(ActionEvent event) {
        try {
            for (Producto producto : carrito) {
                Producto productoEnLista = listaProductos.stream()
                        .filter(p -> p.getId() == producto.getId())
                        .findFirst()
                        .orElse(null);
                if (productoEnLista != null) {
                    int nuevoStock = productoEnLista.getStock() - producto.getStock();
                    ventasDAO.actualizarStock(producto.getId(), nuevoStock);
                    productoEnLista.setStock(nuevoStock);
                }
            }
            carrito.clear();
            txtBuscarProducto.clear();
            txtCantidad.clear();
            txtDineroDado.clear();
            lblTotal.setText("0.0");
            lblVuelto.setText("0.0");
            tblProductos.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
    Tools tools = new Tools();
    public void salir(ActionEvent event){
        tools.salir(event);
    }
    public void login(ActionEvent event){
        tools.abrirLogin(event);
    }
}
