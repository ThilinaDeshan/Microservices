package com.microservices.customerservice.configuration;

import com.microservices.customerservice.dto.Customer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Configuration
public class CustomerDataStreamConfiguration {

    private final String[] names = "Thilina,Deshan,Supun,Sisihara,Dewdu,Diyanga".split(",");
    private final AtomicLong counter = new AtomicLong();
    private final Flux<Customer> customers = getCustomerFlux();


    private Flux<Customer> getCustomerFlux() {
        return Flux.fromStream(
                Stream.generate(() -> {
                            var id = counter.incrementAndGet();
                            var index = id % names.length;
                            return new Customer(id, names[Integer.parseInt(Long.toString(index))]);
                        }
                )
        ).delayElements(Duration.ofSeconds(3));
    }

    @Bean
    Flux<Customer> customers() {
        // using auto connect this will provide the same stream for each request
        return this.customers.publish().autoConnect();
    }

}
