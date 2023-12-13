package mk.ukim.finki.dians.app.service;

import mk.ukim.finki.dians.app.model.Heritage;
import mk.ukim.finki.dians.app.repository.HeritageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeritageService {

    @Autowired
    private HeritageRepository heritageRepository;
    //private final HeritageRepo heritageRepo;

    //public HeritageService(HeritageRepo heritageRepo) {
     //   this.heritageRepo = heritageRepo;
    //}

    public List<Heritage> findAll() {
        return heritageRepository.findAll();
    }

    /*public List<Heritage> findByName(String name) {
        return heritageRepo.findByName(name);
    }

    public List<Heritage> searchByCity(String city) {
        return heritageRepo.searchByCity(city);
    }

    public List<Heritage> searchByCategory(String category) {
        return heritageRepo.searchByCategory(category);
    }

    public List<Heritage> searchByNameAndCity(String name, String city) {
        return heritageRepo.searchByNameAndCity(name, city);
    }*/
    public List<String> findAllCities() {
        return heritageRepository.findAllCities();
    }
    public List<String> findAllCategories() {
        return heritageRepository.findAllCategories();
    }

    public List<Heritage> search(String name, String city, String category) {
       return heritageRepository.search(name, city, category);
    }
    public void deleteHeritages(List<Long> heritageIds) {
        for (Long heritageId : heritageIds) {
            heritageRepository.deleteById(heritageId);
        }
    }

}