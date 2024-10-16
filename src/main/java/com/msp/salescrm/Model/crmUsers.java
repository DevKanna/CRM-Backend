package com.msp.salescrm.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "crm_user")
public class crmUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "email",nullable = false,unique = true)
    private String userEmail;
    @Column(name = "username",nullable = false)
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
    private String timestamp;





}
