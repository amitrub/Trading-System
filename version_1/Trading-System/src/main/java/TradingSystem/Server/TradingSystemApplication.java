package TradingSystem.Server;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
@RestController
@EnableJpaRepositories
public class TradingSystemApplication {

	@Autowired
	public static TradingSystem tradingSystem;

	public static void main(String[] args) {
		Properties props = createProps();
		if(props!= null){
			new SpringApplicationBuilder(TradingSystemApplication.class)
					.properties(props).run(args);
		}
//		SpringApplication.run(TradingSystemApplication.class, args);
	}

	private static Properties createProps(){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String path = "src/main/resources/initialization_System.json";
			File file = new File(path);
			String absolutePath = file.getAbsolutePath();
			JsonInitReader readJson = objectMapper.readValue(new File(absolutePath), JsonInitReader.class);
			Map map = objectMapper.readValue(new File(absolutePath), Map.class);

			Properties props = new Properties();
			props.put("spring.datasource.url", readJson.getUrl());
			props.put("spring.datasource.username", readJson.getUserName());
			props.put("spring.datasource.password", readJson.getPassword());
			props.put("spring.jpa.hibernate.ddl-auto", readJson.getDdlAuto());
			props.put("spring.jpa.show-sql", readJson.getShowSql());
			props.put("spring.jpa.properties.hibernate.dialect", readJson.getDialect());
			props.put("spring.jpa.properties.hibernate.format_sql", readJson.getFormatSql());

			return props;
		}
		catch (Exception e){
			System.out.println(e);
			return null;
		}
	}

	@GetMapping
	public String hello(){ return "Hello Trading-System"; }

}
