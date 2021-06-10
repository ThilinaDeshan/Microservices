package com.microservices.customerservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.customerservice.dto.Customer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import reactor.core.publisher.Flux;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class WebSocketConfiguration {
    private final ObjectMapper objectMapper;

    @SneakyThrows
    private String getCustomerString(Customer customer){
        return this.objectMapper.writeValueAsString(customer);
    }

    @Bean
    WebSocketHandler webSocketHandler(Flux<Customer> customerFlux){
        return webSocketSession -> {
            var map = customerFlux
                    .map(customer -> getCustomerString(customer))
                    .map(str->webSocketSession.textMessage(str));
            return webSocketSession.send(map);
        };
    }

    @Bean
    SimpleUrlHandlerMapping simpleUrlHandlerMapping(WebSocketHandler webSocketHandler){
        return new SimpleUrlHandlerMapping(Map.of("/ws/customers",webSocketHandler),10);
    }
}
