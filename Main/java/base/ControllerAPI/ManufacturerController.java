package base.ControllerAPI;

import base.DTO.ManufacturerDTO;
import base.DTO.MapperDTO;
import base.Entity.Manufacturer;
import base.Service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private MapperDTO mapperDTO;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ManufacturerDTO>> getAllManufacturers(){
        List<ManufacturerDTO> manufacturers = manufacturerService.getAllManufacturers()
                .stream().map(mapperDTO::entityToDTO).collect(Collectors.toList());
        return !manufacturers.isEmpty()
                ? new ResponseEntity<>(manufacturers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ManufacturerDTO> getManufacturerById(@PathVariable int id){
        ManufacturerDTO manufacturer = mapperDTO.entityToDTO(manufacturerService.getManufacturerById(id));
        return manufacturer != null
                ? new ResponseEntity<>(manufacturer, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/country/{country}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ManufacturerDTO>> getAllManufacturersByCountry(@PathVariable String country){
        List<ManufacturerDTO> manufacturers = manufacturerService.getAllManufacturersByCountry(country)
                .stream().map(mapperDTO::entityToDTO).collect(Collectors.toList());
        return !manufacturers.isEmpty()
                ? new ResponseEntity<>(manufacturers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Manufacturer save(@RequestBody Manufacturer manufacturer) {
        manufacturerService.saveManufacturer(manufacturer);
        return manufacturer;
    }

    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteManufacturerById(@PathVariable(name = "id") int id) {
        try {
            manufacturerService.deleteManufacturerById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Производитель успено удален");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении производителя: " + e.getMessage());
        }
    }

}
