package TradingSystem;

import TradingSystem.Client.Client;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.time.LocalDateTime;
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
        System.out.printf(ANSI_YELLOW + "%s: Store Owner is trying to remove product... " + ANSI_RESET, this.name);
//        this.client.connectSystem();
//        if(this.client.getConnID().equals(""))
//            System.out.println("Failed!!");
//        else
//            System.out.printf("Connection succeed! connID=%s", this.client.getConnID());
//        System.out.println(ANSI_RESET);
    }

    @Override
    public Result call() throws Exception {
        //Prepare
        //assume owner client is register and logged in before execute call

        //Issue
        Response response = client.removeProduct(this.storeID, this.productID);
        System.out.println(client.getUserName() + ": (RemoveProduct task parallel) response: " + response);

        //Results to assert
        return new Result(this.name, LocalDateTime.now().toString(), response);

    }
}
