package com.avegarlabs.construct_hub.application.dto;


import com.avegarlabs.construct_hub.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private Usuario.Rol rol;
    private String cargo;
}
