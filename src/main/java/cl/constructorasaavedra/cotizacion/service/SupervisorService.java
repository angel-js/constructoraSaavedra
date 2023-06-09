package cl.constructorasaavedra.cotizacion.service;

import cl.constructorasaavedra.cotizacion.exception.ResourceNotFoundException;
import cl.constructorasaavedra.cotizacion.model.Supervisor;
import cl.constructorasaavedra.cotizacion.repository.DepartamentoRepository;
import cl.constructorasaavedra.cotizacion.repository.GerenteRepository;
import cl.constructorasaavedra.cotizacion.repository.SupervisorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupervisorService {
    //Injection of dependencies
    private final GerenteRepository gerenteRepository;
    private final SupervisorRepository supervisorRepository;
    private static final Logger log = LoggerFactory.getLogger(SupervisorService.class);
    @Autowired
    private DepartamentoRepository departamentoRepository;
    public SupervisorService(GerenteRepository gerenteRepository, SupervisorRepository supervisorRepository) {
        this.gerenteRepository = gerenteRepository;
        this.supervisorRepository = supervisorRepository;
    }
    /* READ */
    public List<Supervisor> busqueda(String consulta) {
        log.info("Supervisor Service: busqueda");
        return supervisorRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(consulta, consulta);
    }
    public List<Supervisor> buscar() {
        log.info("Supervisor Service: buscar");
        return supervisorRepository.buscarTodos();
    }

    /* SAVE */
    public Supervisor guardar(Supervisor supervisor) {
        log.info("Supervisor Service: guardar");
        return supervisorRepository.save(supervisor);
    }

    /* UPDATE */
    public Supervisor findById(Integer id) {
        log.info("Supervisor Service: findById");
        return supervisorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supervisor not found with id ", "id", id));
    }
    public void update(Integer id, Supervisor supervisorDetails) {
        log.info("Supervisor Service: update");
        Supervisor supervisor = findById(id);
        supervisor.setNombre(supervisorDetails.getNombre());
        supervisor.setApellido(supervisorDetails.getApellido());
        supervisor.setCorreo(supervisorDetails.getCorreo());
        supervisor.setTelefono(supervisorDetails.getTelefono());
        supervisor.setDepartamentos(supervisorDetails.getDepartamentos());
        log.info("Data of supervisor " + supervisor);
        supervisorRepository.save(supervisor);
    }

    /* DELETE */
    public void deleteSupervisor(Integer id) {
        log.info("Supervisor Service: deleteSupervisor");
        supervisorRepository.deleteById(id);
    }

    public List<Supervisor> findSupervisorsByLocalId(Integer localId) {
        List<Supervisor> supervisores = departamentoRepository.findSupervisorsByLocalId(localId);
        // hacer algo con los supervisores, por eja emplo, agregarlos a unlista
        List<String> nombresSupervisores = supervisores.stream().map(Supervisor::getNombre).collect(Collectors.toList());
        return supervisores;
    }

}
