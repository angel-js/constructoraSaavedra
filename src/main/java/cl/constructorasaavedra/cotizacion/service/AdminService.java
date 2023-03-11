package cl.constructorasaavedra.cotizacion.service;

import cl.constructorasaavedra.cotizacion.model.Usuario;
import cl.constructorasaavedra.cotizacion.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario saveUser(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
