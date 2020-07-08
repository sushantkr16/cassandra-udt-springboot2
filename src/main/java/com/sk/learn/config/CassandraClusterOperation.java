package com.sk.learn.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CassandraClusterOperation {

    @Value("${spring.data.cassandra.primary:true}")
    private boolean primaryCluster;

}