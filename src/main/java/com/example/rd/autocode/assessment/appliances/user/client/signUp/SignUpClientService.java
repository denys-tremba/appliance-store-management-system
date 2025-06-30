package com.example.rd.autocode.assessment.appliances.user.client.signUp;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SignUpClientService {
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final ApplicationEventPublisher eventPublisher;
    public void save(String name, String email, String password, String card) {
        Client client = Client.create(name, email, passwordEncoder.encode(password), card);
        clientRepository.save(client);
        eventPublisher.publishEvent(new ClientRegistered(client));
    }
}
