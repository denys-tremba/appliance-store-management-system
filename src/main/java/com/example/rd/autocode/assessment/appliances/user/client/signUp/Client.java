package com.example.rd.autocode.assessment.appliances.user.client.signUp;

import com.example.rd.autocode.assessment.appliances.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString(callSuper = true)
@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@DiscriminatorValue("client")
public class Client extends User {
    @ToString.Exclude
    @Column(length = 16)
    String card;

    public Client(Long id, String name, String email, String password, String card, Boolean locked) {
        super(id, name, email, password, locked);
        this.card = card;
    }
    public static Client create(String name, String email, String password, String card) {
        return new Client(null, name, email, password, card, false);
    }
}
