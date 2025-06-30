package com.example.rd.autocode.assessment.appliances.auxiliary;

import com.example.rd.autocode.assessment.appliances.user.client.signUp.Client;

public class ClientBuilder {
    Long id = 0L;
    String name = "";
    String email = "";
    String password = "";
    String card = "";

    public ClientBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ClientBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ClientBuilder email(String email) {
        this.email = email;
        return this;
    }

    public ClientBuilder password(String password) {
        this.password = password;
        return this;
    }

    public ClientBuilder card(String card) {
        this.card = card;
        return this;
    }

    public Client build() {
        return new Client(id, name, email, password, card, null);
    }
}
