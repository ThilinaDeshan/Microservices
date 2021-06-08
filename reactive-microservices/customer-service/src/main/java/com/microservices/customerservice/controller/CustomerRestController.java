package com.microservices.customerservice.controller;

import com.microservices.customerservice.dto.Customer;
import com.microservices.customerservice.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
public class CustomerRestController {
    private final Flux<Customer> customerFlux;
    private final CustomerService customerService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/customers")
    Flux<Customer> getCustomers(){
        return this.customerFlux;
    }

    @GetMapping(value = "/customers/v1")
    Flux<Customer> getV1customers(){
        return customerService.getV1Customers();
    }
}
