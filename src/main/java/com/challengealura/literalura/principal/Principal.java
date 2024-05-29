package com.challengealura.literalura.principal;

import com.challengealura.literalura.model.Autor;
import com.challengealura.literalura.model.DatosLibro;
import com.challengealura.literalura.model.Lenguaje;
import com.challengealura.literalura.model.Libro;
import com.challengealura.literalura.repository.LibroRepository;
import com.challengealura.literalura.service.ConsumoAPI;
import com.challengealura.literalura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private LibroRepository repository;
    private List<Libro> Libros;
    private Optional<Libro> libroBuscado;

    public Principal(LibroRepository repository) {
        this.repository = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
            var menu = """
                    ***************************************************
                          -- Seleccione una opción para continuar --
                    ***************************************************
                    
                        1 - Buscar libro por título
                        2 - Listar libros registrados
                        3 - Listar autores registrados
                        4 - Listar autores vivos en un determinado año
                        5 - Listar libros por idioma        
                                                     
                        0 - Salir
                    ***************************************************
                    """;
            while (opcion != 0) {
            System.out.println(menu);
            try {
                opcion = Integer.valueOf(teclado.nextLine());

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosEnUnAnio();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("¡Gracias por usar LiterAlura!");
                        break;
                    default:
                        System.out.println("¡La opción ingresada no es valida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("La opción ingresada no es valida!: " + e.getMessage());
            }
        }

    }

    private DatosLibro buscarLibroPorTitulo() {
        System.out.println("Escriba el título del libro:");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        //System.out.println(json);
        DatosLibro datos = conversor.obtenerDatos(json, DatosLibro.class);

        Optional<DatosLibro> libroBuscado = datos.libros().stream().findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println(
                    "\n****** LIBRO ******" +
                            "\nTítulo: " + libroBuscado.get().titulo() +
                            "\nAutor: " + libroBuscado.get().autores().stream()
                            .map(a -> a.nombre()).limit(1).collect(Collectors.joining()) +
                            "\nIdioma: " + libroBuscado.get().lenguajes().stream().collect(Collectors.joining()) +
                            "\nCantidad de descargas: " + libroBuscado.get().descarga() +
                            "\n******************\n"
            );

            try {
                List<Libro> libroEncontrado = libroBuscado.stream().map(a -> new Libro(a)).collect(Collectors.toList());
                Autor autorAPI = libroBuscado.stream().
                        flatMap(l -> l.autores().stream()
                                .map(a -> new Autor(a)))
                        .collect(Collectors.toList()).stream().findFirst().get();
                Optional<Autor> autorBD = repository.buscarAutorPorNombre(libroBuscado.get().autores().stream()
                        .map(a -> a.nombre())
                        .collect(Collectors.joining()));
                Optional<Libro> libroOptional = repository.buscarLibroPorNombre(nombreLibro);
                if (libroOptional.isPresent()) {
                    System.out.println("¡El libro ya está registrado!");
                } else {
                    Autor autor;
                    if (autorBD.isPresent()) {
                        autor = autorBD.get();
                        System.out.println("¡El autor ya está registrado!");
                    } else {
                        autor = autorAPI;
                        repository.save(autor);
                    }
                    autor.setLibros(libroEncontrado);
                    repository.save(autor);
                }
            } catch (Exception e) {
                System.out.println("¡Advertencia! " + e.getMessage());
            }
        } else {
            System.out.println("¡No se pudo encontrar el libro!");
        }
        return datos;
    }

    public void listarLibrosRegistrados() {
        List<Libro> libros = repository.buscarTodosLosLibros();
        libros.forEach(l -> System.out.println(
                "****** LIBRO ******" +
                        "\nTítulo: " + l.getTitulo() +
                        "\nAutor: " + l.getAutor().getNombre() +
                        "\nIdioma: " + l.getLenguaje().getIdioma() +
                        "\nCantidad de descargas: " + l.getDescarga() +
                        "\n*******************\n"
        ));
    }

    public void listarAutoresRegistrados() {
        List<Autor> autores = repository.findAll();
        System.out.println();
        autores.forEach(l -> System.out.println(
                        "****** AUTOR ******" +
                        "\nNombre: " + l.getNombre() +
                        "\nFecha de nacimiento: " + l.getFecha_nacimiento() +
                        "\nFecha de muerte: " + l.getFecha_muerte() +
                        "\nLibros: " + l.getLibros().stream()
                        .map(t -> t.getTitulo()).collect(Collectors.toList()) +
                        "\n*******************\n" + "\n"
        ));
    }

    public void listarAutoresVivosEnUnAnio() {
        System.out.println("Ingresa el año en el vivió el autor:");
        try {
            var fecha = Integer.valueOf(teclado.nextLine());
            List<Autor> autores = repository.buscarAutoresVivos(fecha);
            if (!autores.isEmpty()) {
                System.out.println();
                autores.forEach(a -> System.out.println(
                                "****** AUTOR ******" +
                                "\nNombre: " + a.getNombre() +
                                "\nFecha de nacimiento: " + a.getFecha_nacimiento() +
                                "\nFecha de muerte: " + a.getFecha_muerte() +
                                "\nLibros: " + a.getLibros().stream()
                                .map(l -> l.getTitulo()).collect(Collectors.toList()) +
                                "\n*******************\n" + "\n"

                ));
            } else {
                System.out.println("¡No se registran autores vivos en ese año!");
            }
        } catch (NumberFormatException e) {
            System.out.println("¡Introducir un año valido!" + e.getMessage());
        }
    }

    public void listarLibrosPorIdioma() {
        var menu = """
                ***************************************************
                       -- Seleccione el idioma de búsqueda --
                ***************************************************
                
                                es - Español
                                en - Inglés
                                pt - Portugués
                
                ***************************************************
                """;
        System.out.println(menu);
        var idioma = teclado.nextLine();
        if (idioma.equalsIgnoreCase("es") || idioma.equalsIgnoreCase("en") || idioma.equalsIgnoreCase("pt")) {
            Lenguaje lenguaje = Lenguaje.fromString(idioma);
            List<Libro> libros = repository.buscarLibrosPorIdioma(lenguaje);
            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados en ese idioma!");
            } else {
                System.out.println();
                libros.forEach(l -> System.out.println(
                        "****** LIBRO ******" +
                                "\nTítulo: " + l.getTitulo() +
                                "\nAutor: " + l.getAutor().getNombre() +
                                "\nIdioma: " + l.getLenguaje().getIdioma() +
                                "\nCantidad de descargas: " + l.getDescarga() +
                                "\n*******************\n"
                ));
            }
        } else {
            System.out.println("¡Idioma no valido!");
        }
    }
}
