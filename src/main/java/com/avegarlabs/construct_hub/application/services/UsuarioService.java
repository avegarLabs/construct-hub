package com.avegarlabs.construct_hub.application.services;

import com.avegarlabs.construct_hub.application.dto.SignupRequest;
import com.avegarlabs.construct_hub.domain.model.Usuario;
import com.avegarlabs.construct_hub.domain.repositories.UsuarioRepository;
import com.avegarlabs.construct_hub.infrastructure.utils.UserDetailsImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return UserDetailsImpl.build(usuario);
    }

    public Usuario crearUsuario(SignupRequest signupRequest) {
        if (usuarioRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        if (usuarioRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(signupRequest.getUsername());
        usuario.setEmail(signupRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        usuario.setRol(signupRequest.getRol());
        usuario.setCargo(signupRequest.getCargo());

        return usuarioRepository.save(usuario);
    }
}
