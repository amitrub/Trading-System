package User;

import TradingSystem.Client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class UserTests {

    Client client;


    @BeforeEach
    void setUp() {
        client = new Client();
    }

    @Test
    void registerHappy() {
        int respondID = client.Register("Roee", "1234");
//        todo: assert and all this sheet
    }

    @Test
    void registerBad() {
    }

    @Test
    void registerSad() {
    }
}
