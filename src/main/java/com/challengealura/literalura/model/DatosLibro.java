package com.challengealura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("languages") List<String> lenguajes,
        @JsonAlias("authors") List<DatosAutor> autores,
        @JsonAlias("results") List<DatosLibro> libros,
        @JsonAlias("download_count") Integer descarga){
}
