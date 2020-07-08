package com.sk.learn.mapper;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InvitationMapperTest {

    InvitationMapper invitationMapper;

    @Test
    public void validateDate () {
        String dateStr = "2020-07-16";
        LocalDate date = InvitationMapper.getLocalDateFromString(dateStr);
        assertNotNull(date);

    }
}
