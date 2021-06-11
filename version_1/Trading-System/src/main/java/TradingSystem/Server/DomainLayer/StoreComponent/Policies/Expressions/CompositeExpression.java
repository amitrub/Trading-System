package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CompositeExpression implements Expression {

    public List<Expression> children;

    public CompositeExpression(List<Expression> c){
        this.children=c;
    }


    public CompositeExpression()
    {
        this.children = new LinkedList<>();
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

    @Override
    public Response checkValidity(int storeID) {
        for (Expression e:children
        ) {
            Response r=e.checkValidity(storeID);
            if(r.getIsErr()){
                return r;
            }
        }
        return new Response("correct");
    }

}
