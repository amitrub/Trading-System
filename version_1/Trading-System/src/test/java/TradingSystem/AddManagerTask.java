package TradingSystem;

import TradingSystem.Client.Client;
import TradingSystem.Result;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static TradingSystem.Server.ServiceLayer.Configuration.*;
import static TradingSystem.Server.ServiceLayer.Configuration.ANSI_GREEN;

public class AddManagerTask implements Callable<Result> {

    private Client client;
    private String name;
    private String pass;
    private int storeID;
    private int newManagerID;

    public AddManagerTask(String name, String pass, int storeID, int newManagerID) {
        this.name = name;
        this.pass = pass;
        this.client = new Client();
        this.storeID = storeID;
        this.newManagerID = newManagerID;
        System.out.printf(ANSI_YELLOW + "%s: Add manager task constructor... \n" + ANSI_RESET, this.name);
        System.out.printf(ANSI_YELLOW + "%s: Trying connect to system... \n", this.name);
        this.client.connectSystem();
        if(this.client.getConnID().equals(""))
            System.out.println("Failed!!");
        else
            System.out.printf("Connection succeed! connID=%s", this.client.getConnID());
        System.out.println(ANSI_RESET);
    }

    @Override
    public Result call() throws Exception {
        //Prepare
        //Register parallel check
        System.out.printf(ANSI_GREEN + "Staring Login... %s %s\n", this.name, this.pass);
        int id = client.Login(this.name, this.pass);
        System.out.printf(ANSI_GREEN + "%s: Finish Login: id=%d connID=%s\n", this.name, id, this.client.getConnID());
        System.out.println(ANSI_RESET);

        //Issue
        Response response = client.addManager(this.storeID, this.newManagerID);
        System.out.println(client.getUserName() + ": (Add manager task parallel) response: " + response);

        //Results to assert
        return new Result(this.name, LocalDateTime.now().toString(), response);
    }
}
