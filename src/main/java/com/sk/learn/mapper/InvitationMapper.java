package com.sk.learn.mapper;

import com.sk.learn.domain.Invitation;
import com.sk.learn.domain.InvitationRequest;
import com.sk.learn.domain.InvitationResponse;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InvitationMapper {

    public static Invitation toDomainFromRequest(InvitationRequest invitationRequest) {
        if (null != invitationRequest) {
          Invitation invitation = new Invitation();
          invitation.setInvitationType(invitationRequest.getInvitationType());
          invitation.setInvitationDate(getLocalDateFromString(invitationRequest.getInvitationDate()));
          invitation.setInvitationMessage(invitationRequest.getInvitationMessage());
          invitation.setInvitationTo(invitationRequest.getInvitationTo());
          invitation.setInvitationFrom(invitationRequest.getInvitationFrom());
          invitation.setAddress(invitationRequest.getVenueAddress());
          return invitation;
        }
        return null;
    }

    public static InvitationResponse toResponseFromDomain(Invitation invitation) {
        if (null != invitation) {
            InvitationResponse invitationResponse = new InvitationResponse();
            invitationResponse.setInvitationId(invitation.getInvitationId().toString());
            invitationResponse.setInvitationType(invitation.getInvitationType());
            invitationResponse.setInvitationDate(getStringFromLocalDate(invitation.getInvitationDate()));
            invitationResponse.setInvitationMessage(invitation.getInvitationMessage());
            invitationResponse.setInvitationTo(invitation.getInvitationTo());
            invitationResponse.setInvitationFrom(invitation.getInvitationFrom());
            invitationResponse.setVenueAddress(invitation.getAddress());
            return invitationResponse;
        }
        return null;
    }

    public static List<InvitationResponse> toResponseListFromDomain(List<Invitation> invitationList) {
        List<InvitationResponse> invitationResponseList = null;
        if (!CollectionUtils.isEmpty(invitationList)) {
            invitationResponseList = new ArrayList<>();
            for (Invitation invitation: invitationList) {
                InvitationResponse invitationResponse = toResponseFromDomain(invitation);
                invitationResponseList.add(invitationResponse);
            }
        }
        return invitationResponseList;
    }

    public static LocalDate getLocalDateFromString (String dateStr) {
       return LocalDate.parse(dateStr);
    }

    public static String getStringFromLocalDate (LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}