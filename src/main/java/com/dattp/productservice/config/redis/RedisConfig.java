package com.dattp.productservice.config.redis;


import com.dattp.productservice.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
  @Value("${spring.redis.host}")
  private String redisHost;

  @Value("${spring.redis.port}")
  private Integer redisPort;

  @Value("${spring.redis.password}")
  private String redisPassword;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(redisHost);
    configuration.setPort(redisPort);
    configuration.setPassword(redisPassword);
    return new LettuceConnectionFactory(configuration);
  }

  @Bean
  @Primary
  public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<Object, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new JSONCustomRedisSerializer<>(String.class));
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new JSONCustomRedisSerializer<>(String.class));
    return template;
  }

  private static class JSONCustomRedisSerializer<T> implements RedisSerializer<T> {
    private final Class<T> type;

    public JSONCustomRedisSerializer(Class<T> tClass) {
      this.type = tClass;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
      return JSONUtils.toByteArray(JSONUtils.toJson(t));
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
      return JSONUtils.toStringJson(bytes, type);
    }
  }
}
