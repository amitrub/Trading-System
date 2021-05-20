package TradingSystem.Server.DomainLayer.ExternalServices;

public class AddressInfo {

    private String name;
    private String country;
    private String city;
    private String address;
    private String zip;

    public AddressInfo(String name, String country, String city, String address, String zip) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.address = address;
        this.zip = zip;
    }

    public AddressInfo() {
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getZip() {
        return zip;
    }
}
