package com.example.gestion.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/** Modelo que representa a la entidad Usuario
 * @author RaulJRG
 * @version 1.0
 */
@Entity
@Data
public class Usuario {
    /** Identificador principal de la entidad*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** Imagen del usuario anotada para ser un long object*/
    @Lob
    @Column
    private String foto;
    /** Nombre del usuarios*/
    @Column
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    /** Emails del usuario, diseñado para contener emails separados por comas
     *  esto para evitar hacer una segunda entidad de un solo atributo*/
    @Column
    @NotBlank(message = "El email no puede estar vacío")
    private String emails;
    /** Genero del usuario*/
    @Column
    private String genero;
    /** Status del usuario, puede ser "Activo" o "Inactivo"*/
    @Column
    @NotBlank(message = "El status no puede estar vacío")
    private String status;
}
