package com.klinik.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.klinik.entity.Drug;

@Repository
public interface DrugRepository extends JpaRepository< Drug, Long>{

    @Query( "SELECT d FROM Drug d WHERE d.name = :word")
    public Optional<Drug> findByName( String word );

    @Query( "SELECT e FROM Drug e WHERE e.drugTreatment.idDrugTreatment = :id")
    List<Drug> findByIdDrugTreatment( Long id);
    
}
