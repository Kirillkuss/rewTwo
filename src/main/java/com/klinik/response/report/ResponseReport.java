package com.klinik.response.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
public class ResponseReport {

    private String name_rehabilitation_treatment;
    private Long   count_treatment;
    private Long   count_patient;
}
