package com.bankcore.bankcore_backend.customer.mapper;

import com.bankcore.bankcore_backend.customer.dto.response.CustomerResponse;
import com.bankcore.bankcore_backend.customer.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

  public CustomerResponse toResponse(Customer customer) {
    return new CustomerResponse(
        customer.getId(),
        customer.getCustomerNumber(),
        customer.getFirstName(),
        customer.getLastName(),
        customer.getEmail(),
        customer.getPhone(),
        customer.getBirthDate(),
        customer.getStatus(),
        customer.getCreatedAt(),
        customer.getUpdatedAt()
    );
  }
}