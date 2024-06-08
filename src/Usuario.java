public class Usuario {
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String direccion;
    protected String contrasena;
    protected int id;
    protected String rol;
    protected BaseDeDatos baseDeDatos;

    public Usuario(String nombre, String apellido, String email, String direccion, String contrasena, int id, String rol, BaseDeDatos baseDeDatos) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.direccion = direccion;
        this.contrasena = contrasena;
        this.id = id;
        this.rol = rol;
        this.baseDeDatos = baseDeDatos;
    }


    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return nombre;
    }

    public String getApellido() {
        return nombre;
    }

    public String getDireccion() {
        return nombre;
    }

    public String getContrasena() {
        return nombre;
    }

    public BaseDeDatos getBaseDeDatos() {
        return baseDeDatos;
    }

    public String getRol() {
        return rol;
    }

    public boolean iniciarSesion(String email, String contrasena) {
        return this.email.equals(email) && this.contrasena.equals(contrasena);
    }
}
