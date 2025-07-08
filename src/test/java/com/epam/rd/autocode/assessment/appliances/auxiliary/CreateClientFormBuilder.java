package com.example.rd.autocode.assessment.appliances.auxiliary;

import com.example.rd.autocode.assessment.appliances.user.client.signUp.SignUpClientForm;

public class CreateClientFormBuilder {
    String name = "name";
    String email = "email";
    String password = "pass";
    String passwordRepeated = "pass";
    String card = "123";

    public CreateClientFormBuilder name(String name) {
        this.name = name;
        return this;
    }
    public CreateClientFormBuilder email(String email) {
        this.email = email;
        return this;
    }
    public CreateClientFormBuilder password(String password) {
        this.password = password;
        return this;
    }
    public CreateClientFormBuilder passwordRepeated(String passwordRepeated) {
        this.passwordRepeated = passwordRepeated;
        return this;
    }
    public CreateClientFormBuilder card(String card) {
        this.card = card;
        return this;
    }

    public SignUpClientForm build() {

        SignUpClientForm form = new SignUpClientForm();
        form.setCard(card);
        form.setName(name);
        form.setPassword(password);
        form.setEmail(email);
        form.setPasswordRepeated(passwordRepeated);
        return form;
    }
}
