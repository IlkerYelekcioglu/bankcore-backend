package com.bankcore.bankcore_backend.customer.repository;

import com.bankcore.bankcore_backend.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository
    extends JpaRepository<Customer, UUID> {

  boolean existsByEmailIgnoreCase(String email);

  boolean existsByNationalId(String nationalId);

  boolean existsByEmailIgnoreCaseAndIdNot(String email, UUID id);
}