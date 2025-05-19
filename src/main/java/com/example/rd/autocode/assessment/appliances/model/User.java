package com.example.rd.autocode.assessment.appliances.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class User {
    Long id; String name, email, password;
}
