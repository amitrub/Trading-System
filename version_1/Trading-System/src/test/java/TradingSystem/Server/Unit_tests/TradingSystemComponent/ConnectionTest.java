package TradingSystem.Server.Unit_tests.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.Task.RegisterTaskUnitTests;
import TradingSystem.Server.DomainLayer.Task.ResultUnitTests;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnectionTest {

    @Autowired
    TradingSystemImplRubin tradingSystem;

    @Before
    public void setUp(){
        tradingSystem.ClearSystem();
    }

    //region requirement 2

    // requirement 2.1
    @Test
    public void connectSystem() {
        Response response= tradingSystem.ConnectSystem();
        assertTrue(response.returnConnID()!="" && response.getIsErr()==false);
    }

    // requirement 2.2
    @Test
    public void exitGood() {
        String connId= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Exit(connId);
        assertFalse(response.getIsErr());
    }

    // requirement 2.2
    @Test
    public void exitBad() {
        String connId= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Exit(connId);
        Response response= tradingSystem.Exit(connId);
        assertTrue(response.getIsErr());
    }

    // requirement 2.3
    @Test
    public void registerGood() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID,"Elinor","123");
        assertFalse(response.getIsErr());
    }

    // requirement 2.3
    @Test
    public void registerExistUser() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID,"Elinor","123");
        Response response= tradingSystem.Register(connID,"Elinor","1234");
        assertTrue(response.getIsErr());
    }

    // requirement 2.3
    //TODO
    @Test
    public void registerParallelSadSameName(){
        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

        List<RegisterTaskUnitTests> taskList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            RegisterTaskUnitTests task = new RegisterTaskUnitTests("SameName");
            taskList.add(task);
        }

        //Execute all tasks and get reference to Future objects
        List<Future<ResultUnitTests>> resultList = null;

        try {
            resultList = executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        System.out.println("\n========Printing the results======");

//        assert resultList != null;
//        for (int i = 0; i < resultList.size(); i++) {
//            Future<ResultUnitTests> future = resultList.get(i);
//            try {
//                ResultUnitTests result = future.get();
//                System.out.println(result.getName() + ": " + result.getTimestamp());
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }


        boolean[] isErrs = new boolean[2];
        for (int i = 0; i < resultList.size(); i++) {
            Future<ResultUnitTests> future = resultList.get(i);
            try {
                ResultUnitTests result = future.get();
                Response response = result.getResponse();
                System.out.println("Assert correctness for " + result.getName() + ": response -> " + response + " ::" + result.getTimestamp());
                isErrs[i] = response.getIsErr();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        //Check that one of the client failed and the other succeed.
        assertTrue(!isErrs[0] && !isErrs[1] && (isErrs[0] || isErrs[1]));
    }

    // requirement 2.4
    @Test
    public void loginSuccess() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID,"Elinor","123");
        Response response= tradingSystem.Login(connID,"Elinor","123");
        assertFalse(response.getIsErr() && response.returnUserID()<0);
    }

    // requirement 2.4
    @Test
    public void loginWrongUserName() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Login(connID,"Elinor","124");
        assertTrue(response.getIsErr());
    }

    // requirement 2.4
    @Test
    public void loginWrongPassword() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID,"Elinor","123");
        Response response= tradingSystem.Login(connID,"Elinor","1234");
        assertTrue(response.getIsErr());
    }

    // requirement 2.5
    @Test
    public void showAllStoresGood() {
        Response res = tradingSystem.ShowAllStores();
        assertEquals(res.getIsErr(),false);
    }

    //endregion

    //region requirement 3

    // requirement 3.1
    @Test
    public void LogoutHappy() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID,"Rubin","123");
        Response res = tradingSystem.Login(connID,"Rubin","123");
        connID = res.returnConnID();
        Response response = tradingSystem.Logout(connID);
        assertFalse(response.getIsErr());
    }

    // requirement 3.1
    @Test
    public void LogoutSad() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        Response res = tradingSystem.Register(connID,"Rubin","123");
        connID = res.returnConnID();
        Response response = tradingSystem.Logout(connID);
        assertTrue(response.getIsErr());
    }

    // requirement 3.2
    @Test
    public void AddStoreWrongUserID() {
        String connID = tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.AddStore(11,connID,"Store");
        assertTrue(response.getIsErr());
    }

    // requirement 3.2
    @Test
    public void AddStoreSuccess() {
        String guest1= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest1, "Elinor", "123");
        Response res= tradingSystem.Login(guest1, "Elinor", "123");
        String EconnID= res.returnConnID();
        Integer EuserId=res.returnUserID();
        Response response= tradingSystem.AddStore(EuserId,EconnID,"HappyStore");
        assertFalse(response.getIsErr());
    }

    // requirement 3.2
    @Test
    public void AddStoreSameName() {
        String guest1= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest1, "Elinor", "123");
        Response res= tradingSystem.Login(guest1, "Elinor", "123");
        String EconnID= res.returnConnID();
        Integer EuserId=res.returnUserID();
        tradingSystem.AddStore(EuserId,EconnID,"Store1");
        Response response= tradingSystem.AddStore(EuserId,EconnID,"Store1");
        assertTrue(response.getIsErr());
    }

    //endregion

}
