package com.msp.salescrm.Service;

import com.msp.salescrm.Dto.EmailDetails;
import com.msp.salescrm.Dto.mailStatus;
import com.msp.salescrm.Model.emailQuery;
import com.msp.salescrm.Reopsitory.MailRepository;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.FlagTerm;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
//    @Override
//    public String sendSimpleMail(EmailDetails details) {
//        return null;
//    }
//
//    @Override
//    public String sendMailWithAttachment(EmailDetails details) {
//        return null;
//    }

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Autowired
    MailRepository mail;

    @Value("${spring.mail.username}")
    private String sender;


    private final static String StatusOpen = "OPEN";
    private final static String StatusClose = "CLOSE";

    // Method 1
    // To send a simple email
    public String sendSimpleMail(EmailDetails details) {

        log.info("Send Mail Method Call");
        // Try block to check for exceptions
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            System.out.println("sender = " + sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            log.info("Mail Sent Successfully...");
            return "Mail Sent Successfully...";

        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            return "Error while Sending Mail" + e.getMessage();
        }
    }

    // Method 2
    // To send an email with attachment
    public String sendMailWithAttachment(EmailDetails details) {
        // Creating a mime message
        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(
                    details.getSubject());

            // Adding the attachment
            FileSystemResource file
                    = new FileSystemResource(
                    new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(
                    file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        // Catch block to handle MessagingException
        catch (MessagingException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            // Display message when exception occurred
            return "Error while sending mail!!!" + e.getMessage();
        }
    }


    // Read the New UnRead mail Every 30 Seconds
    public List<EmailDetails> readNewMail() throws MessagingException, IOException {
        log.info("EMAIL READ METHOD CALL!!!");
        List<EmailDetails> email = new ArrayList<>();
//
//        String username = "mailtokannan1112m@gmail.com";
//        String password = "hwyg xrin mzwo hjai";
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", env.getProperty("spring.mail.properties.mail.imap.host"));
        props.put("mail.imaps.port", env.getProperty("spring.mail.properties.mail.imap.port"));
        props.put("mail.imaps.ssl.enable", "true");
        props.put("mail.imaps.ssl.trust", "*");
        props.put("mail.imaps.ssl.protocols", "TLSv1.2");
//        Session session = Session.getInstance(props);

        String host = env.getProperty("spring.mail.properties.mail.imap.host");
//        System.out.println("host = " + host);
        String username = env.getProperty("spring.mail.username");
//        System.out.println("username = " + username);
        String password = env.getProperty("spring.mail.password");
//        System.out.println("password = " + password);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Store store = session.getStore("imaps");
        if (host == null || username == null || password == null) {
            throw new MessagingException("Mail configuration is incomplete. Please check your application.properties.");
        }

        store.connect(props.getProperty("spring.mail.properties.mail.imap.host"),
                props.getProperty("spring.mail.username"),
                props.getProperty("spring.mail.password"));

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);

        Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        if (messages.length > 0) {
            log.info("New Message Arrived");
            for (Message message : messages) {
                EmailDetails em = new EmailDetails();

                String subject = message.getSubject();
                System.out.println("Subject: " + subject);
                em.setSubject(message.getSubject());

                String MsgBody = getTextFromMessage(message);
                System.out.println("Content: " + MsgBody);
                em.setMsgBody(getTextFromMessage(message));

                String recipient = getEmailAddress(message.getFrom()[0]);
                System.out.println("From: " + recipient);
                em.setRecipient(recipient);


                String SentDate = String.valueOf(message.getSentDate());
                System.out.println("Date: " + SentDate);

                // Here I used Attachement for getting sentDate
                em.setAttachment(SentDate);
                System.out.println("--------------------------------------------------------------");
                message.setFlag(Flags.Flag.SEEN, true);
                System.out.println(" EMAIL DETAILS : " + em);
                email.add(em);
            }
        } else {
            log.info("No New Message ");
        }

        inbox.close(false);
        store.close();
        if (!email.isEmpty()) {
            System.out.println(" Email Size= " + email.size());
            try {
                saveAllmail(email);

            } catch (Exception e) {
                System.err.println("Error occur : " + e.getMessage());
            }

//            System.out.println("TESTING 123 : " + email);
        }
//        session.setDebug(true);

        return email;
    }

    @Override
    public Map OpenMailStatus(mailStatus s) {
        log.info("Open Status Mail method ");
        long time = System.currentTimeMillis();
        Map op = new HashMap<>();
        List<emailQuery> emailList = mail.findByStatus(s.getStatus().name());
        long time1 = System.currentTimeMillis() - time;
        if (emailList.size() > 0) {

            op.put("EMAIL", emailList);
        } else {
            op.put("EMAIL", "NO EMAIL");
        }
        op.put("Request Time", time);
        op.put("Responese Time", time1);

        return op;
    }


    @Transactional
    protected void saveAllmail(List<EmailDetails> emailList) {
        log.info("SAVE EMAIL DETAILS AND GENERATE TOKEN");
        List<emailQuery> em = new ArrayList<>();
        for (EmailDetails email : emailList) {
            emailQuery emailSave = new emailQuery();
            String token = generateTokenId();
            System.out.println("token = " + token);
            emailSave.setTokenId(token);
            emailSave.setFromEmail(email.getRecipient());
            emailSave.setSubject(email.getSubject());
            emailSave.setContent(email.getMsgBody());
            emailSave.setDate(email.getAttachment());
            emailSave.setStatus(StatusOpen);
            emailSave.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            System.out.println("emailSave = " + emailSave);
            em.add(emailSave);

            email.setAttachment("");

            email.setMsgBody(mailContent(token, email.getRecipient()));
            sendSimpleMail(email);
        }
        mail.saveAll(em);
    }

    private String generateTokenId() {
        String to = mail.findMax();
        if (to == null) {
            to = "1000001";
        } else {
            int max = Integer.parseInt(to);
            to = String.valueOf(++max);
        }
        System.out.println("to = " + to);
        return to;
    }


    private String getEmailAddress(Address address) {
        if (address instanceof InternetAddress) {
            return ((InternetAddress) address).getAddress();
        }
        return address.toString();
    }

    private String mailContent(String tok, String ma) {
        StringBuilder cont = new StringBuilder();
//        cont.append()

        String name = ma.split("@")[0];

        cont.append("Dear ").append(name).append(",").append("\n\n")
                .append("Thank you for contacting CRM . We hope you are doing well.")
                .append("\n\n")
                .append("We have received your support request and it is currently being reviewed by our support staff. ")
                .append("To track the status of your request, please refer to your ticket ID: ").append(tok).append(".")
                .append("\n\n")
                .append("Please note that this is an automated acknowledgment and no further replies are required. ")
                .append("For any urgent concerns, feel free to revert to the initial email.")
                .append("\n\n")
                .append("Please allow us sometime to review and respond to your request. Your patience is highly appreciated in this regard.")
                .append("\n\n")
                .append("Regards,").append("\n\n")
                .append("MSP CRM");
        return String.valueOf(cont);
    }


    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }


    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
                break;
            }
//                result.append("\n").append(bodyPart.getContent());
//            } else if (bodyPart.isMimeType("text/html")) {
//                String html = (String) bodyPart.getContent();
//                result.append("\n").append(html);
            else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString().trim();
    }


}
