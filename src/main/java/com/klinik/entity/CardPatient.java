package com.klinik.entity;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table( name = "card_patient")
@Setter
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class CardPatient  implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column( name = "id_card_patient")
    @Schema( name        = "idCardPatient",
            description = "ИД карты пациента",
            example     = "100",
            required    = true )
    @JsonInclude(Include.NON_NULL)
    private Long idCardPatient;

    @Column( name = "diagnosis")
    @Schema( name        = "diagnosis",
            description = "Диагноз пациента",
            example     = "Рассеянный склероз",
            required    = true )
    private String diagnosis;

    @Column( name = "allergy")
    @Schema( name        = "allergy",
            description = "Аллергия на препараты",
            example     = "true",
            required    = true )
    private Boolean allergy;

    @Column( name = "note")
    @Schema( name        = "note",
            description = "Примечание",
            example     = "Есть аллергия на цитрамон" )
    private String note;

    @Column( name = "сonclusion")
    @Schema( name        = "сonclusion",
            description = "Заключение",
            example     = "Болен")
    private String сonclusion;

    @Hidden
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name                = "Card_patient_Complaint",
            joinColumns         = @JoinColumn(name = "card_patient_id", referencedColumnName = "id_card_patient"),
            inverseJoinColumns  = @JoinColumn(name = "type_complaint_id", referencedColumnName = "id_type_complaint")
    )
    @JsonInclude(Include.NON_NULL)
    private List<TypeComplaint> typeComplaint = new ArrayList<>();

    @Hidden
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pacient_id", referencedColumnName = "id_patient")
    @JsonInclude(Include.NON_NULL)
    private Patient patient;


    public CardPatient(Long idCardPatient, String diagnosis, Boolean allergy,  String note, String сonclusion , List<TypeComplaint> typeComplaint, Patient patient){
        this.idCardPatient = idCardPatient;
        this.diagnosis = diagnosis;
        this.allergy = allergy;
        this.note = note;
        this.сonclusion = сonclusion;
        this.typeComplaint = typeComplaint;
        this.patient = patient;
    }

    public CardPatient(String diagnosis, Boolean allergy,  String note, String сonclusion , List<TypeComplaint> typeComplaint, Patient patient){
        this.diagnosis = diagnosis;
        this.allergy = allergy;
        this.note = note;
        this.сonclusion = сonclusion;
        this.typeComplaint = typeComplaint;
        this.patient = patient;
    }

    public CardPatient(String diagnosis, Boolean allergy,  String note, String сonclusion , Patient patient){
        this.diagnosis = diagnosis;
        this.allergy = allergy;
        this.note = note;
        this.сonclusion = сonclusion;
        this.patient = patient;
    }

}
