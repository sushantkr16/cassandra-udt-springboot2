package com.sk.learn.repository;

import com.sk.learn.domain.Invitation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface InvitationRepository {

    Invitation createInvitation(Invitation invitation);
    Invitation updateInvitation(Invitation invitation);
    void deleteAllInvitationsForSpecificGuest(String invitationDate, String guestName);
    List<Invitation> getAllInvitationByDate(LocalDate invitationDate);
    List<Invitation> getAllInvitationsForSpecificGuest(LocalDate invitationDate, String guestName);
    Invitation getInvitationForTheGuest(LocalDate invitationDate, String guestName, UUID invitationId);
}
