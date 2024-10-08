package com.msp.salescrm.Service;

import com.msp.salescrm.Dto.EmailDetails;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public interface EmailService {


    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);

    List<EmailDetails> readNewMail() throws MessagingException, IOException;

//    String  readNewMail();
}
