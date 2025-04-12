package base.ControllerAPI;

import base.ControllerWeb.JwtTokenProvider;
import base.DTO.AuthDTO;
import base.DTO.JwtAuthResponse;
import base.Service.SecurityService.MyClientDetails;
import base.Service.SecurityService.MyClientService;
import org.springframework.http.ResponseEntity;
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

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
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


//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
//        // Реализация регистрации пользователя
//        return ResponseEntity.ok("User registered successfully");
//    }
}


