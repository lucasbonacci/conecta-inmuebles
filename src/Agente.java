import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Time;

public class Agente extends Usuario {
    public Agente(String nombre, String apellido, String email, String direccion, String contrasena, int id, BaseDeDatos baseDeDatos) {
        super(nombre, apellido, email, direccion, contrasena, id, "agente", baseDeDatos);
    }

    public void crearPropiedad(Scanner scanner) {
        System.out.print("Ingrese la ubicación de la propiedad: ");
        String ubicacion = scanner.nextLine();
        System.out.print("Ingrese el precio de la propiedad: ");
        float precio = scanner.nextFloat();
        System.out.print("Ingrese el tamaño de la propiedad: ");
        float tamano = scanner.nextFloat();
        scanner.nextLine();
        System.out.print("Ingrese el estado de la propiedad (disponible, vendida, alquilada): ");
        String estado = scanner.nextLine();

        Propiedad nuevaPropiedad = new Propiedad(0, ubicacion, precio, tamano, estado);

        String sql = "INSERT INTO Propiedades (ubicacion, precio, tamano, estado, agente_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = baseDeDatos.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nuevaPropiedad.getUbicacion());
            pstmt.setFloat(2, nuevaPropiedad.getPrecio());
            pstmt.setFloat(3, nuevaPropiedad.getTamano());
            pstmt.setString(4, nuevaPropiedad.getEstado());
            pstmt.setInt(5, this.id);
            pstmt.executeUpdate();
            System.out.println("Propiedad creada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al crear la propiedad: " + e.getMessage());
        }
    }


    public void editarPropiedad(Scanner scanner) {
        System.out.print("Ingrese el ID de la propiedad que desea editar: ");
        int propiedadId = scanner.nextInt();
        scanner.nextLine();

        String sqlVerificar = "SELECT * FROM Propiedades WHERE propiedad_id = ? AND agente_id = ?";
        try (Connection connection = baseDeDatos.conectar();
             PreparedStatement pstmtVerificar = connection.prepareStatement(sqlVerificar)) {
            pstmtVerificar.setInt(1, propiedadId);
            pstmtVerificar.setInt(2, this.id);
            ResultSet rs = pstmtVerificar.executeQuery();

            if (rs.next()) {
                System.out.print("Ingrese la nueva ubicación de la propiedad: ");
                String nuevaUbicacion = scanner.nextLine();
                System.out.print("Ingrese el nuevo precio de la propiedad: ");
                float nuevoPrecio = scanner.nextFloat();
                System.out.print("Ingrese el nuevo tamaño de la propiedad: ");
                float nuevoTamano = scanner.nextFloat();
                scanner.nextLine();
                System.out.print("Ingrese el nuevo estado de la propiedad (disponible, vendida, alquilada): ");
                String nuevoEstado = scanner.nextLine();

                String sqlActualizar = "UPDATE Propiedades SET ubicacion = ?, precio = ?, tamano = ?, estado = ? WHERE propiedad_id = ? AND agente_id = ?";
                try (PreparedStatement pstmtActualizar = connection.prepareStatement(sqlActualizar)) {
                    pstmtActualizar.setString(1, nuevaUbicacion);
                    pstmtActualizar.setFloat(2, nuevoPrecio);
                    pstmtActualizar.setFloat(3, nuevoTamano);
                    pstmtActualizar.setString(4, nuevoEstado);
                    pstmtActualizar.setInt(5, propiedadId);
                    pstmtActualizar.setInt(6, this.id);
                    pstmtActualizar.executeUpdate();
                    System.out.println("Propiedad actualizada exitosamente.");
                }
            } else {
                System.out.println("No tiene permiso para editar esta propiedad o la propiedad no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al editar la propiedad: " + e.getMessage());
        }
    }


    public void eliminarPropiedad(Scanner scanner) {
        System.out.print("Ingrese el ID de la propiedad que desea eliminar: ");
        int propiedadId = scanner.nextInt();
        scanner.nextLine();

        String sqlVerificar = "SELECT * FROM Propiedades WHERE propiedad_id = ? AND agente_id = ?";
        try (Connection connection = baseDeDatos.conectar();
             PreparedStatement pstmtVerificar = connection.prepareStatement(sqlVerificar)) {
            pstmtVerificar.setInt(1, propiedadId);
            pstmtVerificar.setInt(2, this.id);
            ResultSet rs = pstmtVerificar.executeQuery();

            if (rs.next()) {
                // Eliminar citas asociadas a la propiedad
                String sqlEliminarCitas = "DELETE FROM Citas WHERE propiedad_id = ?";
                try (PreparedStatement pstmtEliminarCitas = connection.prepareStatement(sqlEliminarCitas)) {
                    pstmtEliminarCitas.setInt(1, propiedadId);
                    pstmtEliminarCitas.executeUpdate();
                }

                // Eliminar la propiedad
                String sqlEliminar = "DELETE FROM Propiedades WHERE propiedad_id = ? AND agente_id = ?";
                try (PreparedStatement pstmtEliminar = connection.prepareStatement(sqlEliminar)) {
                    pstmtEliminar.setInt(1, propiedadId);
                    pstmtEliminar.setInt(2, this.id);
                    pstmtEliminar.executeUpdate();
                    System.out.println("Propiedad eliminada exitosamente.");
                }
            } else {
                System.out.println("No tiene permiso para eliminar esta propiedad o la propiedad no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la propiedad: " + e.getMessage());
        }
    }

    public void listarPropiedades() {
        try (Connection connection = baseDeDatos.conectar()) {
            String sql = "SELECT * FROM Propiedades WHERE agente_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, this.id);
            ResultSet rs = pstmt.executeQuery();

            boolean tienePropiedades = false;

            while (rs.next()) {
                tienePropiedades = true;
                int id = rs.getInt("propiedad_id");
                String ubicacion = rs.getString("ubicacion");
                float precio = rs.getFloat("precio");
                float tamano = rs.getFloat("tamano");
                String estado = rs.getString("estado");

                System.out.println("ID: " + id + ", Ubicación: " + ubicacion + ", Precio: " + precio + ", Tamaño: " + tamano + ", Estado: " + estado);
            }

            if (!tienePropiedades) {
                System.out.println("No tiene propiedades registradas.");
            }
        } catch (SQLException e) {
            System.out.println("Error al listar las propiedades: " + e.getMessage());
        }
    }

    public void obtenerDetallePropiedad(Scanner scanner) {
        System.out.print("Ingrese el ID de la propiedad de la que desea obtener detalles: ");
        int propiedadId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT * FROM Propiedades WHERE propiedad_id = ? AND agente_id = ?";
        try (Connection connection = baseDeDatos.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, propiedadId);
            pstmt.setInt(2, this.id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("propiedad_id");
                String ubicacion = rs.getString("ubicacion");
                float precio = rs.getFloat("precio");
                float tamano = rs.getFloat("tamano");
                String estado = rs.getString("estado");

                System.out.println("ID: " + id + ", Ubicación: " + ubicacion + ", Precio: " + precio + ", Tamaño: " + tamano + ", Estado: " + estado);
            } else {
                System.out.println("No tiene permiso para ver esta propiedad o la propiedad no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener detalles de la propiedad: " + e.getMessage());
        }
    }

    public void listarCitas(Scanner scanner) {
        String sql = "SELECT * FROM Citas WHERE agente_id = ?";
        try (Connection connection = baseDeDatos.conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, this.id);
            ResultSet rs = pstmt.executeQuery();

            boolean tieneCitas = false;

            while (rs.next()) {
                tieneCitas = true;
                int id = rs.getInt("cita_id");
                Date fecha = rs.getDate("fecha");
                Time hora = rs.getTime("hora");
                int propiedadId = rs.getInt("propiedad_id");
                int clienteId = rs.getInt("cliente_id");

                System.out.println("ID de la Cita: " + id);
                System.out.println("Fecha: " + fecha);
                System.out.println("Hora: " + hora);
                System.out.println("ID de la Propiedad: " + propiedadId);
                System.out.println("ID del Cliente: " + clienteId);
                System.out.println("---------------------------");
            }

            if (!tieneCitas) {
                System.out.println("No tiene citas agendadas.");
            }
        } catch (SQLException e) {
            System.out.println("Error al listar las citas: " + e.getMessage());
        }
    }
}
