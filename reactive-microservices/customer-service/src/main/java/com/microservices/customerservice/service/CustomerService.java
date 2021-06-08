package com.microservices.customerservice.service;

import com.microservices.customerservice.dto.Customer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
public class CustomerService implements ICustomerService {
    @Override
    public Flux<Customer> getV1Customers() {
        return Flux.just(
                new Customer(1L, "Thilina"),
                new Customer(2L, "Deshan"),
                new Customer(3L, "Wishvekeerthi"));
    }
}
