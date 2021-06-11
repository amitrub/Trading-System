package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.Sales.DBSaleExpression;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBSaleExpRepository extends JpaRepository<DBSaleExpression,Integer> {
}
