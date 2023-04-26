package com.example.pi_back.Services;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SMSrequest {
    private final String smsnumber;
    private final String smsmessage;

}
