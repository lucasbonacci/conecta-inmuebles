import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        return email;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public BaseDeDatos getBaseDeDatos() {
        return baseDeDatos;
    }

    public String getRol() {
        return rol;
    }

    public static void registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (nombre, apellido, email, direccion, contrasena, rol, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = usuario.baseDeDatos.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApellido());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getDireccion());
            pstmt.setString(5, usuario.getContrasena());
            pstmt.setString(6, usuario.getRol());
            pstmt.setString(7, "activo");

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Usuario obtenerUsuarioPorEmail(BaseDeDatos baseDeDatos, String email) {
        String sql = "SELECT * FROM Usuarios WHERE email = ?";
        try (Connection connection = baseDeDatos.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("usuario_id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String direccion = rs.getString("direccion");
                String contrasena = rs.getString("contrasena");
                String rol = rs.getString("rol");
                String estado = rs.getString("estado");

                if (estado.equals("suspendido")) {
                    return null;
                }

                if (rol.equals("administrador")) {
                    return new Administrador(nombre, apellido, email, direccion, contrasena, id, baseDeDatos);
                } else if (rol.equals("agente")) {
                    return new Agente(nombre, apellido, email, direccion, contrasena, id, baseDeDatos);
                } else {
                    return new Cliente(nombre, apellido, email, direccion, contrasena, id, baseDeDatos);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuario por email: " + e.getMessage());
        }
        return null;
    }


    public boolean iniciarSesion(String email, String contrasena) {
        return this.email.equals(email) && this.contrasena.equals(contrasena);
    }
}
