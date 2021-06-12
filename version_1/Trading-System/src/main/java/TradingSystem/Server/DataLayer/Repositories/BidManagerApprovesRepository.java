package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.Bid.DataBid;
import TradingSystem.Server.DataLayer.Data_Modules.Bid.DataBidManagerApproves;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.BidManagerKey;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserProductKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidManagerApprovesRepository extends JpaRepository<DataBidManagerApproves, BidManagerKey> {


}
