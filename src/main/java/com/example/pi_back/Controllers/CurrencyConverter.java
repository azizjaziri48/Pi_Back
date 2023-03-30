package com.example.pi_back.Controllers;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {

    private static final String API_KEY = "f2ba7e8b2b225aff3756ea1a"; // Mettez votre clé API ici

    private static final Map<String, Double> CURRENCY_RATES = new HashMap<>();

    public static String convert(double amount, String fromCurrency, String toCurrency, String f2ba7e8b2b225aff3756ea1a) {
        double conversionRate = getConversionRate(fromCurrency, toCurrency);
        double convertedAmount = amount * conversionRate;
        return round(convertedAmount, 2).toString();
    }

    private static double getConversionRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return 1.0;
        }

        String key = fromCurrency + "_" + toCurrency;
        if (CURRENCY_RATES.containsKey(key)) {
            return CURRENCY_RATES.get(key);
        }

        String url = "https://v6.exchangerate-api.com/v6/" + key + "&compact=ultra&apiKey=" + API_KEY;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            double conversionRate = Double.parseDouble(response.body().replaceAll("[^0-9.]", ""));
            CURRENCY_RATES.put(key, conversionRate);
            return conversionRate;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private static BigDecimal round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd;
    }

}
