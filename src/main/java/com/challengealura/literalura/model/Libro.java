package com.challengealura.literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Lenguaje lenguaje;

    private Integer descarga;
    @ManyToOne
    private Autor autor;

    public Libro() {}

    public Libro(DatosLibro libro) {
        this.titulo = libro.titulo();
        this.lenguaje = Lenguaje.fromString(libro.lenguajes().stream()
                .limit(1).collect(Collectors.joining()));
        this.descarga = libro.descarga();
    }

    @Override
    public String toString() {
        return "titulo='" + titulo +
                ", idioma=" + lenguaje +
                ", autores=" + autor;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Lenguaje getIdioma() {
        return lenguaje;
    }

    public void setIdioma(Lenguaje idioma) {
        this.lenguaje = idioma;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Lenguaje getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(Lenguaje lenguaje) {
        this.lenguaje = lenguaje;
    }

    public Integer getDescarga() {
        return descarga;
    }

    public void setDescarga(Integer descarga) {
        this.descarga = descarga;
    }
}
