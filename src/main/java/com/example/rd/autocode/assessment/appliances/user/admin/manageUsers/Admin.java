package com.example.rd.autocode.assessment.appliances.user.admin.manageUsers;

import com.example.rd.autocode.assessment.appliances.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString(callSuper = true)
@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Entity
@DiscriminatorValue("admin")
public class Admin extends User {
    public static Admin create() {
        Admin admin = new Admin();
        admin.setEmail("admin");
        admin.setName("admin");
        admin.setPassword("{noop}admin");
        admin.setLocked(false);
        return admin;
    }
}
