package com.example.rd.autocode.assessment.appliances.user.client.signUp;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.logging.BusinessLogicEvent;

public record ClientRegistered(Client client) implements BusinessLogicEvent {
    @Override
    public String describe() {
        return "Client %s [%s] registered himself".formatted(client.getName(), client.getEmail());
    }
}
