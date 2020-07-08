package com.sk.learn.config;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.sk.learn.properties.CassandraPoolingProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
public class CommonCassandraConfig {

  @Autowired
  protected CassandraProperties cassandraProperties;

  @Autowired
  protected CassandraPoolingProperties poolingProperties;

  protected QueryOptions getQueryOptions() {
    QueryOptions options = new QueryOptions();
    CassandraProperties properties = this.cassandraProperties;
    if (properties.getConsistencyLevel() != null) {
      options.setConsistencyLevel(properties.getConsistencyLevel());
    }
    if (properties.getSerialConsistencyLevel() != null) {
      options.setSerialConsistencyLevel(properties.getSerialConsistencyLevel());
    }
    options.setFetchSize(properties.getFetchSize());
    return options;
  }

  protected void printCassandraDetails(Cluster cluster, Session session) {
    final LoadBalancingPolicy loadBalancingPolicy =
            cluster.getConfiguration().getPolicies().getLoadBalancingPolicy();
    final PoolingOptions poolingOptions =
            cluster.getConfiguration().getPoolingOptions();

    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    scheduler.scheduleAtFixedRate(() -> {
      Session.State state = session.getState();
      for (Host host : state.getConnectedHosts()) {
        HostDistance distance = loadBalancingPolicy.distance(host);
        int connections = state.getOpenConnections(host);
        int inFlightQueries = state.getInFlightQueries(host);
        log.debug("op={}, status=OK, desc=Host: {}, Connections: {}, Current load: {}, Max Load: {}, Distance: {}",
                "cassandra_metrics", host, connections, inFlightQueries,
                connections * poolingOptions.getMaxRequestsPerConnection(distance), distance);
      }
    }, poolingProperties.getHealthCheckInterval(), poolingProperties.getHealthCheckInterval(), TimeUnit.SECONDS);

  }
}
