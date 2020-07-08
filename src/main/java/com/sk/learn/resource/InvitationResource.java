package com.sk.learn.resource;

import com.sk.learn.domain.InvitationRequest;
import com.sk.learn.domain.InvitationResponse;
import com.sk.learn.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.sk.learn.mapper.InvitationMapper.getLocalDateFromString;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/invitation")
public class InvitationResource {

    @Autowired
    InvitationService invitationService;

    @PostMapping
    public ResponseEntity createInvitation(@RequestBody InvitationRequest invitationRequest) {

        InvitationResponse invitationResponse = invitationService.createInvitation(invitationRequest);

        if (null != invitationResponse) {
            return new ResponseEntity(invitationResponse, OK);
        } else {
            return new ResponseEntity("Issue while writing invitationRequest message", BAD_REQUEST);
        }
    }

    @PutMapping("/{invitationId}")
    public ResponseEntity updateInvitation(@RequestBody InvitationRequest invitationRequest, @PathVariable String invitationId) {

        InvitationResponse invitationResponse = invitationService.updateInvitation(invitationRequest, invitationId);

        if (null != invitationResponse) {
            return new ResponseEntity(invitationResponse, OK);
        } else {
            return new ResponseEntity("Issue while writing invitationRequest message", BAD_REQUEST);
        }
    }

    @GetMapping("/{invitationGuestName}")
    public ResponseEntity getInvitationForTheGuest(@RequestParam String invitationDate, @PathVariable String invitationGuestName) {
        LocalDate invitationLocalDate = getLocalDateFromString(invitationDate);
        List<InvitationResponse> invitationResponseList = invitationService.getAllInvitationsForSpecificGuest(invitationLocalDate, invitationGuestName);

        if (!CollectionUtils.isEmpty(invitationResponseList)) {
            return new ResponseEntity(invitationResponseList, OK);
        } else {
            return new ResponseEntity("No invitation exists", OK);
        }
    }

    @GetMapping()
    public ResponseEntity getAllInvitationsByDate(@RequestParam String invitationDate) {

        LocalDate invitationLocalDate = getLocalDateFromString(invitationDate);
        List<InvitationResponse> invitationResponseList = invitationService.getAllInvitationByDate(invitationLocalDate);

        if (!CollectionUtils.isEmpty(invitationResponseList)) {
            return new ResponseEntity(invitationResponseList, OK);
        } else {
            return new ResponseEntity("No invitation exists", OK);
        }
    }

    //TODO Fix delete api as its not working
    @DeleteMapping("/{invitationGuestName}")
    public ResponseEntity deleteInvitations(@RequestParam String invitationDate, @PathVariable String invitationGuestName) {
        LocalDate invitationLocalDate = getLocalDateFromString(invitationDate);
        invitationService.deleteAllInvitationsForSpecificGuest(invitationLocalDate, invitationGuestName);
        return new ResponseEntity(OK);
    }

}
