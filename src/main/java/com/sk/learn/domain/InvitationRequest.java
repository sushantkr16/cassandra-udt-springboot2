package com.sk.learn.domain;

import lombok.Data;

/**
 * Created by sk on 28/10/18.
 */

@Data
public class InvitationRequest {

    private String invitationType;
    private String invitationDate;
    private String invitationMessage;
    private String invitationTo;
    private String invitationFrom;
    private Address venueAddress;

}
