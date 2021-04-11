package TradingSystem.Server.ServiceLayer;

public class Configuration {
    //Colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    //Getters
    public static String getAnsiReset() {
        return ANSI_RESET;
    }
    public static String getAnsiBlack() {
        return ANSI_BLACK;
    }
    public static String getAnsiRed() {
        return ANSI_RED;
    }
    public static String getAnsiGreen() {
        return ANSI_GREEN;
    }
    public static String getAnsiYellow() {
        return ANSI_YELLOW;
    }
    public static String getAnsiBlue() {
        return ANSI_BLUE;
    }
    public static String getAnsiPurple() {
        return ANSI_PURPLE;
    }
    public static String getAnsiCyan() {
        return ANSI_CYAN;
    }
    public static String getAnsiWhite() {
        return ANSI_WHITE;
    }

    //Urls
    public static String ip = "localhost";
    public static String urlbaseGuest = String.format("http://%s:8080/api/", ip) ;
    public static String urlbaseSubscriber = String.format("http://%s:8080/api/subscriber", ip) ;
    public static String urlbaseOwner = String.format("http://%s:8080/api/owner", ip) ;


    public static String errMsgGenerator(String side, String className, String line, String msg) {
        return side + " : <" + className + " in line >" + line + " ; \"" + msg + "\"";
    }
}
