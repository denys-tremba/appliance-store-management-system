package com.example.rd.autocode.assessment.appliances.user.client.signUp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Transactional(readOnly = true)
public interface ClientRepository extends JpaRepository<Client, Long>, PagingAndSortingRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
}
