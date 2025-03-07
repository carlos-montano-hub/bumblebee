package com.beehive.beehive_nest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beehive.beehive_nest.model.entities.Beehive;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeehiveRepository extends JpaRepository<Beehive, Long> {
    List<Beehive> findByApiary_Id(Long apiaryId);

    Optional<Beehive> findBySerial(String serial);
}
