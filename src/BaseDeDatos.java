import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDeDatos {
    private static final String URL = "jdbc:mysql://localhost:3306/conecta_inmuebles";
    private static final String USER = "root";
    private static final String PASSWORD = "rootroot";

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (nombre, apellido, email, direccion, contrasena, rol) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApellido());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getDireccion());
            pstmt.setString(5, usuario.getContrasena());
            pstmt.setString(6, usuario.getRol());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        String sql = "SELECT * FROM Usuarios WHERE email = ?";
        try (Connection connection = conectar();
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

                if (rol.equals("administrador")) {
                    return new Administrador(nombre, apellido, email, direccion, contrasena, id, this);
                } else if (rol.equals("agente")) {
                    return new Agente(nombre, apellido, email, direccion, contrasena, id, this);
                } else {
                    return new Cliente(nombre, apellido, email, direccion, contrasena, id, this);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuario por email: " + e.getMessage());
        }
        return null;
    }

}
