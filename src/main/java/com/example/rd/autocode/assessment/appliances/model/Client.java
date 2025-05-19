package com.example.rd.autocode.assessment.appliances.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Client extends User{
    String card;

    public Client(Long id, String name, String email, String password, String card) {
        super(id, name, email, password);
        this.card = card;
    }

    public Client() {
    }
}
