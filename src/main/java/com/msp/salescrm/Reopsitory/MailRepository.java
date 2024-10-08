package com.msp.salescrm.Reopsitory;

import com.msp.salescrm.Model.emailQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<emailQuery, String> {

    @Query(value = "SELECT MAX(tokenId) from emailQuery")
    String findMax();
}
