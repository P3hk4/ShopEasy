package base.ControllerAPI;

import base.DTO.ManufacturerDTO;
import base.Entity.Manufacturer;
import base.Service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping
    public ResponseEntity<List<ManufacturerDTO>> getAllManufacturers(){
        List<ManufacturerDTO> manufacturers = manufacturerService.getAllManufacturersDTO();
        return manufacturers != null && !manufacturers.isEmpty()
                ? new ResponseEntity<>(manufacturers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ManufacturerDTO> getManufacturerById(@PathVariable int id){
        ManufacturerDTO manufacturer = manufacturerService.getManufacturerDTOById(id);
        return manufacturer != null
                ? new ResponseEntity<>(manufacturer, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<ManufacturerDTO>> getManufacturersByCountry(@PathVariable String country){
        List<ManufacturerDTO> manufacturers = manufacturerService.getAllManufacturersDTOByCountry(country);
        return manufacturers != null && !manufacturers.isEmpty()
                ? new ResponseEntity<>(manufacturers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Manufacturer save(@RequestBody Manufacturer manufacturer) {
        manufacturerService.saveManufacturer(manufacturer);
        return manufacturer;
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteManufacturerById(@PathVariable(name = "id") int id) {
        try {
            manufacturerService.deleteManufacturerById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Производитель успено удален");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении производителя: " + e.getMessage());
        }
    }

}
