package com.challengealura.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String nombre;
    private Integer fecha_nacimiento;
    private Integer fecha_muerte;

    @ManyToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;


    public Autor(){}

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fecha_nacimiento = datosAutor.fecha_nacimiento();
        this.fecha_muerte = datosAutor.fecha_muerte();
    }

    @Override
    public String toString() {
        return "nombre='" + nombre +
                ", fecha_nacimiento=" + fecha_nacimiento +
                ", fecha_muerte=" + fecha_muerte;
    }
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Integer fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public Integer getFecha_muerte() {
        return fecha_muerte;
    }

    public void setFecha_muerte(Integer fecha_muerte) {
        this.fecha_muerte = fecha_muerte;
    }

    public List<Libro> getLibros() {
        return libros;
    }

     public void setLibros(List<Libro> libros) {
        libros.forEach(l -> l.setAutor(this));
        this.libros = libros;
    }
}
