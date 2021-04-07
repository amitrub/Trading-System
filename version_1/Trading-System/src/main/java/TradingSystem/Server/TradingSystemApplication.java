package TradingSystem.Server;

import TradingSystem.Server.DomainLayer.StoreComponent.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class TradingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingSystemApplication.class, args);
	}

	@GetMapping
	public String hello(){ return "Hello Trading-System"; }

}
