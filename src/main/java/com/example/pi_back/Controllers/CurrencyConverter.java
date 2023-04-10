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

    private static final String API_KEY = "5ea398464ed909ccf53aee2d"; // Mettez votre clé API ici

    private static final Map<String, BigDecimal> CURRENCY_RATES = new HashMap<>();

    public static BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency, String apiKey) {
        BigDecimal conversionRate = getConversionRate(fromCurrency, toCurrency);
        BigDecimal convertedAmount = amount.multiply(conversionRate);
        return round(convertedAmount, 2);
    }

    private static BigDecimal getConversionRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return BigDecimal.ONE;
        }

        String key = fromCurrency + "_" + toCurrency;
        if (CURRENCY_RATES.containsKey(key)) {
            return CURRENCY_RATES.get(key);
        }

        String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/" + key;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            BigDecimal conversionRate = new BigDecimal(response.body().replaceAll("[^0-9.]", ""));
            CURRENCY_RATES.put(key, conversionRate);
            return conversionRate;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    private static BigDecimal round(BigDecimal value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        return value.setScale(places, RoundingMode.HALF_UP);
    }

}
