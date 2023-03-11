package cl.constructorasaavedra.cotizacion.service;

import cl.constructorasaavedra.cotizacion.component.RolGrantedAuthority;
import cl.constructorasaavedra.cotizacion.dto.UsuarioRegistroDto;
import cl.constructorasaavedra.cotizacion.interfaces.UsuarioServicio;
import cl.constructorasaavedra.cotizacion.model.Usuario;
import cl.constructorasaavedra.cotizacion.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {
    @Autowired
    private BCryptPasswordService passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario guardar(UsuarioRegistroDto registroDto) {
        Usuario usuario = new Usuario(registroDto.getName(),
                registroDto.getLastname(), registroDto.getEmail(),
                passwordEncoder.passwordEncoder().encode(registroDto.getPassword()),
                registroDto.getRol());
        return usuarioRepository.save(usuario);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario o password inválidos");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new RolGrantedAuthority(usuario.getRol().getName()));

        return new User(usuario.getEmail(),usuario.getPassword(), authorities);
    }

}
