import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Administrador extends Usuario {
    public Administrador(String nombre, String apellido, String email, String direccion, String contrasena, int id, BaseDeDatos baseDeDatos) {
        super(nombre, apellido, email, direccion, contrasena, id, "administrador", baseDeDatos);
    }

    public void registrarAgente(Scanner scanner) {
        System.out.print("Ingrese el nombre del agente: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el apellido del agente: ");
        String apellido = scanner.nextLine();
        System.out.print("Ingrese el email del agente: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese la dirección del agente: ");
        String direccion = scanner.nextLine();
        System.out.print("Ingrese la contraseña del agente: ");
        String contrasena = scanner.nextLine();

        Agente nuevoAgente = new Agente(nombre, apellido, email, direccion, contrasena, 0, baseDeDatos);
        Usuario.registrarUsuario(nuevoAgente);

        System.out.println("Agente registrado exitosamente.");
    }

    public void suspenderAgente(Scanner scanner) {
        System.out.print("Ingrese el ID del agente que desea suspender: ");
        int agenteId = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE Usuarios SET estado = 'suspendido' WHERE usuario_id = ? AND rol = 'agente'";
        try (Connection connection = baseDeDatos.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, agenteId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Agente suspendido exitosamente.");
            } else {
                System.out.println("No se encontró el agente o el agente ya está suspendido.");
            }
        } catch (SQLException e) {
            System.out.println("Error al suspender el agente: " + e.getMessage());
        }
    }

    public void reactivarAgente(Scanner scanner) {
        System.out.print("Ingrese el ID del agente que desea reactivar: ");
        int agenteId = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE Usuarios SET estado = 'activo' WHERE usuario_id = ? AND rol = 'agente'";
        try (Connection connection = baseDeDatos.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, agenteId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Agente reactivado exitosamente.");
            } else {
                System.out.println("No se encontró el agente o el agente ya está activo.");
            }
        } catch (SQLException e) {
            System.out.println("Error al reactivar el agente: " + e.getMessage());
        }
    }

    public void listarAgentes() {
        String sql = "SELECT * FROM Usuarios WHERE rol = 'agente'";
        try (Connection connection = baseDeDatos.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("usuario_id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                String direccion = rs.getString("direccion");
                String estado = rs.getString("estado");

                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Apellido: " + apellido + ", Email: " + email + ", Dirección: " + direccion + ", Estado: " + estado);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los agentes: " + e.getMessage());
        }
    }

    public void eliminarAgente(Scanner scanner) {
        System.out.print("Ingrese el ID del agente que desea eliminar: ");
        int agenteId = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = baseDeDatos.conectar()) {
            // Eliminar citas asociadas al agente
            String sqlEliminarCitas = "DELETE FROM Citas WHERE agente_id = ?";
            try (PreparedStatement pstmtEliminarCitas = connection.prepareStatement(sqlEliminarCitas)) {
                pstmtEliminarCitas.setInt(1, agenteId);
                pstmtEliminarCitas.executeUpdate();
            }

            // Eliminar propiedades asociadas al agente
            String sqlEliminarPropiedades = "DELETE FROM Propiedades WHERE agente_id = ?";
            try (PreparedStatement pstmtEliminarPropiedades = connection.prepareStatement(sqlEliminarPropiedades)) {
                pstmtEliminarPropiedades.setInt(1, agenteId);
                pstmtEliminarPropiedades.executeUpdate();
            }

            // Eliminar el agente
            String sqlEliminarAgente = "DELETE FROM Usuarios WHERE usuario_id = ? AND rol = 'agente'";
            try (PreparedStatement pstmtEliminarAgente = connection.prepareStatement(sqlEliminarAgente)) {
                pstmtEliminarAgente.setInt(1, agenteId);
                int rowsAffected = pstmtEliminarAgente.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Agente eliminado exitosamente.");
                } else {
                    System.out.println("No se encontró el agente.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el agente: " + e.getMessage());
        }
    }

    public void listarPropiedades() {
        String sql = "SELECT * FROM Propiedades";
        try (Connection connection = baseDeDatos.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("propiedad_id");
                String ubicacion = rs.getString("ubicacion");
                float precio = rs.getFloat("precio");
                float tamano = rs.getFloat("tamano");
                String estado = rs.getString("estado");

                System.out.println("ID: " + id + ", Ubicación: " + ubicacion + ", Precio: " + precio + ", Tamaño: " + tamano + ", Estado: " + estado);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar las propiedades: " + e.getMessage());
        }
    }

    public void eliminarPropiedad(Scanner scanner) {
        System.out.print("Ingrese el ID de la propiedad que desea eliminar: ");
        int propiedadId = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = baseDeDatos.conectar()) {
            // Eliminar citas asociadas a la propiedad
            String sqlEliminarCitas = "DELETE FROM Citas WHERE propiedad_id = ?";
            try (PreparedStatement pstmtEliminarCitas = connection.prepareStatement(sqlEliminarCitas)) {
                pstmtEliminarCitas.setInt(1, propiedadId);
                pstmtEliminarCitas.executeUpdate();
            }

            // Eliminar la propiedad
            String sqlEliminarPropiedad = "DELETE FROM Propiedades WHERE propiedad_id = ?";
            try (PreparedStatement pstmtEliminarPropiedad = connection.prepareStatement(sqlEliminarPropiedad)) {
                pstmtEliminarPropiedad.setInt(1, propiedadId);
                pstmtEliminarPropiedad.executeUpdate();
                System.out.println("Propiedad eliminada exitosamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la propiedad: " + e.getMessage());
        }
    }
    public void obtenerDetalleAgente(Scanner scanner) {
        System.out.print("Ingrese el ID del agente del que desea obtener detalles: ");
        int agenteId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT * FROM Usuarios WHERE usuario_id = ? AND rol = 'agente'";
        try (Connection connection = baseDeDatos.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, agenteId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("usuario_id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                String direccion = rs.getString("direccion");
                String estado = rs.getString("estado");

                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Apellido: " + apellido + ", Email: " + email + ", Dirección: " + direccion + ", Estado: " + estado);
            } else {
                System.out.println("No se encontró el agente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener detalles del agente: " + e.getMessage());
        }
    }


    public void obtenerDetallePropiedad(Scanner scanner) {
        System.out.print("Ingrese el ID de la propiedad de la que desea obtener detalles: ");
        int propiedadId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT * FROM Propiedades WHERE propiedad_id = ?";
        try (Connection connection = baseDeDatos.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, propiedadId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("propiedad_id");
                String ubicacion = rs.getString("ubicacion");
                float precio = rs.getFloat("precio");
                float tamano = rs.getFloat("tamano");
                String estado = rs.getString("estado");

                System.out.println("ID: " + id + ", Ubicación: " + ubicacion + ", Precio: " + precio + ", Tamaño: " + tamano + ", Estado: " + estado);
            } else {
                System.out.println("La propiedad no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener detalles de la propiedad: " + e.getMessage());
        }
    }
}

