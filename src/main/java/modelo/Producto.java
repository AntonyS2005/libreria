package modelo;


public class Producto {
    private int id;
    private String nombre;
    private double costo;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, double costo, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.costo = costo;
        this.precio = precio;
        this.stock = stock;
    }
    public Producto( String nombre, double costo, double precio ) {

        this.nombre = nombre;
        this.costo = costo;
        this.precio = precio;
    }


    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return nombre + " (ID: " + id + ")";
    }
}
