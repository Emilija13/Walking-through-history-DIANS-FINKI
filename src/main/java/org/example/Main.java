package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class CitiesMap{
    public static Map<String, String> loadCities(String text) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(text));
        Map<String, String> cities = new LinkedHashMap<>();
        List<String> lines = br.lines().toList();
        for(String line : lines){
            String[] parts = line.split(",");
            String city = parts[0];
            for(int i=1; i< parts.length; i++){
                String area = parts[i].substring(1);
                cities.put(area, city);
            }
        }
        return cities;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, String> cities = CitiesMap.loadCities("src/gradovi.csv");

        /*------------------FILTERS--------------------------------*/
        Filter<JsonNode> removeUselessKeysFromProperties = new Filter<JsonNode>() {
            @Override
            public JsonNode execute(JsonNode jsonNode) {
                ObjectNode objectNode = (ObjectNode) jsonNode.get("properties");
                if(objectNode.get("name")==null){
                    //objectNode = objectNode.retain("name:en");
                    objectNode = objectNode.retain("name");
                    objectNode.put("name", "");
                }else{
                    objectNode = objectNode.retain("name");
                }
                objectNode.set("id",  jsonNode.get("id"));
                objectNode.set("geometry", jsonNode.get("geometry"));
                return objectNode;
            }
        };


        Filter<JsonNode> addCityOrMunicipalityForGeometry = new Filter<JsonNode>() {
            @Override
            public JsonNode execute(JsonNode jsonNode) {
                ObjectNode objectNode = (ObjectNode) jsonNode;
                if (objectNode.get("geometry") == null || !objectNode.get("geometry").has("coordinates")){
                    String city = "";
                    objectNode.put("city", city);
                    return objectNode;
                }

                Double longitude;
                Double latitude;
                String type = objectNode.get("geometry").get("type").asText();

                if(type.equals("Point")){ //JA BARAME PRVATA KOORDINATA ZA DA NAJDEME GRAD
                    longitude = objectNode.get("geometry").get("coordinates").get(0).asDouble();
                    latitude = objectNode.get("geometry").get("coordinates").get(1).asDouble();
                }else if(type.equals("LineString")){
                    longitude = objectNode.get("geometry").get("coordinates").get(0).get(0).asDouble();
                    latitude = objectNode.get("geometry").get("coordinates").get(0).get(1).asDouble();
                }
                else if(type.equals("Polygon")){
                    longitude = objectNode.get("geometry").get("coordinates").get(0).get(0).get(0).asDouble();
                    latitude = objectNode.get("geometry").get("coordinates").get(0).get(0).get(1).asDouble();
                }else{
                    longitude = objectNode.get("geometry").get("coordinates").get(0).get(0).get(0).get(0).asDouble();
                    latitude = objectNode.get("geometry").get("coordinates").get(0).get(0).get(0).get(1).asDouble();
                }
                objectNode.put("lon", longitude);
                objectNode.put("lat", latitude);

                String city = ReverseGeocoder.reverseGeocode(latitude, longitude, cities); //KLASA STO IZVRSUVA REVERSE GEOCODING
                objectNode.put("city", city);                               //KOORDINATA -> GRAD
                return objectNode;
            }
        };
        /*------------------FILTERS--------------------------------*/
        /*-------------------PIPE-------------------------*/

        Pipe<JsonNode> pipe1 = new Pipe<>();
        pipe1.addFilter(removeUselessKeysFromProperties);
        pipe1.addFilter(addCityOrMunicipalityForGeometry);
        /*-------------------PIPE-------------------------*/

        Map<String, String> jsonsTypes = new LinkedHashMap<>();
        jsonsTypes.put("arheoloski.json", "Археолошко наоѓалиште/Тврдина/Кула");
        jsonsTypes.put("crkvi_manastiri.json", "Црква/Манастир");
        jsonsTypes.put("muzei_galerii.json", "Музеј/Галерија");
        jsonsTypes.put("spomenici.json", "Споменик/Градба");

        ObjectMapper objectMapper = new ObjectMapper();
        List<Artefact> artefactList = new ArrayList<>();

        Gson gson = new Gson();

        for(String json : jsonsTypes.keySet()){
            JsonNode rootNode = objectMapper.readTree(new File("src/"+json));
            JsonNode features = rootNode.get("features");
            int i=0;
            for (JsonNode feature : features) {
                JsonNode node = pipe1.runFilters(feature); //posle node ke cuva samo id, ime, geometry i grad
                ObjectNode on = (ObjectNode) node;

                Artefact artefact = new Artefact(node.get("id").asText(),
                        node.get("lat").asText(), node.get("lon").asText());

                if(node.get("name").asText()==null){
                    artefact.setName("");
                }
                else{
                    artefact.setName(node.get("name").asText());
                }
                if(node.get("city").asText()==null){
                    artefact.setCity("");
                }
                else{
                    artefact.setCity(node.get("city").asText());
                }

                artefact.setCategory(jsonsTypes.get(json));
                artefactList.add(artefact);
            }
        }

        /*------------------FILTERS----------------------------------*/
        Filter<List<Artefact>> removeArtefactsWithoutName =  l -> l.stream()
                .filter(a-> !a.getName().isEmpty()).collect(Collectors.toList());

        Filter<List<Artefact>> removeArtefactsWithoutCity = l -> l.stream()
                .filter(a-> !a.getCity().isEmpty()).collect(Collectors.toList());

        Filter<List<Artefact>> removeDuplicateNames = l -> l.stream()
                .distinct().collect(Collectors.toList());

        Filter<List<Artefact>> editCityName = new Filter<List<Artefact>>() {
            @Override
            public List<Artefact> execute(List<Artefact> input) {
                for(Artefact a : input){
                    if(a.getCity().equals("Прилеп (Околина)")){
                        a.setCity("Прилеп");
                    }
                }
                return input;
            }
        };
        /*------------------FILTERS----------------------------------*/
        /*-------------------PIPE-------------------------*/
        Pipe<List<Artefact>> pipe2 = new Pipe<>();
        pipe2.addFilter(removeArtefactsWithoutName);
        pipe2.addFilter(removeArtefactsWithoutCity);
        pipe2.addFilter(removeDuplicateNames);
        pipe2.addFilter(editCityName);
        /*--------------------PIPE-----------------------------*/

        artefactList = pipe2.runFilters(artefactList);

        StringBuilder sb = new StringBuilder();
        sb.append(gson.toJson(artefactList));

        try (FileWriter fileWriter = new FileWriter("filtered_heritage.json")) {
            fileWriter.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //artefactList.forEach(System.out::println);
        //System.out.println(artefactList.size());
    }
}