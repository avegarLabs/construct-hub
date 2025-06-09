package com.avegarlabs.construct_hub.application.dto;

import com.avegarlabs.construct_hub.domain.model.Usuario;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Usuario.Rol rol;
    private String cargo;



}


