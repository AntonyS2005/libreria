package com.example.li;


import javafx.event.ActionEvent;
import modelo.Usuario;
import db.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;
    Tools tools = new Tools();
    public void salir(ActionEvent event){
        tools.salir(event);
    }
    public void login(ActionEvent event){
        tools.abrirLogin(event);
    }
    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.obtenerUsuarioPorUsernameYPassword(username, password);

        if (usuario != null) {
            try {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                if (usuario.getRole().equals("admin")) {
                    Parent root = FXMLLoader.load(getClass().getResource("AdminMenu.fxml"));
                    stage.setTitle("Admin Menu");
                    stage.setScene(new Scene(root));
                } else if (usuario.getRole().equals("vendedor")) {
                    Parent root = FXMLLoader.load(getClass().getResource("ventas.fxml"));
                    stage.setTitle("Vendedor Menu");
                    stage.setScene(new Scene(root));
                }
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Autenticación");
            alert.setHeaderText("Usuario o Contraseña incorrecta");
            alert.setContentText("Por favor, inténtelo de nuevo.");
            alert.showAndWait();
        }
    }
}
