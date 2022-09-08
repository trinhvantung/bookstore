package vn.trinhtung.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import vn.trinhtung.dto.EmailForgotPasswordDto;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Bean
	ConsumerFactory<String, EmailForgotPasswordDto> consumerUserFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_1");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, EmailForgotPasswordDto> emailForgotPasswordContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, EmailForgotPasswordDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerUserFactory());

		return factory;
	}

}
