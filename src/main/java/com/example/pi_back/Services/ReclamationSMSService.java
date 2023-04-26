package com.example.pi_back.Services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
@Service
@NoArgsConstructor
public class ReclamationSMSService {
    public String sid ="AC508d45d3fdb1af9e9455d645e6b4b078";
    public String authid ="10d418064d2665a50348180484ed345e";

    public String outNumber ="+44 7700 162943";

    public String ip="";
    @PostConstruct
    private void setup(){
        Twilio.init(sid,authid);
    }
    public String sendSMS(String number, String smsmessage) throws MalformedURLException {


        Message message = Message.creator(
                new PhoneNumber(number),
                new PhoneNumber(outNumber),
                smsmessage).create();
        return message.getStatus().toString();


    }
}