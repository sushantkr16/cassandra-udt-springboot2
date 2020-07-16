package com.sk.learn.repository.impl;

import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.sk.learn.domain.Invitation;
import com.sk.learn.repository.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.datastax.driver.core.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.sk.learn.mapper.InvitationMapper.getStringFromLocalDate;


@Repository
public class InvitationRepositoryImpl implements InvitationRepository {

    private static final String TBL_INVITATION = "invitation";

    private static final String COL_INVITATION_TO = "invitation_to";

    private static final String COL_INVITATION_ID = "invitation_id";

    private static final String COL_INVITATION_DATE = "invitation_date";

    @Autowired
    private CassandraOperations cassandraOperations;

    @Override
    public Invitation createInvitation(Invitation invitation) {
       return cassandraOperations.insert(invitation);
    }

    @Override
    public Invitation updateInvitation(Invitation invitation) {
        return cassandraOperations.update(invitation);
    }

    @Override
    public void deleteAllInvitationsForSpecificGuest(String invitationDate, String guestName) {
        Delete delete = QueryBuilder.delete()
                .from("invitation")
                .where(eq(COL_INVITATION_DATE, invitationDate))
                .ifExists();

        cassandraOperations.delete(delete);
    }

    @Override
    public List<Invitation> getAllInvitationsForSpecificGuest(LocalDate invitationDate, String guestName) {
        Statement statement = QueryBuilder.select().all()
                .from(TBL_INVITATION)
                .where(eq(COL_INVITATION_DATE, invitationDate.toString()))
                .and(eq(COL_INVITATION_TO, guestName));

        List<Invitation> invitationList = cassandraOperations.select(statement.toString(), Invitation.class);
        return invitationList;
    }

    @Override
    public Invitation getInvitationForTheGuest(LocalDate invitationDate, String guestName, UUID invitationId) {
        Statement statement = QueryBuilder.select().all()
                .from(TBL_INVITATION)
                .where(eq(COL_INVITATION_DATE, invitationDate.toString()))
                .and(eq(COL_INVITATION_TO, guestName))
                .and(eq(COL_INVITATION_ID, invitationId));

        Invitation invitation = cassandraOperations.selectOne(statement.toString(), Invitation.class);
        return invitation;
    }

    @Override
    public List<Invitation> getAllInvitationByDate(LocalDate invitationDate) {
        Statement statement = QueryBuilder.select().all()
                .from(TBL_INVITATION)
                .where(eq(COL_INVITATION_DATE, invitationDate.toString()));

        List<Invitation> invitationList = cassandraOperations.select(statement.toString(), Invitation.class);
        return invitationList;
    }
}
