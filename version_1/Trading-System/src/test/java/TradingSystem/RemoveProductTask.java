package TradingSystem;

import TradingSystem.Client.Client;

import java.util.concurrent.Callable;

import static TradingSystem.Server.ServiceLayer.Configuration.*;
import static TradingSystem.Server.ServiceLayer.Configuration.ANSI_GREEN;

public class RemoveProductTask implements Callable<Result> {

    private String name;
    private Client client;
    private int storeID;
    private int productID;

    public RemoveProductTask(String name, Client client, int storeID, int productID) {
        this.name = name;
        this.client = client;
        this.storeID = storeID;
        this.productID = productID;
        System.out.printf(ANSI_YELLOW + "%s: Trying connect to system... ", this.name);
        this.client.connectSystem();
        if(this.client.getConnID().equals(""))
            System.out.println("Failed!!");
        else
            System.out.printf("Connection succeed! connID=%s", this.client.getConnID());
        System.out.println(ANSI_RESET);
    }

    @Override
    public Result call() throws Exception {
        //Register parallel check
        System.out.printf(ANSI_GREEN + "%s: Staring Register...\n", this.name);
        int id = client.Register(this.name, "1234");
        System.out.printf(ANSI_GREEN + "%s: Finish Register: id=%d connID=%s\n", this.name, id, this.client.getConnID());
        System.out.println(ANSI_RESET);

        //Prepare

        return null;

    }
}
