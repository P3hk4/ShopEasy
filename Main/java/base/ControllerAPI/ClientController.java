package base.ControllerAPI;

import base.DTO.ClientDTO;
import base.Entity.Client;
import base.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        final List<ClientDTO> clients = clientService.getAllClientsDTO();
        return clients != null && !clients.isEmpty()
                ? new ResponseEntity<>(clients, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable(name = "id") int id) {
        ClientDTO client = clientService.getClientDTOById(id);
        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/all")
    public ResponseEntity<List<Integer>> getAllClientIds() {
        List<Integer> ids = clientService.getAllClientsIds();
        return ids != null && !ids.isEmpty()
                ? new ResponseEntity<>(ids, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ClientDTO> getClientByUsername(@PathVariable(name = "username") String username) {
        ClientDTO client = clientService.getClientDTOByUsername(username);
        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClientDTO> getClientByEmail(@PathVariable(name = "email") String email) {
        ClientDTO client = clientService.getClientDTOByEmail(email);
        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Client> save(@RequestBody Client client) {
        clientService.saveClient(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Client> update(@RequestBody Client client) {
        clientService.updateClient(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteClientById(@PathVariable(name = "id") int id) {
        try {
            clientService.deleteClientById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Клиент успено удален");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении клиента: " + e.getMessage());
        }
    }
}
