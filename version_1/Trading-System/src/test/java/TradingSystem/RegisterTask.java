package TradingSystem;

import TradingSystem.Client.Client;


import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

public class RegisterTask implements Callable<Result> {

    private String name;
    private Client client;


    public RegisterTask(String name) {
        this.name = name;
        this.client = new Client();
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

        System.out.printf(ANSI_GREEN + "%s: Staring Register...\n", this.name);
        int id = client.Register(this.name, "1234");
        System.out.printf(ANSI_GREEN + "%s: Finish Register: id=%d connID=%s\n", this.name, id, this.client.getConnID());
        System.out.println(ANSI_RESET);
//        try {
//            long duration = (long) (Math.random() * 10);
//            System.out.printf("%s: Waiting %d seconds for results.\n", this.name, duration);
//            TimeUnit.SECONDS.sleep(duration);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return new Result(this.name, LocalDateTime.now().toString());    }
}
