package com.beehive.beehiveNest.model.forms;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SelfRegisterBeehiveForm {
    private double latitude;
    private double longitude;
    private String serial;
}