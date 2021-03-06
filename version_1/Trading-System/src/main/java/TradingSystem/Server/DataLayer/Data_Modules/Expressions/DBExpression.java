package TradingSystem.Server.DataLayer.Data_Modules.Expressions;

import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.AndComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.*;
import org.hibernate.annotations.Cascade;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "Expression")
public class DBExpression {
    @Id
    @SequenceGenerator(
            name = "Expression_SEQUENCE",
            sequenceName = "Expression_SEQUENCE",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "Expression_SEQUENCE"
    )
    @Column(
            name = "ExpressionID"
    )
    Integer ExpressionId;


    @Column(name = "storeId")
    @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_id_fk"
            )
    )
    int storeId;
    @OneToOne
    private DBExpression parent;
    @OneToMany(cascade= CascadeType.PERSIST) //add column definitions as needed
    private List<DBExpression> subdomains;
    @Column(
            name = "minAge"
    )
    private Integer minAge;
    @Column(
            name = "category"
    )
    private String  category;
    @JoinColumn(
            name = "product_id",
            nullable = false,
            referencedColumnName = "productID",
            foreignKey = @ForeignKey(
                    name = "prodcut_id_fk"
            )
    )
    int productid;

    Date date;
    @Column(
            name = "maxQuantity"
    )
    Integer maxQuantity;

    private String Type;

    public DBExpression(){
        this.parent=this;
    }

    public DBExpression(DBExpression parent){
        this.parent=parent;
    }

    public DBExpression(Expression expression, DBExpression parent){
        if(expression instanceof OrComposite){
            this.parent=null;
            Type="OR";
            subdomains=new ArrayList<>();
            for(Expression exp: ((OrComposite) expression).children){
                DBExpression toadd= new DBExpression(exp, this);
                subdomains.add(toadd);
            }
        }
        else if(expression instanceof AndComposite){
            this.parent=null;
            Type="AND";
            subdomains=new ArrayList<>();
            for(Expression exp: ((AndComposite) expression).children){
                DBExpression toadd= new DBExpression(exp,this);
                subdomains.add(toadd);
            }
        }
        else if(expression instanceof AgeLimitForCategory){
            this.parent=parent;
            Type="Limit_Category";
            this.category=((AgeLimitForCategory) expression).category;
            this.minAge=((AgeLimitForCategory) expression).minAge;
        }
        else if(expression instanceof AgeLimitForProduct){
            this.parent=parent;
            Type="Limit_Product";
            this.minAge= ((AgeLimitForProduct) expression).getMinAge();
            this.productid=((AgeLimitForProduct) expression).getProductID();
        }
        else if(expression instanceof AgeLimitForStore){
            this.parent=parent;
            Type="Limit_Store";
            this.minAge=((AgeLimitForStore) expression).getMinAge();
            this.storeId=((AgeLimitForStore) expression).getStoreID();
        }
        else if(expression instanceof QuantityLimitForStore){
            this.parent=parent;
            Type="Quantity_Store";
            this.storeId=((QuantityLimitForStore) expression).getStoreID();
            this.maxQuantity=((QuantityLimitForStore) expression).getMaxQuantity();
        }
        else if(expression instanceof QuantityLimitForProduct){
            this.parent=parent;
            Type="Quantity_Product";
            this.productid=((QuantityLimitForProduct) expression).getProductID();
            this.maxQuantity=((QuantityLimitForProduct) expression).getMaxQuantity();
        }
        else if(expression instanceof QuantityLimitForCategory){
            this.parent=parent;
            Type="Quantity_Category";
            this.category=((QuantityLimitForCategory) expression).getCategory();
            this.maxQuantity=((QuantityLimitForCategory) expression).getMaxQuantity();
        }
        else{
            this.parent=null;
        }
    }

    public DBExpression getParent(){
        return parent;
    }

    public void setParent(DBExpression expression){
        this.parent=expression;
    }

    public List<DBExpression> getSubdomains() {
        return subdomains;
    }

    public void setStoreId(int id){
        this.storeId=id;
    }

    public String getType(){
        return Type;
    }
    public String getCategory(){
        return category;
    }
    public Integer getMinAge(){
        return minAge;
    }

    public int getProductid(){
        return productid;
    }

    public int getStoreId(){
        return storeId;
    }

    public Integer getMaxQuantity(){
        return maxQuantity;
    }
}
