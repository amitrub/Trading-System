package TradingSystem.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@RestController
@EnableJpaRepositories
public class TradingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingSystemApplication.class, args);
	}

	@GetMapping
	public String hello(){ return "Hello Trading-System"; }

}
