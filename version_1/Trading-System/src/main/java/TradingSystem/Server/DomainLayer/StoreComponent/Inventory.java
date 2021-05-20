package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.*;
import java.util.concurrent.locks.Lock;

import java.util.concurrent.ConcurrentHashMap;

public class Inventory {

    private final Integer storeID;
    private final String storeName;
    private int nextProductID = 0;
    //productID_product
    private ConcurrentHashMap<Integer, Product> products;
    //productID_quantity
//    private ConcurrentHashMap<Integer, Integer> productQuantity;
    //productID_Lock
//    private ConcurrentHashMap<Integer, Lock> productLock;


    public Inventory(Integer storeID, String storeName) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.products = new ConcurrentHashMap<Integer, Product>();
//        this.productQuantity=new ConcurrentHashMap<Integer, Integer>();
//        this.productLock=new ConcurrentHashMap<Integer, Lock>();
    }

    private synchronized int getNextProductID() {
        this.nextProductID++;
        return this.nextProductID;
    }

    public List<DummyProduct> ShowStoreProducts() {
        List<DummyProduct> products = new ArrayList<>();
        Set<Integer> productSet = this.products.keySet();
        for (Integer key : productSet) {
            Product product = this.products.get(key);
            products.add(new DummyProduct(this.storeID, this.storeName, key, product.getProductName(), product.getPrice(), product.getCategory(), product.getQuantity()));
        }
        return products;
    }

    public Response addProduct(String productName, String category, Double price, int quantity){
        if (!IsProductNameExist(productName)){
            Integer productID=getNextProductID();
            Product p=new Product(storeID, storeName, productID, productName, category, price, quantity);
            this.products.put(productID,p);
//            this.productQuantity.put(productID,0);
//            this.productLock.put(productID,new ReentrantLock());
            return new Response(false, "AddProductToStore: Add Product " + productName + " was successful");
        } else
            return new Response(true, "AddProductToStore: Product name " + productName + " is taken");

    }

    public boolean IsProductNameExist(String productName) {
        Set<Integer> productSet = this.products.keySet();
        for (Integer id : productSet) {
            Product product = this.products.get(id);
            if (productName.equals(product.getProductName()))
                return true;
        }
        return false;
    }

    public boolean checkProductsExistInTheStore(Integer productID, Integer quantity) {
        return this.products.containsKey(productID) && this.products.get(productID).getQuantity() >= quantity;
    }


    public Response addQuantityProduct(Integer productId, Integer quantity) {
        if (this.products.containsKey(productId)) {
            Product product = this.products.get(productId);
            Integer oldQuantity = product.getQuantity();
            product.setQuantity(quantity + oldQuantity);
            return new Response("ChangeQuantityProduct: Add Product " + product.getProductName() + " to Inventory was successful");
        } else
            return new Response(true, "ChangeQuantityProduct: The product with id " + productId + "  does not exist in the system");
    }

    public Response deleteProduct(Integer productID) {
        if (this.products.containsKey(productID)) {
//            this.productQuantity.remove(productID);
            this.products.remove(productID);
//            this.productLock.remove(productID);
            return new Response(false, "RemoveProduct: Remove product " + productID + " from the Inventory was successful");
        } else
            return new Response(true, "RemoveProduct: The product " + productID + " does not exist in the system");
    }

    public Product getProduct(Integer productId) {
        return this.products.get(productId);
    }

    //todo- syncronize!
    public Response reduceProducts(ConcurrentHashMap<Integer, Integer> products_quantity) {
        Set<Integer> PQ = products_quantity.keySet();
        for (Integer PID : PQ) {
            int quantityToReduce = products_quantity.get(PID);
            if (products.containsKey(PID)) {
                Product product = this.products.get(PID);
                int quantity = product.getQuantity();
                int newQuantity = quantity - quantityToReduce;
                if (newQuantity < 0) {
                    return new Response(true, "Purchase: There are not enough units from a product " + PID + " In store " + storeID);
                }
                product.setQuantity(newQuantity);
//                this.productQuantity.remove(PID);
//                this.productQuantity.put(PID, newQuantity);
            } else {
                return new Response(true, "Purchase: The product " + PID + " In store " + storeID + " does not exist in the system");
            }
        }
        return new Response(false, "Purchase: Product inventory successfully updated");
    }

    public void cancelReduceProducts(ConcurrentHashMap<Integer, Integer> products) {
        Set<Integer> PQ = products.keySet();
        for (Integer PID : PQ) {
            int quantityToAdd = products.get(PID);
            if (products.containsKey(PID)) {
                Product product = this.products.get(PID);
                int quantity = product.getQuantity();
                int newQuantity = quantity + quantityToAdd;
                product.setQuantity(newQuantity);
            }
        }
    }

    public Response addCommentToProduct(Integer productId, Integer userID, String comment) {
        if (this.products.containsKey(productId)) {
            return this.products.get(productId).addComment(userID, comment);
        } else
            return new Response(true, "The product does not exist in the system");
    }

    public Response removeCommentFromProduct(Integer productId, Integer userID) {
        if (this.products.containsKey(productId)) {
            return this.products.get(productId).removeComment(userID);
        } else
            return new Response(true, "The product does not exist in the system");

    }

    public List<String> getAllCommentsForProduct(Integer productID) {
        return this.products.get(productID).getComments();
    }

    public boolean checkProductExist(Integer productID) {
        Iterator it = this.products.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            int id = (int) pair.getKey();
            if (id == productID) {
                return true;
            }
        }
        return false;
    }

    public Integer getProductID(Integer storeID, String productName) {
        List<Integer> storeProducts = getAllTheStoreProductID(storeID);
        for (Integer i : storeProducts
        ) {
            Product p = products.get(i);
            if (p.getProductName().equals(productName)) {
                return i;
            }
        }
        return -1;
    }

    public List<Integer> getAllTheStoreProductID(Integer storeID) {
        List<Integer> storeProducts = new ArrayList<>();
        Iterator it = this.products.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            int PID = (int) pair.getKey();
            storeProducts.add(PID);
        }
        return storeProducts;
    }

    public List<Product> getAllTheProducts(List<Integer> productsID) {
        List<Product> products = new ArrayList<>();
        for (Integer i : productsID
        ) {
            Product p = products.get(i);
            products.add(p);
        }
        return products;
    }

    public Response editProductDetails(Integer productId, String productName, Double price, String category, Integer quantity) {
        Iterator it = this.products.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            int id = (int) pair.getKey();
            if (id == productId) {
                Product p = new Product(storeID, storeName, productId, productName, category, price, quantity);
                this.products.remove(productId);
                this.products.put(id, p);
                return new Response(false, "EditProduct: The product " + productName + " update successfully");
            }
        }
        return new Response(true, "EditProduct: The product " + productName + " does not exist in the system");
    }

    public List<Integer> SearchProduct(String name, String category, int minprice, int maxprice) {
        List<Integer> products = new ArrayList<>();
        Set<Integer> productsId = this.products.keySet();
        for (Integer PID : productsId) {
            boolean AddTheProduct = true;
            Product p = this.products.get(PID);
            if (name != null) {
                AddTheProduct = p.getProductName().equals(name);
            }
            if (AddTheProduct) {
                if (category != null) {
                    AddTheProduct = p.getCategory().equals(category);
                }
                if (AddTheProduct) {
                    if (minprice != -1 && maxprice != -1) {
                        AddTheProduct = p.getPrice() >= minprice && p.getPrice() <= maxprice;
                    }
                    if (AddTheProduct) {
                        if (AddTheProduct) {
                            products.add(PID);
                        }
                    }
                }
            }
        }
     return products;
    }

    public List<Integer> getDummySearchByName(List<Integer> FinalID, String name)
    {
        List<Integer> products = new ArrayList<>();
        if (FinalID.isEmpty()) {
            Set<Integer> productsId = this.products.keySet();
            for (Integer PID : productsId) {
                Product p = this.products.get(PID);
                if (p.getProductName().equals(name)) {
                    products.add(PID);
                }
            }
        } else {
            Set<Integer> productsId = this.products.keySet();
            for (Integer PID : productsId) {
                Product p = this.products.get(PID);
                if (p.getProductName().equals(name) && FinalID.contains(PID)) {
                    products.add(PID);
                }
            }
        }
        return products;
    }

    public List<Integer> getDummySearchByCategory(List<Integer> FinalID, String category) {
        List<Integer> products = new ArrayList<>();
        if (FinalID.isEmpty()) {
            Set<Integer> productsId = this.products.keySet();
            for (Integer PID : productsId) {
                Product p = this.products.get(PID);
                if (p.getCategory().equals(category)) {
                    products.add(PID);
                }
            }
        } else {
            Set<Integer> productsId = this.products.keySet();
            for (Integer PID : productsId) {
                Product p = this.products.get(PID);
                if (p.getCategory().equals(category) && FinalID.contains(PID)) {
                    products.add(PID);
                }
            }
        }
            return products;

        }

        public List<Integer> getDummySearchByRate (List < Integer > FinalID,int prank)
        {
            List<Integer> products = new ArrayList<>();
            if (FinalID.isEmpty()) {
                Set<Integer> productsId = this.products.keySet();
                for (Integer PID : productsId) {
                    Product p = this.products.get(PID);
                    Double R = this.products.get(PID).getRate();
                    if (R >= prank) {
                        products.add(PID);
                    }
                }
            } else {
                Set<Integer> productsId = this.products.keySet();
                for (Integer PID : productsId) {
                    Product p = this.products.get(PID);
                    Double R = this.products.get(PID).getRate();
                    if (R >= prank && FinalID.contains(PID)) {
                        products.add(PID);
                    }
                }
            }
                return products;
            }

            public List<Integer> getDummySearchByPrice (List<Integer> FinalID, int minprice, int maxprice)
            {
                List<Integer> products = new ArrayList<>();
                if (FinalID.isEmpty()) {
                    Set<Integer> productsId = this.products.keySet();
                    for (Integer PID : productsId) {
                        Product p = this.products.get(PID);
                        Double Price = p.getPrice();
                        if (Price >= minprice && Price <= maxprice) {
                            products.add(PID);
                        }
                    }
                } else {
                    Set<Integer> productsId = this.products.keySet();
                    for (Integer PID : productsId) {
                        Product p = this.products.get(PID);
                        Double Price = p.getPrice();
                        if (Price >= minprice && Price <= maxprice && FinalID.contains(PID)) {
                            products.add(PID);
                        }
                    }
                }
                return products;
            }

            public List<DummyProduct> getDummySearchForList (List<Integer> products)
            {
                List<DummyProduct> dummyProducts = new ArrayList<DummyProduct>();
                for (Integer i : products) {
                    Product p = this.products.get(i);
                    DummyProduct D = new DummyProduct(this.storeID, storeName, p.getProductID(), p.getProductName(), p.getPrice(), p.getCategory(), p.getQuantity());//productComments.get(i).getRate());
                    dummyProducts.add(D);
                }
                return dummyProducts;
            }

            public void addRatingToProduct (Integer productID, Integer userID, Double rating)
            {
                this.products.get(productID).addRate(userID, rating);
                Double Rate = this.products.get(productID).CalculateRate();
                this.products.get(productID).setRate(Rate);
            }

            public void removeRatingFromProduct (Integer productID, Integer userID)
            {
                this.products.get(productID).removeRate(userID);
                Double Rate = this.products.get(productID).CalculateRate();
                this.products.get(productID).setRate(Rate);
            }

            public Double CalculateRateForProduct (Integer productID)
            {
                return this.products.get(productID).CalculateRate();
            }

            public void lockProduct (Integer productID){
            this.products.get(productID).lockProduct();
        }

            public void unlockProduct (Collection < Integer > productID) {
            this.products.get(productID).unlockProduct();
        }

            public boolean productIsLock (Integer productID){
            return this.products.get(productID).productIsLock();
        }

            public List<Product> getProducts () {
            List<Product> products = new ArrayList<Product>();
            Set<Integer> productSet = this.products.keySet();
            for (Integer id : productSet) {
                products.add(this.products.get(id));
            }
            return products;
        }

            public int getQuantity (Integer productID){
            if (this.products.containsKey(productID)) {
                return this.products.get(productID).getQuantity();
            }
            return 0;
        }

            public List<String> getCommentsForProduct ( int productID){
            if (this.products.get(productID) != null) {
                return this.products.get(productID).getCommentsForProduct(productID);
            }
            return null;
        }


    public Lock getProductLock(int productID) {
        return this.products.get(productID).getLock();
    }


}

