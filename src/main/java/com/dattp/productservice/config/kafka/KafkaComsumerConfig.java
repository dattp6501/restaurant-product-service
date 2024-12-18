//package com.dattp.productservice.config.kafka;
//
//
//import com.dattp.productservice.dto.kafka.booking.BookingResponseDTO;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.config.KafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//@EnableKafka
//@Configuration
//public class KafkaComsumerConfig {
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String BOOTSTRAP_SERVERS;
//
//    @Value("${kafka.test}")
//    private boolean KAFKA_TEST;
//    @Value("${spring.kafka.properties.sasl.jaas.config}")
//    private String SASL_JAAS_CONFIG;
//    @Value("${spring.kafka.properties.sasl.mechanism}")
//    private String SASL_MECHANISM;
//    @Value("${spring.kafka.properties.security.protocol}")
//    private String SECURITY_PROTOCOL;
//    @Value("${spring.kafka.properties.sasl.trust_store_password}")
//    private String TRUST_STORE_PASSWORD;
//    //
//    public Map<String, Object> comsumerConfigJSON(){
//        Map<String,Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG,  "com.dattp.restaurant.product.id");
//        if(KAFKA_TEST){
//            props.put("sasl.jaas.config", SASL_JAAS_CONFIG);
//            props.put("sasl.mechanism", SASL_MECHANISM);
//            props.put("security.protocol", SECURITY_PROTOCOL);
//            props.put("ssl.endpoint.identification.algorithm", "");
//            props.put("ssl.truststore.type", "jks");
//            props.put("ssl.truststore.location", Objects.requireNonNull(getClass().getClassLoader().getResource("client.truststore.jks")).getPath());
//            props.put("ssl.truststore.password", TRUST_STORE_PASSWORD);
//        }
//        return props;
//    }
//    //booking
//    @Bean
//    public ConsumerFactory<String, BookingResponseDTO> consumerFactoryBooking(){
//        JsonDeserializer<BookingResponseDTO> jsonDeserializernew = new JsonDeserializer<>(BookingResponseDTO.class, false);
//        jsonDeserializernew.addTrustedPackages("*");
//        return new DefaultKafkaConsumerFactory<>(comsumerConfigJSON(),new StringDeserializer(), jsonDeserializernew);
//    }
//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,BookingResponseDTO>> factoryBooking(ConsumerFactory<String,BookingResponseDTO> consumerFactoryBooking){
//        ConcurrentKafkaListenerContainerFactory<String,BookingResponseDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactoryBooking);
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
//        return factory;
//    }
//}