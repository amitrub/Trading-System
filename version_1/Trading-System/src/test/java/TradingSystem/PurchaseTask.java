package TradingSystem;

import TradingSystem.Client.Client;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;

import static TradingSystem.Server.ServiceLayer.Configuration.ANSI_GREEN;
import static TradingSystem.Server.ServiceLayer.Configuration.ANSI_RESET;
import static TradingSystem.Server.ServiceLayer.Configuration.ANSI_YELLOW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PurchaseTask implements Callable<Result> {

    private String name;
    private Client client;
    private int storeID;
    private int productID;
    private String creditCard;
    private String phoneNumber;
    private String adress;

    public PurchaseTask(String name, int storeID, int productID, String creditCard, String phoneNumber, String adress) {
        this.name = name;
        this.client = new Client();
        this.storeID = storeID;
        this.productID = productID;
        this.creditCard = creditCard;
        this.phoneNumber = phoneNumber;
        this.adress = adress;
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
        client.addProductToCart(storeID, productID, 1);

//
//        List<DummyProduct> dummyProductList= client.showShoppingCart();
//        int sizeCart = dummyProductList.size();
//        String productName = dummyProductList.get(0).getProductName();
//        System.out.println("!!!!!!!-------- shopping cart size: " + sizeCart);
//        System.out.println("!!!!!!!-------- productName: " + productName);
//
//        if(sizeCart != 1)
//            System.out.println("Error!!! purchase task");
//        String ans1 = client.showShoppingCart().get(0).getProductName();


        Response response = client.guestPurchase(this.name, this.creditCard, this.phoneNumber, this.adress);
        System.out.println(client.getUserName() + ": (Purchase task parallel) response: " + response);
//
//        List<DummyProduct> cartAfter = client.showShoppingCart();
//        List<DummyProduct> productsAfter = client.showStoreProducts(storeID);
//        DummyProduct shortPantsAfter = productsAfter.get(0);

        //Todo: result take back information to assert in tests
//        try {
//            long duration = (long) (Math.random() * 10);
//            System.out.printf("%s: Waiting %d seconds for results.\n", this.name, duration);
//            TimeUnit.SECONDS.sleep(duration);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return new Result(this.name, LocalDateTime.now().toString(), response);
    }
}
