package com.challengealura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(

    @JsonAlias("name") String nombre,

    @JsonAlias("birth_year") Integer fecha_nacimiento,

    @JsonAlias("death_year") Integer fecha_muerte) {
}
