package com.example.TradingSystem.Client;

public class ApiClient {

    private static String urlbase = "http://localhost:8080/api/" ;

    public static void main(String[] args) {
        Client client = new Client();
        client.getTest1();

    }
}
