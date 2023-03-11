package cl.constructorasaavedra.cotizacion.repository;

import cl.constructorasaavedra.cotizacion.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    public List<Rol> findAll();
}
