package modelo;

    public class Usuario {
        private int id;
        private String nombre;
        private String username;
        private String password;
        private String role;

        // Constructor
        public Usuario(int id, String nombre, String username, String password, String role) {
            this.id = id;
            this.nombre = nombre;
            this.username = username;
            this.password = password;
            this.role = role;
        }
        public Usuario(String nombre, String username, String password, String role) {
            this.nombre = nombre;
            this.username = username;
            this.password = password;
            this.role = role;
        }

        // Getters y Setters
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

