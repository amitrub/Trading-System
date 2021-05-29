package TradingSystem.Server;

import java.util.List;
import java.util.Map;

public class JsonStateReader {
    public List<JsonUser> register;
    public List<Map<String,String>> login;
    public List<Map<String,String>> open_store;
    public List<Map<String,Object>> add_item;
    public List<Map<String,Object>> add_manager;

    public List<JsonUser> getRegister() {
        return register;
    }

    public List<Map<String, String>> getLogin() {
        return login;
    }

    public List<Map<String, String>> getOpen_store() {
        return open_store;
    }

    public List<Map<String, Object>> getAdd_item() {
        return add_item;
    }

    public List<Map<String, Object>> getAdd_manager() {
        return add_manager;
    }

    @Override
    public String toString() {
        return "JsonStateReader{" +
                "register=" + register +
                ", login=" + login +
                ", open_store=" + open_store +
                ", add_item=" + add_item +
                ", add_manager=" + add_manager +
                '}';
    }
}

