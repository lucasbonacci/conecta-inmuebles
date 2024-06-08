public class Agente extends Usuario {
    public Agente(String nombre, String apellido, String email, String direccion, String contrasena, int id, BaseDeDatos baseDeDatos) {
        super(nombre, apellido, email, direccion, contrasena, id, "agente", baseDeDatos);
    }

    public void crearPropiedad() {
        System.out.println("obtenerDetalleAgente");
    }

    public void editarPropiedad() {
        System.out.println("editarPropiedad");
    }

    public void eliminarPropiedad() {
        System.out.println("eliminarPropiedad");
    }

    public void listarPropiedades() {
        System.out.println("listarPropiedades");
    }

    public void obtenerDetallePropiedad() {
        System.out.println("obtenerDetallePropiedad");
    }

    public void listarCitas() {
        System.out.println("listarCitas");
    }

    public void seleccionarPropiedad() {
        System.out.println("seleccionarPropiedad");
    }
}
