package com.sk.learn.domain;

import com.datastax.driver.core.DataType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Created by sk on 28/10/18.
 */
@Data
@Table("invitation")
@EqualsAndHashCode(doNotUseGetters = true, of = {"invitationId"})
public class Invitation {

    @PrimaryKeyColumn(name = "invitation_date", ordinal = 1, type = PrimaryKeyType.PARTITIONED,
            ordering = Ordering.DESCENDING)
    @CassandraType(type = DataType.Name.DATE)
    private LocalDate invitationDate;

    @PrimaryKeyColumn(name = "invitation_to", ordinal = 2, type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING)
    @CassandraType(type = DataType.Name.TEXT)
    private String invitationTo;

    @PrimaryKeyColumn(name = "invitation_id", ordinal = 3, type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING)
    @CassandraType(type = DataType.Name.UUID)
    private UUID invitationId;

    @CassandraType(type = DataType.Name.TEXT)
    @Column("invitation_type")
    private String invitationType;

    @CassandraType(type = DataType.Name.TEXT)
    @Column("invitation_message")
    private String invitationMessage;

    @CassandraType(type = DataType.Name.TEXT)
    @Column("invitation_from")
    private String invitationFrom;

    @CassandraType(type = DataType.Name.UDT, userTypeName = "address_type")
    @Column("address")
    private Address address;

}
