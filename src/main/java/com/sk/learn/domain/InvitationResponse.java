package com.sk.learn.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
public class InvitationResponse {

    private String invitationType;
    private String invitationDate;
    private String invitationMessage;
    private String invitationTo;
    private String invitationFrom;
    private Address venueAddress;
    private String invitationId;
}
