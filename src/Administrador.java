public class Administrador extends Usuario {
    public Administrador(String nombre, String apellido, String email, String direccion, String contrasena, int id,BaseDeDatos baseDeDatos) {
        super(nombre, apellido, email, direccion, contrasena, id, "administrador", baseDeDatos);
    }

    public void registrarAgente() {
        System.out.println("RegistrarAgente");
    }

    public void suspenderAgente() {
        System.out.println("suspenderAgente");
    }

    public void reactivarAgente() {
        System.out.println("reactivarAgente");
    }

    public void listarAgentes() {
        System.out.println("listarAgentes");
    }

    public void obtenerDetalleAgente() {
        System.out.println("obtenerDetalleAgente");
    }

    public void seleccionarPropiedad() {
        System.out.println("seleccionarPropiedad");
    }

    public void eliminarAgente() {
        System.out.println("eliminarAgente");
    }

    public void eliminarPropiedad() {
        System.out.println("eliminarPropiedad");
    }

    public void listarPropiedades() {
        System.out.println("listarPropiedades");
    }
}