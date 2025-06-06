package base.ControllerAPI;

import base.DTO.ClientDTO;
import base.DTO.MapperDTO;
import base.Entity.Client;
import base.Service.ClientService;
import base.Service.SecurityService.MyClientDetails;
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
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private MapperDTO mapperDTO;

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClientDTO> getMyClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyClientDetails myClientDetails = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            myClientDetails = (MyClientDetails) authentication.getPrincipal();

        }
        ClientDTO client = mapperDTO.entityToDTO(myClientDetails.getClient());
        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> editMyClient(@RequestBody Client client){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyClientDetails myClientDetails = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            myClientDetails = (MyClientDetails) authentication.getPrincipal();
        }
        Client clientDetails = myClientDetails.getClient();
        clientService.updateClient(new Client(
                clientDetails.getClientId(),
                clientDetails.getUsername(),
                client.getEmail(),
                client.getPassword(),
                clientDetails.getRole(),
                client.getFirstName(),
                client.getLastName()
                ));
        return new ResponseEntity<>("Данные успешно изменены", HttpStatus.OK);
    }

    @PostMapping("/my/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@RequestBody String password){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyClientDetails myClientDetails = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            myClientDetails = (MyClientDetails) authentication.getPrincipal();
        }
        clientService.changePassword(myClientDetails.getClient(), password);
        return new ResponseEntity<>("Пароль успешно изменён", HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<ClientDTO> clientsDTO = clientService.getAllClients().stream()
                .map(mapperDTO::entityToDTO).collect(Collectors.toList());
        return !clientsDTO.isEmpty()
                ? new ResponseEntity<>(clientsDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable(name = "id") int id) {
        ClientDTO clientDTO = mapperDTO.entityToDTO(clientService.getClientById(id));
        return clientDTO != null
                ? new ResponseEntity<>(clientDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Integer>> getAllClientIds() {
        List<Integer> ids = clientService.getAllClientsIds();
        return ids != null && !ids.isEmpty()
                ? new ResponseEntity<>(ids, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientDTO> getClientByUsername(@PathVariable(name = "username") String username) {
        ClientDTO clientDTO = mapperDTO.entityToDTO(clientService.getClientByUsername(username));
        return clientDTO != null
                ? new ResponseEntity<>(clientDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientDTO> getClientByEmail(@PathVariable(name = "email") String email) {
        ClientDTO clientDTO = mapperDTO.entityToDTO(clientService.getClientByEmail(email));
        return clientDTO != null
                ? new ResponseEntity<>(clientDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Client> update(@RequestBody Client client) {
        clientService.updateClient(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteClientById(@PathVariable(name = "id") int id) {
        try {
            clientService.deleteClientById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Клиент успено удален");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении клиента: " + e.getMessage());
        }
    }

    @PostMapping("/login-check")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> loginCheck(@RequestBody String username){
        if (clientService.loginIsAvailable(username)) return ResponseEntity.status(HttpStatus.OK).body("Логин доступен дли регистрации");
        else return ResponseEntity.status(HttpStatus.FOUND).body("Логин уже существует");
    }
}
