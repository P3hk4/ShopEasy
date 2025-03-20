package base.Service;

import base.Entity.Order;
import base.Entity.Shipping;
import base.Repository.ShippingRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShippingService {

    private final ShippingRepository shippingRepository;

    public ShippingService(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    public Shipping getShippingByShippingId(int id){
        return shippingRepository.findShippingByShippingId(id);
    }

    public List<Shipping> getShippingsBy(){
        return shippingRepository.findShippingsBy();
    }

    public List<Shipping> getShippingsByClientId(int id){
        return shippingRepository.findShippingsByClientId(id);
    }

    public List<Shipping> getShippingsByAddress(String address){
        return shippingRepository.findShippingsByAddress(address);
    }

    public List<Shipping> getShippingsByCity(String city){
        return shippingRepository.findShippingsByCity(city);
    }

    public List<Shipping> getShippingsByClientIdAndAddress(int id, String address){
        return shippingRepository.findShippingsByClientIdAndAddress(id,address);
    }

    public void saveShipping(Shipping shipping){
        try {
            shippingRepository.save(shipping);
        } catch (StaleObjectStateException e) {
            System.out.println("Конфликт при сохранении");
        }
    }

    public void saveAllShippings(ArrayList<Shipping> shippings) {
        shippingRepository.saveAll(shippings);
    }

    public List<Integer> getAllShippingIds(){
        return shippingRepository.findAllShippingIds();
    }

    public void deleteShippingById(int id){
        shippingRepository.delete(getShippingByShippingId(id));
    }

}
