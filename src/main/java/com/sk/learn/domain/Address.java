package com.sk.learn.domain;

import com.datastax.driver.core.DataType;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@Data
@UserDefinedType("address_type")
public class Address {

    @CassandraType(type = DataType.Name.TEXT)
    @Column("first_name")
    private String firstName;

    @CassandraType(type = DataType.Name.TEXT)
    @Column("middle_initial")
    private String middleInitial;

    @CassandraType(type = DataType.Name.TEXT)
    @Column("last_name")
    private String lastName;

    @CassandraType(type = DataType.Name.TEXT)
    @Column("street_name")
    private String streetName;

    @CassandraType(type = DataType.Name.TEXT)
    @Column("additional_address")
    private String additional_address;

    @CassandraType(type = DataType.Name.TEXT)
    @Column("city")
    private String city;

    @CassandraType(type = DataType.Name.TEXT)
    @Column("state")
    private String state;

    @CassandraType(type = DataType.Name.TEXT)
    @Column("zip")
    private String zip;

    @CassandraType(type = DataType.Name.TEXT)
    @Column("country")
    private String country;

    @CassandraType(type = DataType.Name.TEXT)
    @Column("phone")
    private String phone;
}
