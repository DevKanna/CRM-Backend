package com.msp.salescrm.Service;

import com.msp.salescrm.Model.crmUsers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    public Map registerNewuser(crmUsers us) {
        log.info(" Create New user ");
        long time = System.currentTimeMillis();
        Map user = new HashMap<>();

        us.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


        long time1 = System.currentTimeMillis() - time;

        user.put("Request Time", time);
        user.put("Responese Time", time1);
        return user;
    }
}
