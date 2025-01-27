package com.beehive.beehiveNest.repository;

import com.beehive.beehiveNest.model.entities.Beehive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeehiveRepository extends JpaRepository<Beehive, Long> {
    List<Beehive> findByApiary_Id(Long apiaryId);

    Optional<Beehive> findBySerial(String serial);
}
