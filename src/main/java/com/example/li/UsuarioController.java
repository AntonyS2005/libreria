package com.example.li;

import db.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import modelo.Usuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsuarioController {

    @FXML
    private TextField buscarUsuarioField;

    @FXML
    private ComboBox<Usuario> resultadosBusquedaUsuario;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField roleField;

    private UsuarioDAO usuarioDAO;

    public UsuarioController() {
        usuarioDAO = new UsuarioDAO();
    }

    @FXML
    private void initialize() {
        if (resultadosBusquedaUsuario != null) {
            resultadosBusquedaUsuario.setConverter(new StringConverter<>() {
                @Override
                public String toString(Usuario usuario) {
                    return usuario != null ? usuario.getNombre() : "";
                }

                @Override
                public Usuario fromString(String string) {
                    return null;
                }
            });
            resultadosBusquedaUsuario.setOnAction(event -> handleSeleccionUsuarioAction());
        }
    }

    @FXML
    private void handleBuscarUsuarioAction() throws SQLException {
        String nombre = buscarUsuarioField.getText();
        List<Usuario> usuarios = usuarioDAO.buscarUsuarioPorNombre(nombre);
        resultadosBusquedaUsuario.getItems().clear();
        resultadosBusquedaUsuario.getItems().addAll(usuarios);
    }

    @FXML
    private void handleSeleccionUsuarioAction() {
        Usuario usuario = resultadosBusquedaUsuario.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            nombreField.setText(usuario.getNombre());
            usernameField.setText(usuario.getUsername());
            passwordField.setText(usuario.getPassword());
            roleField.setText(usuario.getRole());
        }
    }

    @FXML
    private void handleAgregarUsuarioAction() throws SQLException {
        String nombre = nombreField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();

        Usuario nuevoUsuario = new Usuario(nombre, username, password, role);
        if (usuarioDAO.insertarUsuario(nuevoUsuario)) {
            mostrarInformacion("Éxito", "Usuario agregado correctamente.");
        } else {
            mostrarError("Error", "No se pudo agregar el usuario.");
        }
    }

    @FXML
    private void handleEditarUsuarioAction() throws SQLException {
        Usuario usuario = resultadosBusquedaUsuario.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            usuario.setNombre(nombreField.getText());
            usuario.setUsername(usernameField.getText());
            usuario.setPassword(passwordField.getText());
            usuario.setRole(roleField.getText());

            if (usuarioDAO.actualizarUsuario(usuario)) {
                mostrarInformacion("Éxito", "Usuario editado correctamente.");
            } else {
                mostrarError("Error", "No se pudo editar el usuario.");
            }
        }
    }

    @FXML
    private void handleEliminarUsuarioAction() throws SQLException {
        Usuario usuario = resultadosBusquedaUsuario.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de que desea eliminar el usuario seleccionado?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (usuarioDAO.eliminarUsuario(usuario.getId())) {
                    mostrarInformacion("Éxito", "Usuario eliminado correctamente.");
                } else {
                    mostrarError("Error", "No se pudo eliminar el usuario.");
                }
            }
        } else {
            mostrarError("Error", "Seleccione un usuario para eliminar.");
        }
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
}
