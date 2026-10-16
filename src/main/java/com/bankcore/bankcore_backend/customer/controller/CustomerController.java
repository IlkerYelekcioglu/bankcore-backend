package com.bankcore.bankcore_backend.customer.controller;

import com.bankcore.bankcore_backend.customer.dto.request.CreateCustomerRequest;
import com.bankcore.bankcore_backend.customer.dto.request.UpdateCustomerRequest;
import com.bankcore.bankcore_backend.customer.dto.response.CustomerResponse;
import com.bankcore.bankcore_backend.customer.service.CustomerService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CustomerResponse create(
      @Valid @RequestBody CreateCustomerRequest request) {
    return customerService.create(request);
  }

  @GetMapping
  public List<CustomerResponse> findAll() {
    return customerService.findAll();
  }

  @GetMapping("/{id}")
  public CustomerResponse findById(@PathVariable UUID id) {
    return customerService.findById(id);
  }

  @PutMapping("/{id}")
  public CustomerResponse update(
      @PathVariable UUID id,
      @Valid @RequestBody UpdateCustomerRequest request) {
    return customerService.update(id, request);
  }

  @PatchMapping("/{id}/deactivate")
  public CustomerResponse deactivate(@PathVariable UUID id) {
    return customerService.deactivate(id);
  }
}