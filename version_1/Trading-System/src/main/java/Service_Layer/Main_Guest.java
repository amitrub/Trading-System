package Service_Layer;

import Domain_Layer.Shopping_Component.Shopping_Bag;
import Domain_Layer.Shopping_Component.Shopping_Cart;
import Domain_Layer.Shopping_Component.Shopping_History;
import Domain_Layer.Store_Component.Product;
import Domain_Layer.Store_Component.Store;

import java.util.List;

public class Main_Guest {
    public static void main(String[] args) {
        System.out.println("Hello World!"); // Display the string.
    }


    public Integer Register(String userName, String password){
        return 0;
    }

    public Integer Login(String userName, String password){ //todo id?
        return 0;
    }

    public List<Object> Search(String objectToSearch){
        return null;
    }

    public List<Object> Sort(Integer Category){ //todo getting list?
        return null;
    }

    //todo- only subscriber
    public void Logout(int userId){ //todo void?

    }

     public List<Store> ShowAllStores(){
        return null;
     }

    public List<Product> ShowAllProducts(){
        return null;
    }

    public Shopping_Cart ShowShoppingCart(int userId){
        return null;
    }

    //todo- only subscriber
    public void OpenStore(int id,String storeName){ //todo void? id?

    }

    //todo- only subscriber
    public Shopping_History ShowHistory(int userId){ //todo list? id?
        return null;
    }

    //todo- only subscriber
    public void WriteComment(int userId, int productId, String comment){ //todo void? id?

    }

    //todo- only System Manager
    public List<Shopping_History> ShowStoreHistory(int userId){ //todo list? id?
        return null;
    }

}
