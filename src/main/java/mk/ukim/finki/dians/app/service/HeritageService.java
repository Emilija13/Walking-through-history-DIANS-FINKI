package mk.ukim.finki.dians.app.service;

import mk.ukim.finki.dians.app.model.Heritage;

import java.util.List;
import java.util.Optional;

public interface HeritageService {
    public List<Heritage> findAll();
    public List<String> findAllCities();
    public List<String> findAllCategories();
    public Optional<Heritage> findById(Long id);
    public void save(String name, String city, String category, Double lat, Double lon);
    public void update(Long id, String name, String city, String category, Double lat, Double lon);
    public List<Heritage> search(String name, String city, String category);
    public void deleteHeritages(List<Long> heritageIds);
}
