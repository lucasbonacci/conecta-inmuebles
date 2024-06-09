public class Propiedad {

    private int id;
    private String ubicacion;
    private float precio;
    private float tamano;
    private String estado;

    public Propiedad(int id, String ubicacion, float precio, float tamano, String estado) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.precio = precio;
        this.tamano = tamano;
        this.estado = estado;
    }


    public String getUbicacion() {
        return ubicacion;
    }
    public float getPrecio() {
        return precio;
    }

    public float getTamano() {
        return tamano;
    }
    public String getEstado() {
        return estado;
    }
}

