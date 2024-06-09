import java.sql.Date;
import java.sql.Time;

public class Cita {
    private int id;
    private Date fecha;
    private Time hora;
    private int propiedad;
    private int agente;
    private int cliente;

    public Cita(int id, Date fecha, Time hora, int propiedad, int agente, int cliente) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.propiedad = propiedad;
        this.agente = agente;
        this.cliente = cliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public Time getHora() {
        return hora;
    }

    public int getPropiedad() {
        return propiedad;
    }

    public int getAgente() {
        return agente;
    }

    public int getCliente() {
        return cliente;
    }

}

