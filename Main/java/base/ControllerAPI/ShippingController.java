package base.ControllerAPI;

import base.DTO.ClientDTO;
import base.DTO.MapperDTO;
import base.DTO.ShippingDTO;
import base.Entity.Shipping;
import base.Service.SecurityService.MyClientDetails;
import base.Service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shippings")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private MapperDTO mapperDTO;

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ShippingDTO>> getMyShippings(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyClientDetails myClientDetails = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            myClientDetails = (MyClientDetails) authentication.getPrincipal();
        }
        List<ShippingDTO> shippings = shippingService.getShippingsByClientId(myClientDetails.getClient().getClientId())
                .stream().map(mapperDTO::entityToDTO).collect(Collectors.toList());
        return !shippings.isEmpty()
                ? new ResponseEntity<>(shippings, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ShippingDTO>> getAllShippings() {
        List<ShippingDTO> shippings = shippingService.getAllShippings().stream()
                .map(mapperDTO::entityToDTO).collect(Collectors.toList());
        return !shippings.isEmpty()
                ? new ResponseEntity<>(shippings, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShippingDTO> getShippingById(@PathVariable(name = "id") int id) {
        ShippingDTO shipping = mapperDTO.entityToDTO(shippingService.getShippingByShippingId(id));
        return shipping != null
                ? new ResponseEntity<>(shipping, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/clients/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ShippingDTO>> getShippingsByClientsId(@PathVariable(name = "id") int id) {
        List<ShippingDTO> shippings = shippingService.getShippingsByClientId(id)
                .stream().map(mapperDTO::entityToDTO).collect(Collectors.toList());
        return shippings != null
                ? new ResponseEntity<>(shippings, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/city/{city}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ShippingDTO>> getShippingsByCity(@PathVariable(name = "city") String city) {
        List<ShippingDTO> shippings = shippingService.getShippingsByCity(city)
                .stream().map(mapperDTO::entityToDTO).collect(Collectors.toList());
        return shippings != null
                ? new ResponseEntity<>(shippings, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Shipping save(@RequestBody Shipping shipping) {
        shippingService.saveShipping(shipping);
        return shipping;
    }

    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable(name = "id") int id) {
        try {
            shippingService.deleteShippingById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Адрес доставки успено удалена");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении адреса: " + e.getMessage());
        }
    }
}
