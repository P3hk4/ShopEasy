package base.ControllerAPI;

import base.ControllerWeb.JwtTokenProvider;
import base.DTO.AuthDTO;
import base.DTO.JwtAuthResponse;
import base.Entity.Client;
import base.Service.ClientService;
import base.Service.SecurityService.MyClientDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ClientService clientService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, ClientService clientService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.clientService = clientService;
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody AuthDTO authDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authDTO.getLogin(),
                    authDTO.getPassword()
                )
        );

        // 2. Установка аутентификации в контекст
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Генерация JWT токена
        String jwt = jwtTokenProvider.generateToken(authentication);

        // 4. Получение данных пользователя
        MyClientDetails client = (MyClientDetails) authentication.getPrincipal();

        // 5. Формирование ответа
        return ResponseEntity.ok(new JwtAuthResponse(
                jwt,
                client.getClient().getClientId(),
                client.getUsername(),
                client.getClient().getEmail(),
                client.getClient().getRole()
        ));
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> register(@RequestBody Client client) {
        clientService.saveClient(client);
       return ResponseEntity.ok("User registered successfully");
    }
}


