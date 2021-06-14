package TradingSystem.Server.Unit_tests;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class myTest {

    @Autowired
    TradingSystemImpl tradingSystem;

    @Test
    public void name() {
        System.out.println("----------TEST----------");
    }
}
