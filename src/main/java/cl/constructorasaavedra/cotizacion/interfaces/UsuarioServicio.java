package cl.constructorasaavedra.cotizacion.interfaces;

import cl.constructorasaavedra.cotizacion.dto.UsuarioRegistroDto;
import cl.constructorasaavedra.cotizacion.model.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioServicio extends UserDetailsService {

    public Usuario guardar(UsuarioRegistroDto registroDto);
}
