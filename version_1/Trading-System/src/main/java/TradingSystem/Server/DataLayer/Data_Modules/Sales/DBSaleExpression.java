package TradingSystem.Server.DataLayer.Data_Modules.Sales;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.AndComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.NumOfProductsForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.PriceForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.QuantityForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.ProductSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "SALE_Expression")
public class DBSaleExpression {
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
            name = "Sale_Exp_Id"
    )
    Integer Id;
//    @OneToOne @MapsId
//    DBSale dbSale;
    @JoinColumn(
            name = "product_id",
            nullable = false,
            referencedColumnName = "productID",
            foreignKey = @ForeignKey(
                    name = "prodcut_id_fk"
            )
    )
    private Integer productId;
    private Integer quantityForSale;
    private Integer priceForSale;
    private Integer numOfProductsForSale;
    @OneToMany(cascade= CascadeType.PERSIST) //add column definitions as needed
    private List<DBSaleExpression> subdomains;
    @OneToOne(cascade= CascadeType.PERSIST)
    private DBSaleExpression parent;

    public DBSaleExpression(){

    }

    public DBSaleExpression(Expression expression, DBSale saleID, DBSaleExpression parentexp){
      //  this.dbSale=saleID;
        if(expression instanceof NumOfProductsForGetSale){
            this.parent=parentexp;
            this.numOfProductsForSale=((NumOfProductsForGetSale) expression).getNumOfProductsForSale();
        }
        else if(expression instanceof PriceForGetSale){
            this.parent=parentexp;
            this.priceForSale=((PriceForGetSale) expression).getPriceForSale();
        }
        else if(expression instanceof QuantityForGetSale){
            this.parent=parentexp;
            this.productId=((QuantityForGetSale) expression).getProductId();
            this.quantityForSale=((QuantityForGetSale) expression).getQuantityForSale();
        }
        else if(expression instanceof OrComposite){
            this.parent=null;
            subdomains=new ArrayList<>();
            for(Expression exp: ((OrComposite) expression).children){
                DBSaleExpression toadd= new DBSaleExpression(exp,saleID,this);
                subdomains.add(toadd);
            }
        }
        else if(expression instanceof AndComposite){
            this.parent=null;
            subdomains=new ArrayList<>();
            for(Expression exp: ((AndComposite) expression).children){
                DBSaleExpression toadd= new DBSaleExpression(exp,saleID,this);
                subdomains.add(toadd);
            }
        }
    }
}
