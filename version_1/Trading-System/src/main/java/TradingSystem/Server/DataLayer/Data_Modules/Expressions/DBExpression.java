package TradingSystem.Server.DataLayer.Data_Modules.Expressions;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.AndComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "DB_expression")
public class DBExpression {
    @Id
    @SequenceGenerator(
            name = "Expression_SEQUENCE",
            sequenceName = "Expression_SEQUENCE",
            allocationSize = 1
    )
    @Column(
            name = "ExpressionID"
    )
    int ExpressionId;

    @Column(name = "ID")
    @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_id_fk"
            )
    )
    int storeId;
    @Column(name = "parent")
    @OneToOne(cascade= CascadeType.MERGE) //add column definitions as needed
    private DBExpression parent;
    @OneToMany //add column definitions as needed
    private List<DBExpression> subdomains;
    @Column(
            name = "minAge"
    )
    Integer minAge;
    @Column(
            name = "category"
    )
    String  category;
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

    public DBExpression(){
        this.parent=this;
    }

    public DBExpression(DBExpression parent){
        this.parent=parent;
    }

    public DBExpression(Expression expression, DBExpression parent){
        if(expression instanceof OrComposite){
            this.parent=parent;
            subdomains=new ArrayList<>();
            for(Expression exp: ((OrComposite) expression).children){
                DBExpression toadd= new DBExpression(exp, this);
                subdomains.add(toadd);
            }
        }
        else if(expression instanceof AndComposite){
            this.parent=this;
            subdomains=new ArrayList<>();
            for(Expression exp: ((AndComposite) expression).children){
                DBExpression toadd= new DBExpression(exp,this);
                subdomains.add(toadd);
            }
        }
        else if(expression instanceof AgeLimitForCategory){
            this.parent=parent;
            this.category=((AgeLimitForCategory) expression).category;
            this.minAge=((AgeLimitForCategory) expression).minAge;
        }
        else if(expression instanceof AgeLimitForProduct){
            this.parent=parent;
            this.minAge= ((AgeLimitForProduct) expression).getMinAge();
            this.productid=((AgeLimitForProduct) expression).getProductID();
        }
        else if(expression instanceof AgeLimitForStore){
            this.parent=parent;
            this.minAge=((AgeLimitForStore) expression).getMinAge();
            this.storeId=((AgeLimitForStore) expression).getStoreID();
        }
        else if(expression instanceof QuantityLimitForStore){
            this.parent=parent;
            this.storeId=((QuantityLimitForStore) expression).getStoreID();
            this.maxQuantity=((QuantityLimitForStore) expression).getMaxQuantity();
        }
        else if(expression instanceof QuantityLimitForProduct){
            this.parent=parent;
            this.productid=((QuantityLimitForProduct) expression).getProductID();
            this.maxQuantity=((QuantityLimitForProduct) expression).getMaxQuantity();
        }
        else if(expression instanceof QuantityLimitForCategory){
            this.parent=parent;
            this.category=((QuantityLimitForCategory) expression).getCategory();
            this.maxQuantity=((QuantityLimitForCategory) expression).getMaxQuantity();
        }
        else{
            this.parent=this;
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
}
