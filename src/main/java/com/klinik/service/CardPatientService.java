package com.klinik.service;

import com.klinik.entity.CardPatient;
import com.klinik.entity.Document;
import com.klinik.entity.Gender;
import com.klinik.entity.Patient;
import com.klinik.entity.TypeComplaint;
import com.klinik.entity.Complaint;
import com.klinik.excep.MyException;
import com.klinik.repositories.CardPatientRepository;
import com.klinik.repositories.PatientRepository;
import com.klinik.repositories.TypeComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

@Service
@RequiredArgsConstructor
public class CardPatientService {

    @PersistenceContext
    EntityManager em;

    private final TypeComplaintRepository typeComplaintRepository;
    private final CardPatientRepository cardPatientRepository;
    private final PatientRepository     patientRepository;

    public CardPatient saveCardPatient( CardPatient card_patient,  Long id_patient ) throws Exception{
        if( cardPatientRepository.findByPatientId( id_patient ).isEmpty() == false)       throw new MyException( 409, "Карта пациента с таким ИД пациента уже существует");
        if( cardPatientRepository.findById( card_patient.getIdCardPatient() ).isEmpty() == false ) throw new MyException ( 409, "Карта с таким ИД уже существует");
        Optional<Patient> patient = patientRepository.findById( id_patient );
        if( patient.isEmpty() == true ) throw new MyException ( 400, "Пациента с таким ИД не существует");
        card_patient.setPatient( patient.get());
        return cardPatientRepository.save( card_patient );
    }

    public CardPatient findByPatientId( Long id ) throws Exception{
        return cardPatientRepository.findByPatientId( id ).orElseThrow();
    }

    public CardPatient findByIdCard( Long id ) throws Exception{
        return cardPatientRepository.findById( id ).orElseThrow();
    }

    @Transactional
    public void addCardPatientComplaint( Long IdCard, Long IdComplaint ) throws Exception{
        if ( cardPatientRepository.findById( IdCard ).isEmpty() == true ) throw new MyException ( 400, "Карта с таким ИД не существует");
        if ( typeComplaintRepository.findById( IdComplaint ).isEmpty() == true ) throw new MyException ( 400, "Под жалобы с таким ИД не существует");
        if ( findByIdCardAndIdComplaint(IdCard, IdComplaint).getIdCardPatient() != null ) throw new MyException ( 409, "Под жалоба с таким ИД уже добавлена в карту пацинета");
        em.createNativeQuery( "INSERT INTO Card_patient_Complaint(card_patient_id, type_complaint_id) VALUES (?,?)")
                .setParameter(1, IdCard)
                .setParameter( 2, IdComplaint)
                .executeUpdate();
    }

    public CardPatient findByIdCardAndIdComplaint( Long idCard, Long IdComplaint ) throws Exception{
        String sql = "SELECT u.id_card_patient FROM Card_patient u "
                    + " left join Card_patient_Complaint cpc on cpc.card_patient_id = u.id_card_patient "
                    + " left join Type_complaint c on c.id_type_complaint = cpc.type_complaint_id "
                    +" WHERE u.id_card_patient = ? and c.id_type_complaint = ? ";
        Session session;
        CardPatient card = new CardPatient();
        try{
            session = em.unwrap( Session.class);
            session.doWork( (Connection conn ) ->{
                try( PreparedStatement st = conn.prepareStatement( sql )){
                    st.setLong(1, idCard);
                    st.setLong( 2, IdComplaint);
                    try( ResultSet set = st.executeQuery()){
                        while( set.next()){
                            card.setIdCardPatient( set.getLong(1));
                        }
                    }
                }
            });

        }catch ( Exception ex ){
        }        
        return card;
    }

    /**
     * Поиск карты пациента по документу пациента ( полис/снилс/номер )
     * @param parametr - параметр поиска
     * @return Card_patient
     * @throws Exception
     */
    public CardPatient findByNPS( String parametr ) throws Exception{
        String sql1 = " SELECT cp.id_card_patient, cp.diagnosis, cp.allergy, cp.note, cp.сonclusion, p.id_patient, p.surname, p.name, p.full_name, p.gender, "
                    + " p.phone, p.address, d.id_document, d.type_document, d.seria, d.numar, d.snils, d.polis  FROM Card_patient cp "
                    + " left join Patient p on p.id_patient = cp.pacient_id "
                    + " left join Document d on d.id_document = p.document_id "
                    + " WHERE d.numar = ? or d.snils = ? or d.polis = ? ";
        String sql2 = " SELECT c.functional_impairment, tc.id_type_complaint, tc.name FROM Card_patient cp "
                    + " left join Patient p on p.id_patient = cp.pacient_id"
                    + " left join Document d on d.id_document = p.document_id"
                    + " left join Card_patient_Complaint cpc on cpc.card_patient_id = cp.id_card_patient "
                    + " left join Type_complaint tc on tc.id_type_complaint = cpc.type_complaint_id "
                    + " left join Complaint c on c.id_complaint = tc.complaint_id"
                    + " WHERE d.numar = ? or d.snils = ? or d.polis = ? ";
        Session session;
        CardPatient card = new CardPatient();
            try{
            session = em.unwrap( Session.class);
            session.doWork( (Connection conn ) ->{
                try( PreparedStatement st = conn.prepareStatement( sql1 )){
                    st.setString(1, parametr);
                    st.setString(2, parametr);
                    st.setString(3, parametr);
                    try( ResultSet set = st.executeQuery()){
                        while( set.next()){
                            card.setIdCardPatient( set.getLong(1));
                            card.setDiagnosis( set.getString(2));
                            card.setAllergy( set.getBoolean(3));
                            card.setNote( set.getString( 4 ));
                            card.setСonclusion( set.getString( 5 ));
                            Patient patient = new Patient();
                            patient.setIdPatient( set.getLong( 6 ));
                            patient.setSurname( set.getString( 7 ));
                            patient.setName( set.getString( 8 ));
                            patient.setFullName( set.getString( 9 ));
                            patient.setGender(  set.getInt( 10 )  == 0 ? Gender.Man : Gender.Woman );
                            patient.setPhone( set.getString( 11));
                            patient.setAddress( set.getString( 12 ));
                            Document document = new Document();
                            document.setIdDocument( set.getLong( 13 ));
                            document.setTypeDocument( set.getString( 14 ));
                            document.setSeria( set.getString( 15 ));
                            document.setNumar( set.getString( 16 ));
                            document.setSnils( set.getString( 17 ));
                            document.setPolis( set.getString( 18 ));
                            patient.setDocument( document );
                            card.setPatient( patient );
                            List<TypeComplaint> list = new ArrayList<>();
                            try( PreparedStatement st2= conn.prepareStatement( sql2 )){
                                st2.setString(1, parametr);
                                st2.setString(2, parametr);
                                st2.setString(3, parametr);
                                try( ResultSet set2 = st2.executeQuery()){
                                    while( set2.next()){
                                        TypeComplaint typeComplaint = new TypeComplaint();
                                        Complaint complaint = new Complaint();
                                        complaint.setFunctionalImpairment( set2.getString( 1 ));
                                        typeComplaint.setComplaint(complaint);
                                        typeComplaint.setIdTypeComplaint( set2.getLong(2 ));
                                        typeComplaint.setName(set2.getString( 3 ));
                                        if( typeComplaint.getComplaint().getFunctionalImpairment() != null ? list.add( typeComplaint ) :  list.isEmpty());
                                    }
                                }
                            }
                            card.setTypeComplaint(list);
                        }
                    }
                }
            });

            }catch ( Exception ex ){
                java.util.logging.Logger.getLogger( CardPatientService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
            if ( card.getIdCardPatient() == null ) throw new MyException( 404, "Карта паицента не найдена");        
            return card;
        }


}
