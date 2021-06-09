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
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.AddComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.MaxComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorComposite;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
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
    @Column(name = "num_products")
    private Integer numOfProductsForSale;
    @Column(name = "price_sale")
    Integer priceForSale;
    @ManyToOne
    @JoinColumn(
            name = "product_id",
            nullable = false,
            referencedColumnName = "productID",
            foreignKey = @ForeignKey(
                    name = "prodcut_id_fk"
            )
    )
    private DataProduct productId;
    @Column(name = "quantity")
    private Integer quantityForSale;

    public DBSale(){

    }

    public DBSale(Sale sale, DBSale parent){
        if(sale instanceof AddComposite){
            this.parent=null;
            subdomains=new ArrayList<>();
            for(Sale s: ((AddComposite) sale).children){
                DBSale toadd= new DBSale(s,this);
                subdomains.add(toadd);
            }
        }
        else if(sale instanceof XorComposite){
            this.parent=null;
            subdomains=new ArrayList<>();
            for(Sale s: ((XorComposite) sale).children){
                DBSale toadd= new DBSale(s,this);
                subdomains.add(toadd);
            }
        }
        else if(sale instanceof MaxComposite){
            this.parent=parent;
            this.parent=null;
            subdomains=new ArrayList<>();
            for(Sale s: ((MaxComposite) sale).children){
                DBSale toadd= new DBSale(s,this);
                subdomains.add(toadd);
            }
        }
        else if(sale instanceof NumOfProductsForGetSale){
            this.parent=parent;
            this.numOfProductsForSale=((NumOfProductsForGetSale) sale).getNumOfProductsForSale();
        }
        else if(sale instanceof PriceForGetSale){
            this.parent=parent;
            this.priceForSale=((PriceForGetSale) sale).getPriceForSale();
        }
        else if(sale instanceof QuantityForGetSale){
            this.parent=parent;
            this.quantityForSale=((QuantityForGetSale) sale).getQuantityForSale();
        }
        else{
            this.parent=null;
        }
    }
}
