package com.example.pi_back.Controllers;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PowerBIReport {

    public static void main(String[] args) {

        // URL du rapport Power BI
        String reportUrl = "https://app.powerbi.com/reportEmbed?reportId=f8b757e9-cd94-45da-96ef-e672b8d8f7cd&autoAuth=true&ctid=513486ec-6643-4f17-a508-76478311be42";

        // Identifiants d'authentification
        String clientId = "cdb64f5d-936c-47c4-b489-839b8e9c7e07";
        String clientSecret = "jHy8Q~DZec-K43sdooWSfTQ.rSPn_v6e7LW4HbSS";

        // Obtention du token d'authentification
        String token = getToken(clientId, clientSecret);

        // Requête GET pour récupérer les données du rapport
        String data = getData(reportUrl, token);

        // Affichage des données dans la console
        System.out.println(data);
    }

    // Fonction pour obtenir le token d'authentification
    public static String getToken(String clientId, String clientSecret) {

        try {
            // URL de l'API Azure AD pour obtenir le token
            URL url = new URL("https://login.microsoftonline.com/common/oauth2/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Paramètres de la requête
            String params = "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret + "&resource=https://analysis.windows.net/powerbi/api";

            // Envoi des paramètres dans le corps de la requête
            conn.getOutputStream().write(params.getBytes(StandardCharsets.UTF_8));

            // Récupération de la réponse de l'API
            InputStream inputStream = conn.getInputStream();
            String response = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            // Extrait le token de la réponse
            int startIndex = response.indexOf("access_token\":\"") + 15;
            int endIndex = response.indexOf("\",\"token_type\"");
            return response.substring(startIndex, endIndex);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Fonction pour récupérer les données du rapport
    public static String getData(String reportUrl, String token) {

        try {
            // URL de l'API Power BI pour récupérer les données du rapport
            URL url = new URL("https://api.powerbi.com/v1.0/myorg/reports/" + getReportId(reportUrl) + "/data");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Ajout du token d'authentification dans l'en-tête de la requête
            conn.setRequestProperty("Authorization", "Bearer " + token);

            // Récupération de la réponse de l'API
            InputStream inputStream = conn.getInputStream();
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getReportId(String reportUrl) {
        int startIndex = reportUrl.indexOf("reportId=") + 9;
        int endIndex = reportUrl.indexOf("&", startIndex);
        if (endIndex == -1) {
            endIndex = reportUrl.length();
        }
        return reportUrl.substring(startIndex, endIndex);
    }
}