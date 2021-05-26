package TradingSystem.Server.DomainLayer.TradingSystemComponent.Task;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Services.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SpringTest {

    @Autowired
    StoreService service;
    static public int id;

    @BeforeAll
    public static void init(@Autowired StoreService service) {
        id = service.Addstore(new DataStore("reut",5.0,1));
    }

    //region search tests - requirement 2.6
    @Test
    void searchProduct() {
        DataStore dataStore= service.findStorebyId(id);
       Assertions.assertEquals(dataStore.getName(),"reut");
    }
}
