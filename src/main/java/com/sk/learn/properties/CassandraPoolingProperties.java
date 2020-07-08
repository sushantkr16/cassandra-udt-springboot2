package com.sk.learn.properties;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "spring.data.cassandra.pooling")
@Data
@NoArgsConstructor
public class CassandraPoolingProperties {

    private Integer heartbeatIntervalSeconds = 300;
    private Integer idleTimeoutSeconds = 120;
    private Integer healthCheckInterval = 300;

    @NestedConfigurationProperty
    CassandraPoolingProperties.CassandraDistancePoolProperties local;

    @NestedConfigurationProperty
    CassandraPoolingProperties.CassandraDistancePoolProperties remote;

    @Getter
    @Setter
    public static class CassandraDistancePoolProperties {
        private Integer coreConnectionsPerHost = 1;
        private Integer maxConnectionsPerHost = 1;
        private Integer maxSimultaneousRequests = 256;
        private Integer minSimultaneousRequests;
    }
}
