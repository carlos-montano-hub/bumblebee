package com.beehive.beehive_nest.model.forms;

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
