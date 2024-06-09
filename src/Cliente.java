import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Scanner;


public class Cliente extends Usuario {
    public Cliente(String nombre, String apellido, String email, String direccion, String contrasena, int id,BaseDeDatos baseDeDatos) {
        super(nombre, apellido, email, direccion, contrasena, id, "cliente", baseDeDatos);
    }

    public void listarPropiedades() {
        try (Connection connection = getBaseDeDatos().conectar()) {
            String sql = "SELECT * FROM Propiedades WHERE estado = 'disponible'";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

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

    public void agendarCita(Scanner scanner) {
        System.out.print("Ingrese el ID de la propiedad que desea visitar: ");
        int propiedadId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese la fecha de la cita (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();
        System.out.print("Ingrese la hora de la cita (HH:MM:SS): ");
        String hora = scanner.nextLine();

        Date fechaCita = Date.valueOf(fecha);
        Time horaCita = Time.valueOf(hora);

        try (Connection connection = baseDeDatos.conectar()) {
            int agenteId = obtenerAgentePorPropiedad(connection, propiedadId);

            Cita nuevaCita = new Cita(0, fechaCita, horaCita, propiedadId, agenteId, this.id);

            String sql = "INSERT INTO Citas (fecha, hora, propiedad_id, agente_id, cliente_id) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setDate(1, nuevaCita.getFecha());
                pstmt.setTime(2, nuevaCita.getHora());
                pstmt.setInt(3, nuevaCita.getPropiedad());
                pstmt.setInt(4, nuevaCita.getAgente());
                pstmt.setInt(5, nuevaCita.getCliente());
                pstmt.executeUpdate();
                System.out.println("Cita agendada exitosamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al agendar la cita: " + e.getMessage());
        }
    }


    private int obtenerAgentePorPropiedad(Connection connection, int propiedadId) throws SQLException {
        String sql = "SELECT agente_id FROM Propiedades WHERE propiedad_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, propiedadId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("agente_id");
            }
        }
        return -1;
    }

    public void listarCitas() {
            try (Connection connection = baseDeDatos.conectar()) {
                String sql = "SELECT * FROM Citas WHERE cliente_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, this.id);
                ResultSet rs = pstmt.executeQuery();
                boolean tieneCitas = false;

                while (rs.next()) {
                    tieneCitas = true;
                    int id = rs.getInt("cita_id");
                    Date fecha = rs.getDate("fecha");
                    Time hora = rs.getTime("hora");
                    int propiedadId = rs.getInt("propiedad_id");
                    int agenteId = rs.getInt("agente_id");

                    System.out.println("ID de la Cita: " + id);
                    System.out.println("Fecha: " + fecha);
                    System.out.println("Hora: " + hora);
                    System.out.println("ID de la Propiedad: " + propiedadId);
                    System.out.println("ID del Agente: " + agenteId);
                    System.out.println("---------------------------");
                }
                if (!tieneCitas) {
                    System.out.println("No hay citas agendadas.");
                }
            } catch (SQLException e) {
            System.out.println("Error al obtener las citas agendadas: " + e.getMessage());
        }
    }

    public void editarCita(Scanner scanner) {
        System.out.print("Ingrese el ID de la cita que desea editar: ");
        int citaId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Ingrese la nueva fecha de la cita (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();
        System.out.print("Ingrese la nueva hora de la cita (HH:MM:SS): ");
        String hora = scanner.nextLine();

        try (Connection connection = baseDeDatos.conectar()) {
            String sql = "UPDATE Citas SET fecha = ?, hora = ? WHERE cita_id = ? AND cliente_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(fecha));
            pstmt.setTime(2, Time.valueOf(hora));
            pstmt.setInt(3, citaId);
            pstmt.setInt(4, this.id);
            pstmt.executeUpdate();
            System.out.println("Cita editada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al editar la cita: " + e.getMessage());
        }
    }

    public void cancelarCita(Scanner scanner) {
        System.out.print("Ingrese el ID de la cita que desea cancelar: ");
        int citaId = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = baseDeDatos.conectar()) {
            String sql = "DELETE FROM Citas WHERE cita_id = ? AND cliente_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, citaId);
            pstmt.setInt(2, this.id);
            pstmt.executeUpdate();
            System.out.println("Cita cancelada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al cancelar la cita: " + e.getMessage());
        }
    }
}
