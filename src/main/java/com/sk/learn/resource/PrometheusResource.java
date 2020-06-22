package com.sk.learn.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spring-boot2/prometheus")
public class PrometheusResource {

    @GetMapping
    public String getMetricsFromPrometheus () {
        return "Spring boot 2 application with Prometheus.";

    }
}
