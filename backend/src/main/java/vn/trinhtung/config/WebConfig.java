package vn.trinhtung.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mm = new ModelMapper();
		mm.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mm;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		add(registry, "image");
	}

	private void add(ResourceHandlerRegistry registry, String s) {
		Path path = Paths.get(s);
		String url = path.toFile().getAbsolutePath();
		registry.addResourceHandler("/" + s + "/**").addResourceLocations("file:/" + url + "/");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
				.allowedOrigins("http://localhost:3000", "http://localhost:3001")
				.allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
				.allowedHeaders("*");
	}
}
