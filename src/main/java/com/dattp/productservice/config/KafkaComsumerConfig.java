package com.dattp.productservice.config;

import java.util.HashMap;
import java.util.Map;

//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.config.KafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.support.converter.StringJsonMessageConverter;
//import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.dattp.productservice.dto.kafka.BookingRequestKafkaDTO;
;
//@EnableKafka
@Configuration
public class KafkaComsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;
    
    
//    public Map<String, Object> comsumerConfigJSON(){
//        Map<String,Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        return props;
//    }
//    // booking
//    @Bean
//    public ConsumerFactory<String,BookingRequestKafkaDTO> consumerFactoryBooking(){
//        JsonDeserializer<BookingRequestKafkaDTO> jsonDeserializernew = new JsonDeserializer<>(BookingRequestKafkaDTO.class, false);
//        jsonDeserializernew.addTrustedPackages("*");
//        return new DefaultKafkaConsumerFactory<>(comsumerConfigJSON(),new StringDeserializer(), jsonDeserializernew);
//    }
//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,BookingRequestKafkaDTO>> factoryBooking(ConsumerFactory<String,BookingRequestKafkaDTO> consumerFactoryBooking){
//        ConcurrentKafkaListenerContainerFactory<String,BookingRequestKafkaDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactoryBooking);
//        return factory;
//    }
//    // string
//    public Map<String, Object> comsumerConfigString(){
//        Map<String,Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
//        // props.put(ConsumerConfig.GROUP_ID_CONFIG, "group2");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        return props;
//    }
//    @Bean
//    public ConsumerFactory<String,String> consumerFactoryString(){
//        return new DefaultKafkaConsumerFactory<>(comsumerConfigJSON());
//    }
//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> factoryString(ConsumerFactory<String,String> consumerFactoryString){
//        ConcurrentKafkaListenerContainerFactory<String,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactoryString);
//        factory.setMessageConverter(new StringJsonMessageConverter());
//        return factory;
//    }
    
}