package com.microservices.springcloudgateway;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouteLocatorTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void	guidesRouteShouldRedirectToWebSiteSpringCloudGuides(){
        var response
                = this.testRestTemplate.getForEntity("http://localhost:" + port + "/guides",String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo((HttpStatus.OK));
        Assertions.assertThat(response.getBody().contains("Spring Cloud")).isEqualTo(Boolean.valueOf(true));
    }

    @Test
    public void githubWithUserNameRouteShouldRedirectToGithubWebSiteWithParsingUsername(){
        var response
                = this.testRestTemplate.getForEntity("http://localhost:" + port + "/github/ThilinaDeshan",String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo((HttpStatus.OK));
        Assertions.assertThat(response.getBody().contains("ThilinaDeshan has 2 repositories available. Follow their code on GitHub")).isEqualTo(Boolean.valueOf(true));

    }
}
