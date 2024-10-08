package com.msp.salescrm.Config;

import com.msp.salescrm.Dto.EmailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;

@Configuration
public class MailIntegrationConfig {

//    @Bean
//    public IntegrationFlow mainIntegration(EmailProperties props) {
//        System.out.println("props = " + props);
//        System.out.println("Props Test : " + props.getImapUrl());
//
//        return IntegrationFlow
//                .from(
//
//                        Mail.imapInboundAdapter(props.getImapUrl())
//                                .shouldDeleteMessages(false)
//                                .simpleContent(true)
//                                .autoCloseFolder(false),
//                        e -> e.poller(
//                                Pollers.fixedDelay(props.getPollRate())
//                                        .errorHandler(t -> System.err.println("Polling error: " + t.getMessage()))
//                        )
//                )
//                .handle(message -> {
//                    System.out.println("New message received: " + message);
//                })
//                .get();
//    }
}
