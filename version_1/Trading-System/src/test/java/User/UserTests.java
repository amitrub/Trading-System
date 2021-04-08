package User;

import TradingSystem.Client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class UserTests {

    Client client;


    @BeforeEach
    void setUp() {
        client = new Client();
    }

    @Test
    void registerHappy() {
        int respondID1 = client.Register("Roee", "1234");
        assertTrue(respondID1 != -1);

        int respondID2 = client.Register("Dani", "qwerty");
        assertTrue(respondID2 != -1);
        //connID is empty??
    }

    @Test
    void registerDuplicateUserName() {   //duplicate userName
        int respondID1 = client.Register("Avi", "qwerty");
        assertTrue(respondID1 != -1);
        int respondID2 = client.Register("Avi", "qqq");
        assertTrue(respondID2 == -1);
    }

    /*
    @Test
    void registerPassword() {    //password is too short
        int respondID = client.Register("Lior", "1");
        assertTrue(respondID == -1);
    }
     */

    @Test
    void loginHappy(){
        int respondID1 = client.Register("Yossi", "qwerty");
        int respondID2 = client.Login("Yossi", "qwerty");
        assertEquals(respondID1, respondID2);
        assertTrue(client.isSubscriber());
    }

    @Test
    void loginIncorrectPassword(){
        client.Register("Shai", "qwerty");
        int respondID = client.Login("Shai", "qwe");
        assertEquals(respondID, -1);
    }

    @Test
    void loginIncorrectUserName(){
        client.Register("Elinor", "qwerty");
        int respondID = client.Login("Eli", "qwerty");
        assertEquals(respondID, -1);
    }

    @Test
    void logoutHappy(){
        client.Register("Shir", "qwerty");
        int respondID = client.Logout();
        assertEquals(respondID, -1);
        assertFalse(client.isSubscriber());
    }

    @Test
    void searchTest(){

    }

}
