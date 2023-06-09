package cl.constructorasaavedra.cotizacion.repository;

import cl.constructorasaavedra.cotizacion.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    // Buscar todos los items según trabajo Id
    @Query("from Item i where i.trabajo.id = :trbjId")
    List<Item> findByTrabajoId(@Param("trbjId") Integer id);


}
