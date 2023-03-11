package cl.constructorasaavedra.cotizacion.repository;

import cl.constructorasaavedra.cotizacion.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    Authority findByName(String Name);

}
