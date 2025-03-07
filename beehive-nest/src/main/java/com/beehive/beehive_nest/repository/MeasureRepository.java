package com.beehive.beehive_nest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beehive.beehive_nest.model.entities.Measure;

import java.util.List;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Long> {
    List<Measure> findByBeehive_Id(Long beehiveId);
}
