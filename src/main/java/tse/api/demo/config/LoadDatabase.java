package tse.api.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tse.api.demo.model.Security;
import tse.api.demo.model.User;
import tse.api.demo.repository.OrderRepository;
import tse.api.demo.repository.SecurityRepository;
import tse.api.demo.repository.TradeRepository;
import tse.api.demo.repository.UserRepository;
import tse.api.demo.utils.constants.ExCon;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository us,
                                   SecurityRepository sec,
                                   OrderRepository or,
                                   TradeRepository tr
    ) {

        return args -> {
            log.info("Preloading " + us.save(new User("userFromPreloading1", ExCon.PWD)));
            log.info("Preloading " + us.save(new User("userFromPreloading2")));
            log.info("Preloading " + sec.save(new Security("secFromPreloading1")));
            log.info("Preloading " + sec.save(new Security("secFromPreloading2")));
        };
    }
}