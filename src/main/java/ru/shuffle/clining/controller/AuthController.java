package ru.shuffle.clining.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shuffle.clining.dao.security.ConfirmationCodeDao;
import ru.shuffle.clining.dto.UserDto;
import ru.shuffle.clining.entity.security.AccountUser;
import ru.shuffle.clining.entity.security.ConfirmationCode;
import ru.shuffle.clining.service.EmailService;
import ru.shuffle.clining.service.UserService;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Контроллер, аутентификация и регистрация")
public class AuthController {
    private final UserService userService;
    private final ConfirmationCodeDao confirmationCodeDao;
    private static UserDto thisUser;
    private final EmailService emailService;
    @Operation(summary = "страница аутентификация")
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login-form";
    }
    @Operation(summary = "страница регистрации")
    @GetMapping("/registration")
    public String registrationPage(Model model) {
//        captchaController.getCaptcha();
//        captchaGenerator.generateCaptcha();
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "auth/registration-form";
    }
    @Operation(summary = "отправка данных для регистрации")
    @PostMapping("/register")
    public String handleRegistrationForm(@Valid UserDto userDto, BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            return "auth/registration-form";
        }
        final String username = userDto.getUsername();
        try {
            userService.findByUsername(username);
            model.addAttribute("userDto", userDto);
            model.addAttribute("registrationError", "Пользователь с таким логином уже существует");
            return "auth/registration-form";
        } catch (UsernameNotFoundException ignored) {}

        thisUser = userService.register(userDto);
        model.addAttribute("username", username);

        String confirmationCode = userService.getConfirmationCode();

        new Thread(new Runnable() {
            @Override
            public void run() {
                emailService.sendConfirmationCode(confirmationCode, userDto.getEmail());
            }
        }).start();
        userService.generateConfirmationCode(thisUser, confirmationCode);
        model.addAttribute("id", thisUser.getId());
        return "auth/registration-confirmation";
    }
    @PostMapping("/confirmation")
    public String handleConfirmationForm(String code, Model model) {
        model.addAttribute("code", code);
        log.info("code: " + code);
        ConfirmationCode confirmationCodeBy_id = confirmationCodeDao.findConfirmationCodeByAccountUser_Id(thisUser.getId());
        if (confirmationCodeBy_id.equals(code)) {
            log.info(String.valueOf(confirmationCodeBy_id.equals(code)));
            AccountUser accountUser = confirmationCodeBy_id.getAccountUser();
            accountUser.setEnabled(true);
            accountUser.setAccountNonLocked(true);
            accountUser.setCredentialsNonExpired(true);
            accountUser.setAccountNonExpired(true);
            userService.update(accountUser);
            return "redirect:/auth/login";
        }
        return "auth/invalid-confirmation";
    }

    @PostMapping("/changeUsername")
    public String changeUsername() {
        thisUser.setUsername(String.format("%s_id%s_%s",
                thisUser.getUsername(), thisUser.getId(), "deleted"));
        userService.update(thisUser);
        userService.deleteById(thisUser.getId());
        return "redirect:/auth/login";
    }
}
