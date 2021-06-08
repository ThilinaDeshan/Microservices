package com.microservices.customerservice.service;

import com.microservices.customerservice.dto.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService {
    Flux<Customer> getV1Customers();
    Mono<String> getCustom();
}
