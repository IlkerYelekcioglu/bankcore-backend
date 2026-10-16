package com.bankcore.bankcore_backend.customer.service;

import com.bankcore.bankcore_backend.common.exception.DuplicateCustomerException;
import com.bankcore.bankcore_backend.common.exception.ResourceNotFoundException;
import com.bankcore.bankcore_backend.customer.dto.request.CreateCustomerRequest;
import com.bankcore.bankcore_backend.customer.dto.request.UpdateCustomerRequest;
import com.bankcore.bankcore_backend.customer.dto.response.CustomerResponse;
import com.bankcore.bankcore_backend.customer.entity.Customer;
import com.bankcore.bankcore_backend.customer.enums.CustomerStatus;
import com.bankcore.bankcore_backend.customer.mapper.CustomerMapper;
import com.bankcore.bankcore_backend.customer.repository.CustomerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  public CustomerService(
      CustomerRepository customerRepository,
      CustomerMapper customerMapper) {
    this.customerRepository = customerRepository;
    this.customerMapper = customerMapper;
  }

  @Transactional
  public CustomerResponse create(CreateCustomerRequest request) {
    String email = normalizeEmail(request.email());

    if (customerRepository.existsByEmailIgnoreCase(email)) {
      throw new DuplicateCustomerException(
          "Email already exists."
      );
    }

    if (customerRepository.existsByNationalId(
        request.nationalId())) {
      throw new DuplicateCustomerException(
          "National ID already exists."
      );
    }

    Customer customer = new Customer();

    customer.setCustomerNumber(
        "CUST-" + UUID.randomUUID()
            .toString()
            .substring(0, 12)
            .toUpperCase(Locale.ROOT)
    );

    customer.setFirstName(request.firstName().trim());
    customer.setLastName(request.lastName().trim());
    customer.setEmail(email);
    customer.setPhone(request.phone().trim());
    customer.setNationalId(request.nationalId().trim());
    customer.setBirthDate(request.birthDate());
    customer.setStatus(CustomerStatus.ACTIVE);

    try {
      Customer saved =
          customerRepository.saveAndFlush(customer);

      return customerMapper.toResponse(saved);
    } catch (DataIntegrityViolationException ex) {
      throw new DuplicateCustomerException(
          "Customer data conflicts with an existing record."
      );
    }
  }

  @Transactional(readOnly = true)
  public List<CustomerResponse> findAll() {
    return customerRepository.findAll()
        .stream()
        .map(customerMapper::toResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public CustomerResponse findById(UUID id) {
    return customerMapper.toResponse(getCustomer(id));
  }

  @Transactional
  public CustomerResponse update(UUID id, UpdateCustomerRequest request) {

    Customer customer = getCustomer(id);
    String email = normalizeEmail(request.email());

    if (customerRepository
        .existsByEmailIgnoreCaseAndIdNot(email, id)) {
      throw new DuplicateCustomerException(
          "Email already exists."
      );
    }

    customer.setFirstName(request.firstName().trim());
    customer.setLastName(request.lastName().trim());
    customer.setEmail(email);
    customer.setPhone(request.phone().trim());
    customer.setBirthDate(request.birthDate());

    try {
      return customerMapper.toResponse(
          customerRepository.saveAndFlush(customer)
      );
    } catch (DataIntegrityViolationException ex) {
      throw new DuplicateCustomerException(
          "Customer data conflicts with an existing record."
      );
    }
  }

  @Transactional
  public CustomerResponse deactivate(UUID id) {
    Customer customer = getCustomer(id);
    customer.setStatus(CustomerStatus.INACTIVE);

    return customerMapper.toResponse(
        customerRepository.saveAndFlush(customer)
    );
  }

  private Customer getCustomer(UUID id) {
    return customerRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Customer not found: " + id
        ));
  }

  private String normalizeEmail(String email) {
    return email.trim().toLowerCase(Locale.ROOT);
  }
}