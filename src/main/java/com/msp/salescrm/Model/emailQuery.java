package com.msp.salescrm.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "email_query")
public class emailQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "token_id",nullable = false, unique = true)
    private String tokenId;
    @Column(name = "from_email")
    private String fromEmail;
    @Column(name = "subject")
    private String subject;
    @Column(name = "content")
    private String content;
    @Column(name = "assignstatus")
    private String assignStatus;
    @Column(name = "date")
    private String date;
    private String status;
    private String timestamp;
}
