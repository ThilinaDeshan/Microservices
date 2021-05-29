package com.microservices.customerservice.controller;

import com.microservices.customerservice.dto.Customer;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
public class CustomerRestController {
    private final Flux<Customer> customerFlux;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/customers")
    Flux<Customer> getCustomers(){
        return this.customerFlux;
    }
}
