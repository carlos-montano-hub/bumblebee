package com.beehive.beehiveNest.model.forms;

import java.util.UUID;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ApiaryForm {
    private String name;
    private UUID ownerId;
}
