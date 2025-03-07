package com.beehive.beehive_nest.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beehive.beehive_nest.configuration.DatabaseInitializer;
import com.beehive.beehive_nest.model.entities.*;
import com.beehive.beehive_nest.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TestDataService {

        @Autowired
        private ApiaryRepository apiaryRepository;

        @Autowired
        private BeehiveRepository beehiveRepository;

        @Autowired
        private MeasureRepository measureRepository;

        @Transactional
        public void insertTestData() {
                if (!DatabaseInitializer.isInitialized()) {
                        return;
                }

                Apiary greenfieldApiary = new Apiary(1L, "Greenfield Apiary",
                                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), null);
                Apiary blueMeadowApiary = new Apiary(2L, "Blue Meadow Apiary",
                                UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), null);
                Apiary sunnyHiveApiary = new Apiary(3L, "Sunny Hive Apiary",
                                UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), null);
                apiaryRepository.save(greenfieldApiary);
                apiaryRepository.save(blueMeadowApiary);
                apiaryRepository.save(sunnyHiveApiary);

                Beehive hiveA = new Beehive(1L, null, "SER001", 34.0522, -118.2437, null, null);
                Beehive hiveB = new Beehive(2L, "Hive B", "SER002", 34.0532, -118.2438, greenfieldApiary, null);
                Beehive hiveC = new Beehive(3L, "Hive C", "SER003", 35.6895, -139.6917, greenfieldApiary, null);
                Beehive hiveD = new Beehive(4L, "Hive D", "SER004", 36.2048, 138.2529, greenfieldApiary, null);

                beehiveRepository.save(hiveA);
                beehiveRepository.save(hiveB);
                beehiveRepository.save(hiveC);
                beehiveRepository.save(hiveD);

                Measure measure1 = new Measure(1L, LocalDateTime.now(), hiveA, 34.5, 60.0, 75.5,
                                "https://example.com/audio/recording1.mp3");
                Measure measure2 = new Measure(2L, LocalDateTime.now().minusMinutes(15), hiveA, 33.1, 58.0, 74.8,
                                "https://example.com/audio/recording2.mp3");
                Measure measure3 = new Measure(3L, LocalDateTime.now().minusMinutes(30), hiveA, 31.8, 61.5, 73.2,
                                "https://example.com/audio/recording3.mp3");
                Measure measure4 = new Measure(4L, LocalDateTime.now().minusMinutes(45), hiveA, 30.2, 64.0, 72.5,
                                "https://example.com/audio/recording4.mp3");
                Measure measure5 = new Measure(5L, LocalDateTime.now().minusMinutes(60), hiveA, 34.0, 57.8, 75.9,
                                "https://example.com/audio/recording5.mp3");
                Measure measure6 = new Measure(6L, LocalDateTime.now().minusMinutes(75), hiveA, 32.7, 59.0, 76.1,
                                "https://example.com/audio/recording6.mp3");
                Measure measure7 = new Measure(7L, LocalDateTime.now().minusMinutes(90), hiveA, 33.5, 62.3, 74.0,
                                "https://example.com/audio/recording7.mp3");
                Measure measure8 = new Measure(8L, LocalDateTime.now().minusMinutes(105), hiveA, 29.9, 65.1, 72.3,
                                "https://example.com/audio/recording8.mp3");
                Measure measure9 = new Measure(9L, LocalDateTime.now().minusMinutes(120), hiveA, 30.5, 63.2, 73.6,
                                "https://example.com/audio/recording9.mp3");
                Measure measure10 = new Measure(10L, LocalDateTime.now().minusMinutes(135), hiveA, 35.0, 56.5, 77.2,
                                "https://example.com/audio/recording10.mp3");

                Measure measure11 = new Measure(11L, LocalDateTime.now().minusMinutes(150), hiveA, 33.2, 60.5, 75.1,
                                "https://example.com/audio/recording11.mp3");
                Measure measure12 = new Measure(12L, LocalDateTime.now().minusMinutes(165), hiveA, 31.4, 62.0, 74.4,
                                "https://example.com/audio/recording12.mp3");
                Measure measure13 = new Measure(13L, LocalDateTime.now().minusMinutes(180), hiveA, 30.8, 64.7, 73.0,
                                "https://example.com/audio/recording13.mp3");
                Measure measure14 = new Measure(14L, LocalDateTime.now().minusMinutes(195), hiveA, 32.0, 61.2, 74.5,
                                "https://example.com/audio/recording14.mp3");
                Measure measure15 = new Measure(15L, LocalDateTime.now().minusMinutes(210), hiveA, 34.3, 58.9, 76.7,
                                "https://example.com/audio/recording15.mp3");
                Measure measure16 = new Measure(16L, LocalDateTime.now().minusMinutes(225), hiveA, 29.7, 66.0, 72.8,
                                "https://example.com/audio/recording16.mp3");
                Measure measure17 = new Measure(17L, LocalDateTime.now().minusMinutes(240), hiveA, 31.1, 63.5, 73.9,
                                "https://example.com/audio/recording17.mp3");
                Measure measure18 = new Measure(18L, LocalDateTime.now().minusMinutes(255), hiveA, 33.9, 59.8, 75.4,
                                "https://example.com/audio/recording18.mp3");
                Measure measure19 = new Measure(19L, LocalDateTime.now().minusMinutes(270), hiveA, 30.3, 65.8, 73.1,
                                "https://example.com/audio/recording19.mp3");
                Measure measure20 = new Measure(20L, LocalDateTime.now().minusMinutes(285), hiveA, 34, 60.9, 74.6,
                                "https://example.com/audio/recording20.mp3");
                Measure measure21 = new Measure(3L, LocalDateTime.now(), hiveB, 31.8, 62.0, 76.0,
                                "https://example.com/audio/recording3.mp3");
                Measure measure22 = new Measure(4L, LocalDateTime.now(), hiveC, 30.0, 55.0, 80.2,
                                "https://example.com/audio/recording4.mp3");
                Measure measure23 = new Measure(5L, LocalDateTime.now(), hiveD, 29.5, 57.5, 78.9,
                                "https://example.com/audio/recording5.mp3");
                measureRepository.saveAll(List.of(
                                measure1, measure2, measure3, measure4, measure5,
                                measure6, measure7, measure8, measure9, measure10,
                                measure11, measure12, measure13, measure14, measure15,
                                measure16, measure17, measure18, measure19, measure20,
                                measure21, measure22, measure23));
        }
}
