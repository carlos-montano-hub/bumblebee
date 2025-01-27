package com.beehive.beehiveNest.repository;

import com.beehive.beehiveNest.model.entities.Measure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Long> {
    List<Measure> findByBeehive_Id(Long beehiveId);
}
