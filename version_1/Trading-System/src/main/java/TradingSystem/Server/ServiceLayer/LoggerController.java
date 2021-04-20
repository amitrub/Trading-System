package TradingSystem.Server.ServiceLayer;


import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerController {

    private static LoggerController logger = null;
    private final Logger LOGGERinfo = Logger.getLogger("Info");
    private final Logger LOGGERErr = Logger.getLogger("Errors");
    private FileHandler fhMsg;
    private FileHandler fhErr;
    private SimpleFormatter formatter;

    public static LoggerController getInstance()
    {
        if (logger == null){
            logger = new LoggerController();
            logger.Initialization();
        }
        return logger;
    }

    private void Initialization(){
        try {
            fhMsg = new FileHandler("Info.log");
            fhErr = new FileHandler("Error.log");
            formatter = new SimpleFormatter();
            LOGGERErr.addHandler(fhErr);
            LOGGERinfo.addHandler(fhMsg);
            fhMsg.setFormatter(formatter);
            fhErr.setFormatter(formatter);
        }
        catch (IOException e){
            System.out.println("Error msg");
        }
    }

    public void WriteErrorMsg(String err){
        LOGGERErr.info(err);
    }

    public void WriteLogMsg(String msg){
        LOGGERinfo.info(msg);
    }
}
