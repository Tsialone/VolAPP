package com.berd.dev.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.berd.dev.models.Caisse;

public interface CaisseRepository extends JpaRepository <Caisse , Integer> {

    @Query ("SELECT DISTINCT  c  FROM caisse c    JOIN FETCH c.caisseMvts  WHERE c.idCaisse = :id_caisse")
    Caisse findByIdWithCaisseMvt (@Param("id_caisse") Integer idCaisse);
}
