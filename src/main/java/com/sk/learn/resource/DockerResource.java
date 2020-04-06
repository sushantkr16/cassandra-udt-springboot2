package com.sk.learn.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spring-boot2/docker")
public class DockerResource {

    @GetMapping
    public String dockerizeSpringBoot2Application () {
        return "Spring boot 2 application dockerized.";

    }
}
