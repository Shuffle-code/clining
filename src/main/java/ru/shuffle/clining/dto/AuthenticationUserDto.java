package ru.shuffle.clining.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationUserDto {

    private String username;
    private String password;
}
