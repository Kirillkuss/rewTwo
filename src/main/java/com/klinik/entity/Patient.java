package com.klinik.entity;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table( name = "patient")
@Setter
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Patient implements Serializable {

         
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column( name = "id_patient")
    @Schema( name        = "id_patient",
            description = "ИД пациента",
            example     = "100",
            required    = true )
    @JsonInclude(Include.NON_NULL) 
    private Long idPatient;

    @Column( name = "surname")
    @Schema( name        = "surname",
            description = "Фамилия",
            example     = "Пупкин",
            required    = true )
    private String surname;

    @Column( name = "name")
    @Schema( name        = "name",
            description = "Имя",
            example     = "Михаил",
            required    = true )
    private String name;

    @Column( name = "full_name")
    @Schema( name        = "fullName",
            description = "Отчество",
            example     = "Петрович",
            required    = true )
    private String fullName;

    @Column( name = "gender")
    @Schema( name        = "gender",
            description = "Пол пациента" )
    private Gender gender;

    @Column( name = "phone")
    @Schema( name        = "phone",
            description = "Номер телефона",
            example     = "+78998956184",
            required    = true )
    private String phone;

    @Column( name = "address")
    @Schema( name        = "address",
            description = "Адрес",
            example     = "Спб, Проспект Тихорецкого д.5",
            required    = true )
    private String address;

    @Hidden
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id", referencedColumnName = "id_document")
    @JsonInclude(Include.NON_NULL)
    private Document document;
        
    public Patient(Long idPatient,  String surname, String name, String fullName, Gender gender ,String phone, String address, Document document ){
        this.idPatient = idPatient;
        this.surname = surname;
        this.name = name;
        this.fullName = fullName;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.document = document;
    }

    public Patient(  String surname, String name, String fullName, Gender gender ,String phone, String address ){
        this.surname = surname;
        this.name = name;
        this.fullName = fullName;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }

}
