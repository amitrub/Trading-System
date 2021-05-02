package TradingSystem.Acceptence_test;

import TradingSystem.Client.Client_Driver;
import TradingSystem.Client.Client_Interface;

public abstract class ProjectTest {

    private Client_Interface bridge;

    public void setUp() {
        this.bridge = Client_Driver.getClient();
    }
}
