package com.berd.dev.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.berd.dev.models.Depense;

public interface DepenseRepository extends JpaRepository<Depense, Integer>, JpaSpecificationExecutor<Depense> {
    List<Depense> findByUtilisateurIdUtilisateur(Long idUtilisateur);

    @Query("SELECT DISTINCT d FROM depense d JOIN FETCH d.depenseDetails")
    List<Depense> findAllWithDepenseDetails();

    @Query("SELECT DISTINCT d FROM depense d JOIN FETCH d.depenseDetails WHERE d.idDepense = :id_depense")
    Depense findAllWithDepenseDetailsByIdDepense(@Param("id_depense") Integer idDepense);

}
