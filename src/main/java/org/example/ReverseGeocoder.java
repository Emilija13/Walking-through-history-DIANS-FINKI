package org.example;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Map;

class ReverseGeocoder {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/reverse";
    private static final String FORMAT = "json";
    public static String reverseGeocode(double latitude, double longitude, Map<String, String>cities) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = NOMINATIM_URL + "?format=" + FORMAT + "&lat=" + latitude + "&lon=" + longitude;
            HttpGet httpGet = new HttpGet(url);
            JSONObject response = new JSONObject(EntityUtils.toString(httpClient.execute(httpGet).getEntity()));

            // Extract the city name from the response
            String city = response.getJSONObject("address").optString("city");
            if(city.isEmpty()){
                String village = response.getJSONObject("address").optString("village");
                String municipality = response.getJSONObject("address").optString("municipality");
                if(cities.containsKey(village)){
                    String city1 = cities.get(village);
                    return city1;
                }
                else if(cities.containsKey(municipality)){
                    String city2 = cities.get(municipality);
                    return city2;
                }
                return "";
            }

            return city;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
