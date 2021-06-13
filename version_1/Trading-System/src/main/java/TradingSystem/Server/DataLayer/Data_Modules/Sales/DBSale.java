package TradingSystem.Server.DataLayer.Data_Modules.Sales;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.AndComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.NumOfProductsForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.PriceForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.QuantityForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "SALE")
public class DBSale {
    @Id
    @SequenceGenerator(
            name = "SALE_SEQUENCE",
            sequenceName = "SALE_SEQUENCE",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "SALE_SEQUENCE"
    )
    @Column(
            name = "SALEID"
    )
    Integer SaleId;
    @OneToOne
    private DBSale parent;

    @OneToMany(cascade= CascadeType.PERSIST) //add column definitions as needed
    private List<DBSale> subdomains;

    private String category;

    private Integer  discountPercentage;

    @JoinColumn(
            name = "product_id",
            nullable = false,
            referencedColumnName = "productID",
            foreignKey = @ForeignKey(
                    name = "prodcut_id_fk"
            )
    )
    private Integer productID;
    @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_id_fk"
            )
    )

    private Integer storeID;

    private Integer quantity;

    private String Type;

    @OneToOne
    public DBSaleExpression expression;

    public DBSale(){

    }

    public DBSale(Sale sale, DBSale parent){
        if(sale instanceof AddComposite){
            this.parent=null;
            Type= "ADD";
            subdomains=new ArrayList<>();
            for(Sale s: ((AddComposite) sale).children){
                DBSale toadd= new DBSale(s,this);
                subdomains.add(toadd);
            }
        }
        else if(sale instanceof XorComposite){
            this.parent=null;
            Type= "XOR";
            subdomains=new ArrayList<>();
            for(Sale s: ((XorComposite) sale).children){
                DBSale toadd= new DBSale(s,this);
                subdomains.add(toadd);
            }
        }
        else if(sale instanceof MaxComposite){
            this.parent=null;
            Type= "MAX";
            subdomains=new ArrayList<>();
            for(Sale s: ((MaxComposite) sale).children){
                DBSale toadd= new DBSale(s,this);
                subdomains.add(toadd);
            }
        }
        else if(sale instanceof ProductSale){
            this.parent=parent;
            Type= "Product";
            this.productID=((ProductSale) sale).getProductID();
            this.discountPercentage=((ProductSale) sale).getDiscountPercentage();
            this.expression=new DBSaleExpression(((ProductSale) sale).getExpression(),this, null);
        }
        else if(sale instanceof StoreSale){
            this.parent=parent;
            Type= "Store";
            this.storeID= ((StoreSale) sale).getStoreID();
            this.discountPercentage=((StoreSale) sale).getDiscountPercentage();
            this.expression=new DBSaleExpression(((StoreSale) sale).getExpression(),this,null);
        }
        else if(sale instanceof CategorySale){
            this.parent=parent;
            Type= "Category";
            this.category=((CategorySale) sale).getCategory();
            this.discountPercentage=((CategorySale) sale).getDiscountPercentage();
            this.expression=new DBSaleExpression(((CategorySale) sale).getExpression(),this,null);
        }
        else{
            this.parent=null;
        }
    }

    public DBSaleExpression getExpression(){
        return expression;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getType() {
        return Type;
    }

    public String getCategory() {
        return category;
    }

    public List<DBSale> getSubdomains(){
        return subdomains;
    }

    public Integer getProductID() {
        return productID;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }
}
