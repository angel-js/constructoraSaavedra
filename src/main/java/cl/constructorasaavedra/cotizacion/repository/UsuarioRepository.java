package cl.constructorasaavedra.cotizacion.repository;

import cl.constructorasaavedra.cotizacion.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Usuario findByEmail(String email);

    List<Usuario> findByNameContainingIgnoreCaseOrLastnameContainingIgnoreCase(String name, String lastname);

    /**
    @Query("from Usuario us order by us.id")
    List<Usuario> findAll();

    Usuario findByUsername(String username);

    Usuario findByUsernameAndPassword(String username, String passwrod);**/
}
