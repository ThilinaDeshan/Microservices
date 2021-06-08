package com.microservices.springcloudgateway.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesResultEvent;
import org.springframework.cloud.gateway.route.CachingRouteLocator;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class RefreshRouteListener {
    Logger logger = LoggerFactory.getLogger(RefreshRouteListener.class);

    @Bean
    ApplicationListener<RefreshRoutesResultEvent> routesRefreshed(){

//        return new ApplicationListener<RefreshRoutesResultEvent>() {
//            @Override
//            public void onApplicationEvent(RefreshRoutesResultEvent refreshRoutesResultEvent) {
//                CachingRouteLocator crl = (CachingRouteLocator)refreshRoutesResultEvent.getSource();
//                Flux<Route> routes = crl.getRoutes();
//                routes.subscribe(r-> logger.info(r.toString()));
//            }
//        };
    // //      using lambda functions
        return refreshRoutesResultEvent -> {
            logger.info("--- routes refresh(updated) ---");
            var crl = (CachingRouteLocator)refreshRoutesResultEvent.getSource();
            Flux<Route> routes = crl.getRoutes();
            routes.subscribe(System.out::println);
        };
    }
}
