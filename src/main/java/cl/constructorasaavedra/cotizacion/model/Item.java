package cl.constructorasaavedra.cotizacion.model;

import javax.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private Integer monto;
    private Integer cantidad;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trabajo_id")
    private Trabajo trabajo;

    public Item(Integer id, String nombre, Integer monto) {
        this.id = id;
        this.nombre = nombre;
        this.monto = monto;
    }

    public Item(Integer id, String nombre, Integer monto, Integer cantidad, Trabajo trabajo) {
        this.id = id;
        this.nombre = nombre;
        this.monto = monto;
        this.cantidad = cantidad;
        this.trabajo = trabajo;
    }



    public Item(String nombre, Integer monto, Trabajo trabajo) {
        this.nombre = nombre;
        this.monto = monto;
        this.trabajo = trabajo;
    }

    public Item() {
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public Trabajo getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(Trabajo trabajo) {
        this.trabajo = trabajo;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", monto=" + monto +
                ", cantidad=" + cantidad +
                ", trabajo=" + trabajo +
                '}';
    }
}
