package com.example.li;

import db.ProductosDAO;
import db.UsuarioDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import modelo.Producto;

import java.util.List;

public class VendedorMenu {
    @FXML
    private TextField TFnom,TFdin,TFca;
    @FXML
    private TableColumn<Producto,String> CPP,CPS,CPPr;
    @FXML
    private TableColumn<Object,String> CVP,CVPr,CVC,CVS;
    @FXML
    private Label tot,vuel;
    @FXML
    private TableView<Producto>  Productos;


    public void serchProduct(){
        String nombre;
        nombre=TFnom.getText();
        ProductosDAO productosDAO = new ProductosDAO();
        List list = productosDAO.buscarProductoPorNombre(nombre);


    }

}
