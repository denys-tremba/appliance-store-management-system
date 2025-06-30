package com.example.rd.autocode.assessment.appliances.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role")
public class User {
    public final static User ANONYMOUS = new User(0L,"Unknown", "Unknown", "", true);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, length = 32)
    String name;
    @Column(nullable = false, length = 32)
    String email;
    @ToString.Exclude
    @Column(nullable = false, length = 255)
    String password;
    @Column(nullable = false)
    @ColumnDefault("false")
    Boolean locked;
}
