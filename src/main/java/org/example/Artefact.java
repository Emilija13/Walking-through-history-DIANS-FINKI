package org.example;

import java.util.Objects;

public class Artefact{
    private String id;
    private String name;
    private String city;
    private String category;
    private String lat;
    private String lon;

    public Artefact(String id, String lat, String lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }


    @Override
    public String toString() {
        return String.format("%-70s %15s %40s", name, city, category);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCategory() {
        return category;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        //if (this == o) return true;
        //if (o == null || getClass() != o.getClass()) return false;
        Artefact artefact = (Artefact) o;
        return name.equals(artefact.name) && category.equals(artefact.category) && city.equals(artefact.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, city);
    }
}
