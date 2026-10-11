package com.avegarlabs.construct_hub;

import com.avegarlabs.construct_hub.application.dto.SignupRequest;
import com.avegarlabs.construct_hub.application.services.IAppConfigService;
import com.avegarlabs.construct_hub.application.services.UsuarioService;
import com.avegarlabs.construct_hub.domain.model.Usuario;
import com.avegarlabs.construct_hub.infrastructure.config.EnvLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConstructHubApplication {

	public static void main(String[] args) {
		EnvLoader.load();
		SpringApplication.run(ConstructHubApplication.class, args);
	}


	@Bean
	CommandLineRunner init(UsuarioService usuarioService, IAppConfigService appConfigService) {
		return args -> {
			if (!usuarioService.existeUsuarioByUsername("admin")) {
				SignupRequest admin = new SignupRequest();
				admin.setUsername("admin");
				admin.setEmail("vegaramirezalfredo@gmail.com");
				admin.setPassword("admin"); // ← SIN codificar
				admin.setRol(Usuario.Rol.ADMIN);
				admin.setCargo("Administrador del Sistema");
				usuarioService.crearUsuario(admin);
			}

			appConfigService.initDefaultConfigIfMissing();
		};
	}

}
