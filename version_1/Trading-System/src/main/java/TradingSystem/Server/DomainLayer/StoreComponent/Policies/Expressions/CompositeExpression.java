package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CompositeExpression implements Expression {

    public List<Expression> children;

    public CompositeExpression()
    { this.children = new LinkedList<>(); }

    public Expression add(Expression expr){
        children.add(expr);
        return this;
    }

    public abstract Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID);

    public Boolean hasChildren()
        { return !children.isEmpty(); }

    public List<Expression> getChildren() {
        return children;
    }

    private void setChildren(List<Expression> children) {
        this.children = children;
    }
}
