package ru.restvoting.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.restvoting.web.json.JsonUtil;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
@EnableCaching
// TODO: cache only most requested data!
public class AppConfig {

    @Profile("!test")
    @Bean(initMethod = "start", destroyMethod = "stop")
    Server h2Server() throws SQLException {
        log.info("Start H2 TCP server");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    //    https://stackoverflow.com/a/46947975/548473
    @Bean
    Module module() {
        return new Hibernate5Module();
    }

    @Autowired
    public void storeObjectMapper(ObjectMapper objectMapper) {
        JsonUtil.setMapper(objectMapper);
    }

    @Bean
    public CaffeineCache restaurants() {
        return new CaffeineCache("restaurants",
                Caffeine.newBuilder()
                        .maximumSize(30)
                        .expireAfterAccess(12, TimeUnit.HOURS)
                        .build());
    }

    @Bean
    public CaffeineCache votes() {
        return new CaffeineCache("voting",
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterAccess(3, TimeUnit.MINUTES)
                        .build());
    }

    @Bean
    public CaffeineCache menus() {
        return new CaffeineCache("todayMenus",
                Caffeine.newBuilder()
                        .maximumSize(30)
                        .expireAfterAccess(12, TimeUnit.HOURS)
                        .build());
    }
}
