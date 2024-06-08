import java.util.Scanner;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static BaseDeDatos baseDeDatos = new BaseDeDatos();

    public static void main(String[] args) {
        // Prueba de conexión a la base de datos
        verificarConexionBaseDeDatos();

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Bienvenido a Conecta Inmuebles");
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar Sesión");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    registrarUsuario(scanner);
                    break;
                case 2:
                    iniciarSesion(scanner);
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor intente de nuevo.");
            }
        }

        System.out.println("Gracias por usar Conecta Inmuebles. ¡Hasta luego!");
        scanner.close();
    }

    private static void registrarUsuario(Scanner scanner) {
        System.out.print("Ingrese su nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese su apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su dirección: ");
        String direccion = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();

        Cliente nuevoCliente = new Cliente(nombre, apellido, email, direccion, contrasena, 0,baseDeDatos);
        baseDeDatos.registrarUsuario(nuevoCliente);

        System.out.println("Usuario registrado exitosamente.");
    }

    private static void iniciarSesion(Scanner scanner) {
        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();

        Usuario usuario = baseDeDatos.obtenerUsuarioPorEmail(email);

        if (usuario != null && usuario.iniciarSesion(email, contrasena)) {
            System.out.println("Sesión iniciada exitosamente.");
            mostrarOpcionesPorRol(usuario, scanner);
        } else {
            System.out.println("Credenciales incorrectas. Por favor intente de nuevo.");
        }
    }

    private static void mostrarOpcionesPorRol(Usuario usuario, Scanner scanner) {
        switch (usuario.getRol()) {
            case "administrador":
                mostrarOpcionesAdministrador((Administrador) usuario, scanner);
                break;
            case "agente":
                mostrarOpcionesAgente((Agente) usuario, scanner);
                break;
            case "cliente":
                mostrarOpcionesCliente((Cliente) usuario, scanner);
                break;
            default:
                System.out.println("Rol desconocido.");
        }
    }

    private static void mostrarOpcionesAdministrador(Administrador administrador,Scanner scanner) {
        System.out.println("Opciones de administrador:");
    }

    private static void mostrarOpcionesAgente(Agente agente,Scanner scanner) {
        System.out.println("Opciones de agente:");
    }

    private static void mostrarOpcionesCliente(Cliente cliente, Scanner scanner) {
        boolean exit = false;

        while (!exit) {
            System.out.println("Opciones de cliente:");
            System.out.println("1. Ver propiedades disponibles");
            System.out.println("2. Agendar una cita");
            System.out.println("3. Ver citas agendadas");
            System.out.println("4. Editar cita");
            System.out.println("5. Cancelar cita");
            System.out.println("6. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();  // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    cliente.listarPropiedades();
                    break;
                case 2:
                    cliente.agendarCita(scanner);
                    break;
                case 3:
                    cliente.listarCitas();
                    break;
                case 4:
                    cliente.editarCita(scanner);
                    break;
                case 5:
                    cliente.cancelarCita(scanner);
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor intente de nuevo.");
            }
        }
    }


    private static void verificarConexionBaseDeDatos() {
        try {
            Connection connection = baseDeDatos.conectar();
            if (connection != null) {
                System.out.println("Conexión exitosa a la base de datos");
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }
}
