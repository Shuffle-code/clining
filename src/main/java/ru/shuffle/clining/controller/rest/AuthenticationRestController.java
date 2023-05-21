package ru.shuffle.clining.controller.rest;
import io.swagger.v3.oas.annotations.tags.Tag;
import liquibase.pro.packaged.R;
import liquibase.pro.packaged.S;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shuffle.clining.dao.security.AccountRoleDao;
import ru.shuffle.clining.dao.security.AccountUserDao;
import ru.shuffle.clining.dto.AuthenticationUserDto;
import ru.shuffle.clining.entity.security.AccountUser;
import ru.shuffle.clining.security.jwt.JwtTokenProvider;
import ru.shuffle.clining.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/auth/")
@Tag(name = "Контроллер, обеспечивающий работу с аутентификацией")
public class AuthenticationRestController {

    private final UserService userService;
    private final AccountUserDao accountUserDao;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationUserDto authenticationUserDto) {
        try {
            final String username = authenticationUserDto.getUsername();
            final String pass = authenticationUserDto.getPassword();
            AccountUser accountUser = userService.findByUsername(username);
            String token = jwtTokenProvider.createToken(username, accountUser.getRoles());
            System.out.println(authenticationUserDto.getPassword());
            if (authenticationUserDto.getPassword().equals("12345")){
                Map<Object, Object> response = new HashMap<>();
                response.put("username", username);
                response.put("token", token);
                response.get(token);
                return ResponseEntity.ok(response);
            }
//
//            Map<Object, Object> response = new HashMap<>();
//            response.put("username", username);
//            response.put("token", token);
//            response.get(token);

           return ResponseEntity.noContent().build();
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
