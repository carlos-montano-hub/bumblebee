package com.beehive.beehiveNest.repository;

import com.beehive.beehiveNest.model.entities.Apiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApiaryRepository extends JpaRepository<Apiary, Long> {
    List<Apiary> findByOwner_Id(UUID userId);
}
