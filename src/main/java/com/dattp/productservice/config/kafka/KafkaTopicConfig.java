package com.dattp.productservice.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
  @Value("${kafka.partition}")
  public int partition;
  public static final String NEW_DISH_TOPIC = "com.dattp.restaurant.product.new_dish";
  public static final String UPDATE_DISH_TOPIC = "com.dattp.restaurant.product.update_dish";

  public static final String NEW_TABLE_TOPIC = "com.dattp.restaurant.product.new_table";
  public static final String UPDATE_TABLE_TOPIC = "com.dattp.restaurant.product.update_table";

  @Bean
  public NewTopic newDishTopic(){
    return TopicBuilder.name(NEW_DISH_TOPIC)
      .partitions(partition)
      .build();
  }
  @Bean
  public NewTopic updateDishTopic(){
    return TopicBuilder.name(UPDATE_DISH_TOPIC)
      .partitions(partition)
      .build();
  }

  @Bean
  public NewTopic newTableTopic(){
    return TopicBuilder.name(NEW_TABLE_TOPIC)
      .partitions(partition)
      .build();
  }
  @Bean
  public NewTopic updateTableTopic(){
    return TopicBuilder.name(UPDATE_TABLE_TOPIC)
      .partitions(partition)
      .build();
  }

    // booking
//    @Bean
//    public NewTopic checkOrderTopic(){
//        return TopicBuilder.name("checkOrder").build();
//    }
//    @Bean
//    public NewTopic newOrderTopic(){
//        return TopicBuilder.name("newOrder").build();
//    }
//    // dish
//    @Bean
//    public NewTopic checkBookedDishTopic(){//check info booked dish
//        return TopicBuilder.name("checkBookedDish").build();
//    }
//
//    @Bean
//    public NewTopic resultCheckBookedDishTopic(){//result check info booked dish
//        return TopicBuilder.name("resultCheckBookedDish").build();
//    }
}