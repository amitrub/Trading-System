package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DataBuyingPolicy;
import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DataLayer.Repositories.BuyingRepository;
import TradingSystem.Server.DataLayer.Repositories.DBExpRepository;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class BuyingService {

    @Autowired
    BuyingRepository buyingRepository;
    @Autowired
    DBExpRepository dataExpCompositeRepository;

    public Response getBuyingByStore(Integer storeid) throws EntityNotFoundException
    {
        Optional<DataBuyingPolicy> buyingPolicy=buyingRepository.findById(storeid);
        if(!buyingPolicy.isPresent()){
            return new Response(true,"Could not found buying policy for store");
        }
        Response response=new Response(false," ");
        response.AddDBBuyingPolicy(buyingPolicy.get());
        return response;
    }

    //TODO make to transction
    public Response AddBuyingPolicy(DataBuyingPolicy buyingPolicy){
        try {
            DBExpression dataExpression= buyingPolicy.getExpression();
            dataExpCompositeRepository.saveAndFlush(dataExpression);
            buyingRepository.saveAndFlush(buyingPolicy);
            return new Response(false,"buying policy was added successfully");
        }
        catch (HibernateException e){
            return new Response(true,"could not add buying policy for store");
        }
    }
}
