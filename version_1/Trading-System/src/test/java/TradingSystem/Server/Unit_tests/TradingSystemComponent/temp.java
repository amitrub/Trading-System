package TradingSystem.Server.Unit_tests.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class temp {

    @Autowired
    TradingSystemImplRubin tradingSystem;

    @Before
    public void setUp() throws Exception {
        Integer test = 1;
    }

    @Test
    public void name() {
        System.out.println("---------------");
    }
}
