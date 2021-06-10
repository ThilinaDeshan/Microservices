package com.microservices.springcloudgateway.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.event.RefreshRoutesResultEvent;
import org.springframework.cloud.gateway.route.CachingRouteLocator;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
public class RefreshRouteListener {
    Logger logger = LoggerFactory.getLogger(RefreshRouteListener.class);

    private final AtomicBoolean wsEnabled = new AtomicBoolean(false);

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

    @Bean
    @RefreshScope
    RouteLocator refreshRoute(RouteLocatorBuilder rlb){
        var id = "customer-refresh";
        if(!wsEnabled.get()) {
            this.wsEnabled.set(true);
            return rlb.routes()
                    .route(
                            id,
                            rs->rs.path("/customer-refresh")
                                    .filters(gatewayFilterSpec -> gatewayFilterSpec.setPath("/customers"))
                                    .uri("lb://customer-service")
                    ).build();
        }
        else //
            {
                return rlb.routes()
                        .route(
                                id,
                                rs->rs.path("/customer-refresh")
                                        .filters(gatewayFilterSpec -> gatewayFilterSpec.setPath("/ws/customers"))
                                        .uri("lb://customer-service/")
                        ).build();

            }
    }

}
