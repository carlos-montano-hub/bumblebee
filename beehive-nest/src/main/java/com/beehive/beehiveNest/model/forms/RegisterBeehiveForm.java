package com.beehive.beehiveNest.model.forms;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RegisterBeehiveForm {
    private String name;
    private String serial;
    private Long apiaryId;
}