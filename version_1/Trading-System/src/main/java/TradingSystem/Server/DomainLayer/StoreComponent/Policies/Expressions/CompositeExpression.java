package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CompositeExpression implements Expression {

    public List<Expression> children;
 //   private Integer expressionID;

    public CompositeExpression(List<Expression> c){
        this.children=c;
    }

    public CompositeExpression(Integer exp)
    {
        this.children = new LinkedList<>();
    //this.expressionID = exp;
    }

    public Expression add(Expression expr){
        children.add(expr);
        return this;
    }

    public Boolean hasChildren()
    { return !children.isEmpty(); }

    public List<Expression> getChildren() {
        return children;
    }

    private void setChildren(List<Expression> children) {
        this.children = children;
    }

    public abstract Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID);


    /**
     *implement with ID
     **/
/*
    @Override
    public Expression setExpression(Integer expID, Expression exp) {
        if(this.expressionID==expID){
            this.children.add(exp);
            return this;
        }
        else{
            for (Expression e:children
                 ) {
                Expression tmp=e.setExpression(expID,exp);
                if(tmp!=null){
                    return this; ///todo check return e? tmp?
                }
            }
        }
        return null;
    }

    @Override
    public Expression getExpression(Integer id) {
        if(this.expressionID==id){
            return this;
        }
        else{
            for (Expression e:children
            ) {
                Expression tmp=e.getExpression(id);
                if(tmp!=null){
                    return tmp;
                }
            }
        }
        return null;
    }

    @Override
    public Integer getExpressionId() {
        return this.expressionID;
    }



 */
}
