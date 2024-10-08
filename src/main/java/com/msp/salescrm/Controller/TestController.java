package com.msp.salescrm.Controller;

import com.msp.salescrm.Dto.EmailDetails;
import com.msp.salescrm.Service.EmailService;
import jakarta.mail.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/test/")
public class TestController {


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;



    // Sending a simple Email
    @PostMapping("/sendMail")
    public String sendMail(@RequestBody EmailDetails details)
    {
        String status
                = emailService.sendSimpleMail(details);

        return status;
    }



    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
            @RequestBody EmailDetails details)
    {
        String status
                = emailService.sendMailWithAttachment(details);

        return status;
    }



   @Scheduled(fixedRate = 30000)
    public void retriveNewMail(
           ) throws MessagingException, IOException {
        List<EmailDetails> status
                = emailService.readNewMail();
        if (!status.isEmpty()) {
            System.out.println("status = " + status);


        }
    }










//    @RequestMapping(value = "/demo",method = RequestMethod.POST)
//    @Scheduled(fixedRate = 30000)

}
