package com.sk.learn.config;

import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.SocketOptions;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;

import static java.util.Objects.nonNull;


public class ConfigurableCassandraClusterFactoryBean extends CassandraClusterFactoryBean {

    ConfigurableCassandraClusterFactoryBean(CassandraProperties props) {
        initProperties(props, props.getContactPoints().get(0));
    }

    private void initProperties(CassandraProperties props, String contactPoints) {
        setClusterName(props.getClusterName());
        setPort(props.getPort());
        setContactPoints(contactPoints);

        if (nonNull(props.getUsername())) {
            setUsername(props.getUsername());
            setPassword(props.getPassword());
        }
        setSocketOptions(getSocketOptions(props));
        getQueryOptions(props);
        setSslEnabled(props.isSsl());
    }

    private QueryOptions getQueryOptions(CassandraProperties properties) {
        QueryOptions options = new QueryOptions();
        if (properties.getConsistencyLevel() != null) {
            options.setConsistencyLevel(properties.getConsistencyLevel());
        }
        if (properties.getSerialConsistencyLevel() != null) {
            options.setSerialConsistencyLevel(properties.getSerialConsistencyLevel());
        }
        options.setFetchSize(properties.getFetchSize());
        return options;
    }

    private SocketOptions getSocketOptions(CassandraProperties properties) {
        SocketOptions options = new SocketOptions();
        options.setConnectTimeoutMillis(Long.valueOf(properties.getConnectTimeout().toMillis()).intValue());
        options.setReadTimeoutMillis(Long.valueOf(properties.getReadTimeout().toMillis()).intValue());
        return options;
    }

}
