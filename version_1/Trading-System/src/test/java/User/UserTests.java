package User;

import TradingSystem.Client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
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
        assertEquals(respondID1, 1);
        int respondID2 = client.Register("Dani", "qwerty");
        assertEquals(respondID2, 2);
    }

    @Test
    void registerDuplicateUserName() {   //duplicate userName
        int respondID1 = client.Register("Dani", "qwerty");
        assertEquals(respondID1, 1);
        int respondID2 = client.Register("Dani", "qqq");
        assertEquals(respondID2, -1);
    }

    @Test
    void registerPassword() {    //password is too short
        int respondID1 = client.Register("Dani", "1");
        assertEquals(respondID1, -1);
    }

    @Test
    void loginHappy(){
        client.Register("Dani", "qwerty");
        assertTrue(client.isLogin());
        client.Logout();
        int respondID = client.Login("Dani", "qwerty");
        assertEquals(respondID, 1);
    }

    @Test
    void loginIncorrectPassword(){
        client.Register("Dani", "qwerty");
        client.Logout();
        int respondID = client.Login("Dani", "qwe");
        assertEquals(respondID, -1);
    }

    @Test
    void loginIncorrectUserName(){
        client.Register("Dani", "qwerty");
        client.Logout();
        int respondID = client.Login("Roee", "qwerty");
        assertEquals(respondID, -1);
    }

    @Test
    void logoutHappy(){
        client.Register("Dani", "qwerty");
        client.Logout();
        assertFalse(client.isLogin());
    }


}
