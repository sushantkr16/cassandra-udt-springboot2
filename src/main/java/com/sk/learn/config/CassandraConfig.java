package com.sk.learn.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryLogger;
import com.datastax.driver.core.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.PoolingOptionsFactoryBean;
import org.springframework.data.cassandra.core.*;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;

@Primary
@Configuration("springDataCassandraConfig")
@Slf4j
public class CassandraConfig extends CommonCassandraConfig {

  @Value("${spring.data.cassandra.ttl}")
  private Integer ttl;

  @Value("${spring.data.cassandra.slowQueryLatencyThresholdMillis:20}")
  private Long slowQueryLatencyThresholdMillis;

  @Autowired
  @Qualifier("cluster")
  private Cluster cluster;

  @Bean
  @Qualifier("cluster")
  @Primary
  public CassandraClusterFactoryBean cluster(
          PoolingOptionsFactoryBean poolingFactory) throws Exception {

    CassandraClusterFactoryBean clusterFactory =
            new ConfigurableCassandraClusterFactoryBean(cassandraProperties);
    clusterFactory.setPoolingOptions(poolingFactory.getObject());
    clusterFactory.setQueryOptions(getQueryOptions());
    clusterFactory.setJmxReportingEnabled(false);
    return clusterFactory;
  }

  @Bean
  @ConditionalOnProperty(value="slow.query.enabled", havingValue = "true", matchIfMissing = false)
  public QueryLogger queryLogger(@Qualifier("cluster") Cluster cluster) {
    QueryLogger queryLogger = QueryLogger.builder()
        .withConstantThreshold(slowQueryLatencyThresholdMillis).build();
    cluster.register(queryLogger);
    return queryLogger;
  }


  @Bean("session")
  @Primary
  public Session getSessionBeanLocal(@Qualifier("cluster") Cluster cluster) {
    Session session = cluster.connect(cassandraProperties.getKeyspaceName());
    printCassandraDetails(cluster, session);
    return session;
  }

  /**
   * create mapping context.
   *
   * @return mapping context
   * @throws Exception throw exception if any.
   */

  @Bean("mappingContext")
  @Primary
  public CassandraMappingContext mappingContext() throws Exception {
    CassandraMappingContext mappingContext = new CassandraMappingContext();
    mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(
            cluster, cassandraProperties.getKeyspaceName()));
    return mappingContext;
  }

  @Bean
  @Qualifier("cassandraOperation")
  @Primary
  @DependsOn({"mappingContext"})
  public CassandraOperations getPrimaryCassandraOperations() throws Exception {
    return new CassandraTemplate(getSessionBeanLocal(cluster), converter());
  }

  @Bean
  @Qualifier("asyncCassandraOperation")
  @Primary
  @DependsOn({"mappingContext"})
  public AsyncCassandraOperations getPrimaryAsyncCassandraOperation() throws Exception {
    return new AsyncCassandraTemplate(getSessionBeanLocal(cluster), converter());
  }

  @Bean
  @Primary
  public CassandraConverter converter() throws Exception {
    return new MappingCassandraConverter(mappingContext());
  }

  @Bean
  public InsertOptions insertOptions() throws Exception {
    InsertOptions insertOptions = InsertOptions.builder()
            .ttl(ttl)
            .build();
    return insertOptions;
  }

  @Bean
  public UpdateOptions updateOptions() throws Exception {
    UpdateOptions updateOptions = UpdateOptions.builder()
            .ttl(ttl)
            .build();
    return updateOptions;
  }

  /**
   * Create pooling options.
   *
   * @return PoolingOptionsFactoryBean pooling options bean.
   * @throws Exception throw exception if any.
   */
  @Bean
  @SuppressWarnings({"squid:S00112"})
  public PoolingOptionsFactoryBean poolingOptions() throws Exception {
    PoolingOptionsFactoryBean poolingOptionsFactoryBean = new PoolingOptionsFactoryBean();

    poolingOptionsFactoryBean
            .setIdleTimeoutSeconds(poolingProperties.getIdleTimeoutSeconds());
    poolingOptionsFactoryBean
            .setHeartbeatIntervalSeconds(poolingProperties.getHeartbeatIntervalSeconds());
    poolingOptionsFactoryBean
            .setLocalCoreConnections(poolingProperties.getLocal().getCoreConnectionsPerHost());
    poolingOptionsFactoryBean
            .setLocalMaxConnections(poolingProperties.getLocal().getMaxConnectionsPerHost());
    poolingOptionsFactoryBean
            .setLocalMaxSimultaneousRequests(poolingProperties.getLocal().getMaxSimultaneousRequests());
    poolingOptionsFactoryBean
            .setLocalMinSimultaneousRequests(poolingProperties.getLocal().getMinSimultaneousRequests());
    poolingOptionsFactoryBean
            .setRemoteCoreConnections(poolingProperties.getRemote().getCoreConnectionsPerHost());
    poolingOptionsFactoryBean
            .setRemoteMaxConnections(poolingProperties.getRemote().getMaxConnectionsPerHost());
    poolingOptionsFactoryBean
            .setRemoteMaxSimultaneousRequests(poolingProperties.getRemote().getMaxSimultaneousRequests());
    poolingOptionsFactoryBean
            .setRemoteMinSimultaneousRequests(poolingProperties.getRemote().getMinSimultaneousRequests());

    return poolingOptionsFactoryBean;
  }

}