package com.sk.learn.service.impl;

import com.sk.learn.domain.Invitation;
import com.sk.learn.domain.InvitationRequest;
import com.sk.learn.domain.InvitationResponse;
import com.sk.learn.mapper.InvitationMapper;
import com.sk.learn.repository.InvitationRepository;
import com.sk.learn.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.sk.learn.mapper.InvitationMapper.getLocalDateFromString;

/**
 * Created by sk on 28/10/18.
 */
@Service
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Override
    public InvitationResponse createInvitation(InvitationRequest invitationRequest) {

        // map request to domain
        Invitation invitationDomain = InvitationMapper.toDomainFromRequest(invitationRequest);
        invitationDomain.setInvitationId(UUID.randomUUID());

        Invitation updatedInvitationDomain = invitationRepository.createInvitation(invitationDomain);

        // map domain to response
        InvitationResponse invitationResponse = InvitationMapper.toResponseFromDomain(updatedInvitationDomain);
        return invitationResponse;
    }

    @Override
    public InvitationResponse updateInvitation(InvitationRequest invitationRequest, String invitationId) {

        LocalDate invitationDate = getLocalDateFromString(invitationRequest.getInvitationDate());
        Invitation invitation = invitationRepository.getInvitationForTheGuest(invitationDate,
                invitationRequest.getInvitationTo(), UUID.fromString(invitationId));

        if (null != invitation) {
            // map request to domain
            Invitation invitationDomain = InvitationMapper.toDomainFromRequest(invitationRequest);
            invitationDomain.setInvitationId(UUID.fromString(invitationId));

            Invitation updatedInvitationDomain = invitationRepository.createInvitation(invitationDomain);

            // map domain to response
            InvitationResponse invitationResponse = InvitationMapper.toResponseFromDomain(updatedInvitationDomain);
            return invitationResponse;
        }
        return null;
    }

    @Override
    public void deleteInvitationByInvitationId(LocalDate invitationDate, String guestName, UUID invitationId) {
        invitationRepository.deleteInvitationByInvitationId(invitationDate, guestName, invitationId);
    }

    @Override
    public void deleteAllInvitationsForSpecificGuest(LocalDate invitationDate, String guestName) {
        invitationRepository.deleteAllInvitationsForSpecificGuest(invitationDate, guestName);
    }

    @Override
    public List<InvitationResponse> getAllInvitationByDate(LocalDate invitationDate) {
        List<Invitation> invitationList = invitationRepository.getAllInvitationByDate(invitationDate);

        // map domain to response
        List<InvitationResponse> invitationResponseList = InvitationMapper.toResponseListFromDomain(invitationList);
        return invitationResponseList;
    }

    @Override
    public List<InvitationResponse> getAllInvitationsForSpecificGuest(LocalDate invitationDate, String guestName) {
        List<Invitation> invitationList = invitationRepository.getAllInvitationsForSpecificGuest(invitationDate, guestName);

        // map domain to response
        List<InvitationResponse> invitationResponseList = InvitationMapper.toResponseListFromDomain(invitationList);
        return invitationResponseList;
    }

    @Override
    public InvitationResponse getInvitationForTheGuest(java.time.LocalDate invitationDate, String guestName, UUID invitationId) {
        Invitation invitation = invitationRepository.getInvitationForTheGuest(invitationDate, guestName, invitationId);

        // map domain to response
        InvitationResponse invitationResponse = InvitationMapper.toResponseFromDomain(invitation);
        return invitationResponse;
    }
}
