package ru.skillbox.authentication.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import ru.skillbox.authentication.entity.RefreshToken;


import java.time.Duration;
import java.util.Collections;


@Configuration
@EnableRedisRepositories(keyspaceConfiguration = RedisConfig.RefreshTokenKeyspaceConfig.class,
enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfig {

    @Value("${app.jwt.refreshToken}")
    private Duration refreshToken;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());

        return new JedisConnectionFactory(configuration);

    }

    public class RefreshTokenKeyspaceConfig extends KeyspaceConfiguration {

        private static final String REFRESH_TOKEN_KEYSPACE = "refresh_tokens";

        @Override
        protected Iterable<KeyspaceSettings> initialConfiguration() {
            KeyspaceSettings keyspaceSettings = new KeyspaceSettings(RefreshToken.class, REFRESH_TOKEN_KEYSPACE);
            keyspaceSettings.setTimeToLive(refreshToken.getSeconds());


            return Collections.singleton(keyspaceSettings);
        }
    }
}
