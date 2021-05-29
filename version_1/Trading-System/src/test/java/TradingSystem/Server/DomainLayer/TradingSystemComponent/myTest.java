package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class myTest {

    @Autowired
    TradingSystem tradingSystem;

    @BeforeAll
    public static void init(@Autowired TradingSystem tradingSystem){
        System.out.println("+++++++++++++++++++++++++++++++");
        System.out.println(tradingSystem);

    }

    @Test
    public void test1(){
        System.out.println("------------------------");
        System.out.println(tradingSystem);
        Assertions.assertEquals(1,1);
    }
}
