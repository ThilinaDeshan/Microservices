package com.microservices.customerservice.service;

import com.microservices.customerservice.dto.Customer;
import reactor.core.publisher.Flux;

public interface ICustomerService {
    Flux<Customer> getV1Customers();
}
