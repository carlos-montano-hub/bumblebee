package com.beehive.beehive_nest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beehive.beehive_nest.model.entities.Apiary;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApiaryRepository extends JpaRepository<Apiary, Long> {
    List<Apiary> findByOwner(UUID userId);
}
