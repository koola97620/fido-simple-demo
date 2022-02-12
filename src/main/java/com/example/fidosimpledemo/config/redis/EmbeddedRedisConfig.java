package com.example.fidosimpledemo.config.redis;


import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;

@Profile("!test")
@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        if (redisServer == null || !redisServer.isActive()) {
            System.out.println("Redis Start");
            redisServer = RedisServer.builder()
                    .port(redisPort)
                    .setting("maxmemory 128M")  // 윈도우에서만 설정 필요
                    .build();
        }
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() throws Exception {
        Optional.ofNullable(redisServer).ifPresent(RedisServer::stop);
    }

}
