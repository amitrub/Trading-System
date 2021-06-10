package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompositeExp extends JpaRepository<DBExpression, Integer> {
}
