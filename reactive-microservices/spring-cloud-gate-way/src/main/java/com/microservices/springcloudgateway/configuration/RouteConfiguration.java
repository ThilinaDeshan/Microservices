package com.microservices.springcloudgateway.configuration;

import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.SetPathGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Spring @Configuration annotation is part of the spring core framework.
// Spring Configuration annotation indicates that the class has @Bean definition methods.
// So Spring container can process the class and generate Spring Beans to be used in the application
// https://cloud.spring.io/spring-cloud-gateway/reference/html/

@Configuration
public class RouteConfiguration {

    //    Spring @Bean annotation tells that a method produces a bean to be managed by the Spring container.
    //    It is a method-level annotation.
    //    During Java configuration ( @Configuration ),
    //    the method is executed and its return value is registered as a bean within a BeanFactory

    @Bean
    RouteLocator getRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
//        Route: The basic building block of the gateway. It is defined by an ID, a destination URI,
//        a collection of predicates, and a collection of filters.
//        A route is matched if the aggregate predicate is true.

//        Predicate: This is a Java 8 Function Predicate.
//        The input type is a Spring Framework ServerWebExchange.
//        This lets you match on anything from the HTTP request, such as headers or parameters.

//        Filter: These are instances of Spring Framework GatewayFilter that have been constructed with a specific factory.
//        Here, you can modify requests and responses before or after sending the downstream request.

        return routeLocatorBuilder
                .routes()
//              when guides route is called the request is forward to spring guide web page
//				predicated is like a if statement ,checks if the path contains the following route
//				using filter we have set an additional path to the downstream API
//				uri is the destination that need to be route
                .route(predicateSpec ->
                        predicateSpec.path("/guides")
                                .filters(gatewayFilterSpec ->
                                        gatewayFilterSpec.setPath("/guides")
                                ).uri("https://spring.io/"))
//				using regex for filter
                .route("github",rs->rs
						.path("/github/**")
						.filters(gatewayFilterSpec -> gatewayFilterSpec
								.rewritePath(
										"/github/(?<handler>.*)",
										"/${handler}")
								)
						.uri("https://github.com/@")
				)
//				load balancing routes
				.route(
						routeSpec->routeSpec
								.path("/customers/**")
								.uri("lb://customer-service/")
				)
                .build();
    }

    @Bean
	RouteLocator customRoute(SetPathGatewayFilterFactory ff){
		var singleRoute =
				Route.async()
						.id("custom-route-01")
						.filter(new OrderedGatewayFilter(ff.apply(config -> config.setTemplate("/custom")), 1))
						.uri("lb://customer-service/").asyncPredicate(serverWebExchange -> {
							var uri = serverWebExchange.getRequest().getURI();
							var path = uri.getPath();
							var match = path.contains("/custom");
							return Mono.just(match);
						}
						).build();
		return () -> Flux.just(singleRoute);
	}
}
