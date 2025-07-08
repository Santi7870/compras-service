package com.agrolink360.compras_service.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RoutingService {

    private final String apiKey = "5b3ce3597851110001cf6248810faad7f1e24eb6bd5910887147f219";

    public JSONObject calcularRuta(double origenLon, double origenLat, double destinoLon, double destinoLat) {
        String url = "https://api.openrouteservice.org/v2/directions/driving-car/geojson";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", apiKey);

        JSONObject body = new JSONObject();
        JSONArray coords = new JSONArray();
        coords.put(new JSONArray(new double[]{origenLon, origenLat}));
        coords.put(new JSONArray(new double[]{destinoLon, destinoLat}));
        body.put("coordinates", coords);

        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return new JSONObject(response.getBody());
    }

    public double[] obtenerCoordenadas(String ciudad) {
        String url = "https://api.openrouteservice.org/geocode/search?api_key=" + apiKey + "&text=" + ciudad + "&boundary.country=EC";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONObject json = new JSONObject(response.getBody());

        JSONArray features = json.getJSONArray("features");
        if (features.length() == 0) {
            throw new RuntimeException("Ciudad no encontrada: " + ciudad);
        }

        JSONArray coords = features.getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONArray("coordinates");

        double lon = coords.getDouble(0);
        double lat = coords.getDouble(1);
        return new double[]{lon, lat};
    }
}


