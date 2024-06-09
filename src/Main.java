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
                    iniciarSesion(scanner, baseDeDatos);
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
        Usuario.registrarUsuario(nuevoCliente);

        System.out.println("Usuario registrado exitosamente.");
    }

    private static void iniciarSesion(Scanner scanner, BaseDeDatos baseDeDatos) {
        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();

        Usuario usuario = Usuario.obtenerUsuarioPorEmail(baseDeDatos, email);

        if (usuario != null) {
            if (usuario.iniciarSesion(email, contrasena)) {
                System.out.println("Sesión iniciada exitosamente.");
                mostrarOpcionesPorRol(usuario, scanner);
            } else {
                System.out.println("Credenciales incorrectas. Por favor intente de nuevo.");
            }
        } else {
            System.out.println("El usuario está suspendido y no puede iniciar sesión.");
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

    private static void mostrarOpcionesAdministrador(Administrador administrador, Scanner scanner) {
        boolean exit = false;

        while (!exit) {
            System.out.println("Opciones de administrador:");
            System.out.println("1. Registrar agente");
            System.out.println("2. Suspender agente");
            System.out.println("3. Reactivar agente");
            System.out.println("4. Listar agentes");
            System.out.println("5. Obtener detalle de agente");
            System.out.println("6. Seleccionar propiedad");
            System.out.println("7. Eliminar agente");
            System.out.println("8. Eliminar propiedad");
            System.out.println("9. Listar propiedades");
            System.out.println("10. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    administrador.registrarAgente(scanner);
                    break;
                case 2:
                    administrador.suspenderAgente(scanner);
                    break;
                case 3:
                    administrador.reactivarAgente(scanner);
                    break;
                case 4:
                    administrador.listarAgentes();
                    break;
                case 5:
                    administrador.obtenerDetalleAgente(scanner);
                    break;
                case 6:
                    administrador.obtenerDetallePropiedad(scanner);
                    break;
                case 7:
                    administrador.eliminarAgente(scanner);
                    break;
                case 8:
                    administrador.eliminarPropiedad(scanner);
                    break;
                case 9:
                    administrador.listarPropiedades();
                    break;
                case 10:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor intente de nuevo.");
            }
        }
    }


    private static void mostrarOpcionesAgente(Agente agente, Scanner scanner) {
        boolean exit = false;

        while (!exit) {
            System.out.println("Opciones de agente:");
            System.out.println("1. Crear propiedad");
            System.out.println("2. Editar propiedad");
            System.out.println("3. Eliminar propiedad");
            System.out.println("4. Listar propiedades");
            System.out.println("5. Obtener detalle de propiedad");
            System.out.println("6. Listar citas");
            System.out.println("7. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    agente.crearPropiedad(scanner);
                    break;
                case 2:
                    agente.editarPropiedad(scanner);
                    break;
                case 3:
                    agente.eliminarPropiedad(scanner);
                    break;
                case 4:
                    agente.listarPropiedades();
                    break;
                case 5:
                    agente.obtenerDetallePropiedad(scanner);
                    break;
                case 6:
                    agente.listarCitas(scanner);
                    break;
                case 7:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor intente de nuevo.");
            }
        }
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
            scanner.nextLine();

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
