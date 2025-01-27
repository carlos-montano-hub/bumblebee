package com.beehive.beehiveNest.model.forms;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BeehiveForm {
    private String name;
    private String serial;
    private double latitude;
    private double longitude;
    private Long apiaryId;
}