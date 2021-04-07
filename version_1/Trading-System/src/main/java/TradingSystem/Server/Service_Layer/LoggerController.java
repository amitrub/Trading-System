package TradingSystem.Server.Service_Layer;


import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerController {

    private static LoggerController logger = null;
    private final Logger LOGGERinfo = Logger.getLogger("Info");
    private final Logger LOGGERErr = Logger.getLogger("Errors");
    private FileHandler fhmsg;
    private FileHandler fherr;
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
            fhmsg = new FileHandler("Info.log");
            fherr = new FileHandler("Error.log");
            formatter = new SimpleFormatter();
            LOGGERErr.addHandler(fherr);
            LOGGERinfo.addHandler(fhmsg);
            fhmsg.setFormatter(formatter);
            fherr.setFormatter(formatter);
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
