package base.Service;


import base.Entity.Manufacturer;
import base.Repository.ManufacturerRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerService(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    public Manufacturer getManufacturerById(int id){
        return manufacturerRepository.findManufacturerByManufacturerId(id);
    }

    public Manufacturer getManufacturerByName(String name){
        return manufacturerRepository.findManufacturerByName(name);
    }

    public List<Manufacturer> getManufacturersByCountry(String country){
        return manufacturerRepository.findManufacturersByCountry(country);
    }

    public List<Manufacturer> getAllManufacturers(){
        return manufacturerRepository.findAllManufacturersBy();
    }

    public void saveManufacturer(Manufacturer manufacturer){
        try {
            manufacturerRepository.save(manufacturer);
        } catch (StaleObjectStateException e) {
            System.out.println("Конфликт при сохранении");
        }
    }

    public void saveAllManufacturers(ArrayList<Manufacturer> manufacturers){
        try {
            manufacturerRepository.saveAll(manufacturers);
        } catch (StaleObjectStateException e) {
            System.out.println("Конфликт при сохранении");
        }
    }

    public List<Integer> getAllManufacturerIds(){
        return manufacturerRepository.findAllManufacturerIds();
    }

    public void deleteManufacturerById(int id){
        manufacturerRepository.delete(getManufacturerById(id));
    }
}
