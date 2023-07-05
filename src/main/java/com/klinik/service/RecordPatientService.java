package com.klinik.service;

import com.klinik.entity.CardPatient;
import com.klinik.entity.Doctor;
import com.klinik.entity.RecordPatient;
import com.klinik.excep.MyException;
import com.klinik.repositories.CardPatientRepository;
import com.klinik.repositories.DoctorRerository;
import com.klinik.repositories.RecordPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordPatientService {

    private final RecordPatientRepository recordPatientRepository;
    private final DoctorRerository        doctorRerository;
    private final CardPatientRepository   cardPatientRepository;
    public List<RecordPatient> allListRecordPatient() {
        return recordPatientRepository.findAll();
    }
    public RecordPatient saveRecordPatient( RecordPatient record_patient, Long doctor_id, Long card_patient_id ) throws Exception{
        Optional<Doctor> doctor = doctorRerository.findById( doctor_id );
        if ( doctor.isEmpty() == true ) throw new MyException( 400, "Нет доктора с таким идентификатором");
        Optional<CardPatient> cardPatient = cardPatientRepository.findById( card_patient_id );
        if ( cardPatient.isEmpty() == true ) throw new MyException( 400, "Нет карты пациента с таким идентификатором");
        if ( recordPatientRepository.findById( record_patient.getIdRecord()).isPresent() == true) throw new MyException( 409, "Запись к врачу с таким ИД уже существует, установите другой ИД записи к врачу");
        record_patient.setDoctor( doctor.get() );;
        record_patient.setCardPatientId( cardPatient.get().getIdCardPatient() );
        return recordPatientRepository.save( record_patient );
    }
    public List<RecordPatient> findByParam( Long id, LocalDateTime dateFrom, LocalDateTime dateTo ) throws Exception{
     return recordPatientRepository.findByParamTwo(id, dateFrom, dateTo);
    }
}
