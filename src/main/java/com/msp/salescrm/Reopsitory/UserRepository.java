package com.msp.salescrm.Reopsitory;

import com.msp.salescrm.Model.crmUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<String, crmUsers> {

}
