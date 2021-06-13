package TradingSystem.Server;

public class JsonInitReader {
    private Boolean local;
    private String urlLocal;
    private String userNameLocal;
    private String passwordLocal;
    private String urlRemote;
    private String userNameRemote;
    private String passwordRemote;
    private String ddlAuto;
    private String showSql;
    private String dialect;
    private String formatSql;
    private JsonUser admin;
    private Boolean externalState;
    private Boolean externalServices;
    //private Boolean LoadTest;

    public String getUrl() {
        if (local){
            return urlLocal;
        }
        else {
            return urlRemote;
        }
    }

    public String getUserName() {
        if (local){
            return userNameLocal;
        }
        else {
            return userNameRemote;
        }
    }

    public String getPassword() {
        if (local){
            return passwordLocal;
        }
        else {
            return passwordRemote;
        }
    }

    public String getDdlAuto() {
        return ddlAuto;
    }

    public String getShowSql() {
        return showSql;
    }

    public String getDialect() {
        return dialect;
    }

    public String getFormatSql() {
        return formatSql;
    }

    public JsonUser getAdmin() {
        return admin;
    }

    public Boolean getExternalState() {
        return externalState;
    }

    public Boolean getLocal() {
        return local;
    }

    public void setLocal(Boolean local) {
        this.local = local;
    }

    public String getUrlLocal() {
        return urlLocal;
    }

    public String getUserNameLocal() {
        return userNameLocal;
    }

    public String getPasswordLocal() {
        return passwordLocal;
    }

    public String getUrlRemote() {
        return urlRemote;
    }

    public String getUserNameRemote() {
        return userNameRemote;
    }

    public String getPasswordRemote() {
        return passwordRemote;
    }

    public Boolean getExternalServices() { return externalServices; }

//    public Boolean getLoadTest() {
//        return LoadTest;
//    }

    @Override
    public String toString() {
        return "ReadJson{" +
                "url='" + getUrl() + '\'' +
                ", userName='" + getUserName() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", ddlAuto='" + ddlAuto + '\'' +
                ", showSql='" + showSql + '\'' +
                ", dialect='" + dialect + '\'' +
                ", formatSql='" + formatSql + '\'' +
                '}';
    }
}

