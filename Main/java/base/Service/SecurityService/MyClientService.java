package base.Service.SecurityService;

import base.Entity.Client;
import base.Repository.ClientRepository;
import base.Service.SecurityService.MyClientDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyClientService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Client> client = clientRepository.getSecurityClientByUsername(username);
        return client.map(MyClientDetails::new).orElseThrow( () -> new UsernameNotFoundException(username + "There is not such user in Data base"));
    }
}
