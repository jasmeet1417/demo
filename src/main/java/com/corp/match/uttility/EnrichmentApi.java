package com.corp.match.uttility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EnrichmentApi {

    private static final String API_KEY = "uDGiG2UhmpjlqMJlU8OGPg";
    private static final String BASE_URL = "https://api.apollo.io/api/v1/organizations/enrich?domain=";

    public static String getCompanyNameFromDomain(String domain) {
        try {
            String urlStr = BASE_URL + domain;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("x-api-key", API_KEY);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                String json = response.toString();
                return extractField(json, "\"name\":\"");
            }
        } catch (Exception e) {
            System.out.println("Error for domain " + domain + ": " + e.getMessage());
        }
        return null;
    }

    private static String extractField(String json, String key) {
        int start = json.indexOf(key);
        if (start == -1) return null;
        start += key.length();
        int end = json.indexOf("\"", start);
        return end > start ? json.substring(start, end) : null;
    }
}
 