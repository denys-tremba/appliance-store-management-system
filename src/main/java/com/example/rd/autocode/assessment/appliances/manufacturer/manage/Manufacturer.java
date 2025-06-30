package com.example.rd.autocode.assessment.appliances.manufacturer.manage;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level=AccessLevel.PRIVATE)
@Table(name = "manufacturers")
@Entity
public class Manufacturer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, length = 32)
    String name;

    public static Manufacturer create(String name) {
        return new Manufacturer(null, name);
    }
}
