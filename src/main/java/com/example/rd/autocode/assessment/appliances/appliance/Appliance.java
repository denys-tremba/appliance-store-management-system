package com.example.rd.autocode.assessment.appliances.appliance;

import com.example.rd.autocode.assessment.appliances.manufacturer.manage.Manufacturer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "appliances")
@Entity
public class Appliance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, length = 32)
    String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Category category;
    @Column(nullable = false, length = 32)
    String model;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PowerType powerType;
    @ManyToOne
    Manufacturer manufacturer;
    @Column(nullable = false, length = 255)
    String characteristic;
    @Column(nullable = false, length = 255)
    String description;
    @Column(nullable = false)
    Integer power;
    @Column(nullable = false)
    BigDecimal price;
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant lastUpdatedAt;
    @Version
    long version;
}
