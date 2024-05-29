package com.challengealura.literalura.model;

public enum Lenguaje {
    ES("es"),
    EN("en"),
    PT("pt");

    private String idioma;

    Lenguaje(String idioma) {
        this.idioma = idioma;
    }

    public static Lenguaje fromString(String text){
        for (Lenguaje lenguaje : Lenguaje.values()){
            if(lenguaje.idioma.equalsIgnoreCase(text)){
                return lenguaje;
            }
        }
        throw new IllegalArgumentException("Ningun lenguaje encontrado: " + text);
    }

    public String getIdioma(){
        return this.idioma;
    }
}
