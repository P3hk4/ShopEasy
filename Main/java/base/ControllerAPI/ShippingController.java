package base.ControllerAPI;

import base.Entity.Shipping;
import base.Service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shippings")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @GetMapping
    public ResponseEntity<List<Shipping>> getAllShippings() {
        List<Shipping> shippings = shippingService.getAllShippings();
        return shippings != null && !shippings.isEmpty()
                ? new ResponseEntity<>(shippings, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Shipping> getShippingById(@PathVariable(name = "id") int id) {
        Shipping shipping = shippingService.getShippingByShippingId(id);
        return shipping != null
                ? new ResponseEntity<>(shipping, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/clients/id/{id}")
    public ResponseEntity<List<Shipping>> getShippingsByClientsId(@PathVariable(name = "id") int id) {
        List<Shipping> shippings = shippingService.getShippingsByClientId(id);
        return shippings != null
                ? new ResponseEntity<>(shippings, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Shipping>> getShippingsByCity(@PathVariable(name = "city") String city) {
        List<Shipping> shippings = shippingService.getShippingsByCity(city);
        return shippings != null
                ? new ResponseEntity<>(shippings, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Shipping save(@RequestBody Shipping shipping) {
        shippingService.saveShipping(shipping);
        return shipping;
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") int id) {
        try {
            shippingService.deleteShippingById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Адрес доставки успено удалена");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении адреса: " + e.getMessage());
        }
    }
}
