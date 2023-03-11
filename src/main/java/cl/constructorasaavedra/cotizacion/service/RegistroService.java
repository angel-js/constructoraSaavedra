package cl.constructorasaavedra.cotizacion.service;

import cl.constructorasaavedra.cotizacion.model.Rol;
import cl.constructorasaavedra.cotizacion.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> mostrarRoles() {
        return rolRepository.findAll();
    }
}
