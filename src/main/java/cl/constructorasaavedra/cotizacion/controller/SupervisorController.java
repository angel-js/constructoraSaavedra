package cl.constructorasaavedra.cotizacion.controller;

import cl.constructorasaavedra.cotizacion.dto.SupervisorDto;
import cl.constructorasaavedra.cotizacion.model.Supervisor;
import cl.constructorasaavedra.cotizacion.repository.SupervisorRepository;
import cl.constructorasaavedra.cotizacion.service.CotizacionService;
import cl.constructorasaavedra.cotizacion.service.SupervisorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/personal")
public class SupervisorController {

    //Injection of dependencies
    private final SupervisorService supervisorService;
    private final SupervisorRepository supervisorRepository;
    private final CotizacionService cotizacionService;
    private static final Logger log = LoggerFactory.getLogger(SupervisorController.class);

    public SupervisorController(SupervisorService supervisorService, SupervisorRepository supervisorRepository, CotizacionService cotizacionService) {
        this.supervisorService = supervisorService;
        this.supervisorRepository = supervisorRepository;
        this.cotizacionService = cotizacionService;
    }

    /* READ */
    @RequestMapping("/buscar")
    public String buscarPersonal(Model model) {
        log.info("This is the search of supervisor PATH: /buscar");
        List<Supervisor> supervisores = supervisorService.buscar();
        Collections.sort(supervisores, (s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
        model.addAttribute("supervisores", supervisores);
        return "supervisor/SupervisorBuscar";
    }

    @RequestMapping("/busquedaPersonalizada")
    public String busqueda(@RequestParam("q") String consulta, Model model) {
        log.info("This is the search of supervisor with any parameter PATH: /busquedaPersonalizada");
        List<Supervisor> todos = supervisorService.busqueda(consulta);
        model.addAttribute("supervisores", todos);
        if (!todos.isEmpty()) { /* If request is not empty show Template with JPA */
            log.info("Return a list of HTML with JPA parameters");
            return "supervisor/SupervisorBuscarPersonalizado";
        } else {
            log.debug("The request is empty!");
            return "supervisor/SupervisorErrorView";
        }
    }

    @RequestMapping("/detalle/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        Optional<Supervisor> supervisorOptional = supervisorRepository.findById(id);
        log.info("This is unique info of supervisor with ID PATH: /detalle/{id}");
        log.info("COTIZACION OBJECT BEFORE JPA ------------> " + supervisorOptional);
        if (supervisorOptional.isPresent()) { // Validate if the object come in the array and if unpacking
            Supervisor supervisor = supervisorOptional.get(); // Then turn in the new object
            model.addAttribute("supervisor", supervisor); //And finally show in the template
            return "supervisor/SupervisorDetalle";
        } else {
            log.debug("The request is empty!");
            return "supervisor/SupervisorErrorView";
        }
    }

    /* CREATE */
    @RequestMapping("/crear")
    public String crearSupervisor(Model model) {
        log.info("This is a create Method of supervisor PATH: /crear");
        model.addAttribute("departamentos", cotizacionService.buscarDepart());
        model.addAttribute("supervisor", new Supervisor());
        return "supervisor/SupervisorCreate";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid SupervisorDto supervisorDto, BindingResult result, Model model) {
        log.info("This is a save of create instance PATH: /guardar");

        if (supervisorDto.getNombre() == "" || supervisorDto.getNombre() == null ||
                supervisorDto.getApellido() == "" || supervisorDto.getApellido() == null ||
                supervisorDto.getCorreo() == "" || supervisorDto.getDepartamentos() == null
        ) {
            model.addAttribute("departamentos", cotizacionService.buscarDepart());
            model.addAttribute("supervisor", new Supervisor());
            model.addAttribute("errorMessage", "Uno de los campos esta vacío!!!");
            return "supervisor/SupervisorCreate";
        }

        if (result.hasErrors()) {
            model.addAttribute("departamentos", cotizacionService.buscarDepart());
            model.addAttribute("supervisor", new Supervisor());
            return "supervisor/SupervisorCreate";
        }

        Supervisor supervisor = new Supervisor();
        try {
            supervisor.setNombre(supervisorDto.getNombre());
            supervisor.setApellido(supervisorDto.getApellido());
            supervisor.setTelefono(supervisorDto.getTelefono());
            supervisor.setCorreo(supervisorDto.getCorreo());
            supervisor.setCargo(supervisorDto.getCargo());
            supervisor.setDepartamentos(supervisorDto.getDepartamentos());
        } catch (Exception e) {
            log.debug(e.getMessage());
            model.addAttribute("departamentos", cotizacionService.buscarDepart());
            model.addAttribute("supervisor", new Supervisor());
            return "supervisor/SupervisorCreate";
        }

        log.info("Se guarda la informacion del Supervisor  ------> " + supervisor);
        supervisorService.guardar(supervisor);
        return "redirect:/admin/personal/buscar";
    }

    /* UPDATE */
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        log.info("This is update of supervisor PATH: /update/{id}");
        Supervisor supervisor = supervisorService.findById(id);
        model.addAttribute("supervisor", supervisor);
        model.addAttribute("departamentos", cotizacionService.buscarDepart());
        return "supervisor/SupervisorUpdateForm";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute Supervisor supervisor) {
        log.info("This is save instance of supervisor PATH: /update/{id}");
        log.info("Update info  ------> " + supervisor);
        supervisorService.update(id, supervisor);
        return "redirect:/admin/personal/buscar";
    }

    /* DELETE */
    @PostMapping("/delete/{id}")
    public String deleteSupervisor(@PathVariable Integer id, @RequestParam(required = false) String _method) {
        if ("delete".equals(_method)) { /* If delete comes in HTML method execute this code*/
            log.info("This is delete of supervisor PATH: /delete/{id}");
            log.info("Delete supervisor of DATABASE  ------> " + id);
            supervisorService.deleteSupervisor(id);
            return "redirect:/admin/personal/buscar";
        } else {
            log.debug("Error to eliminate  ------> " + id);
            return "supervisor/Error404";
        }
    }
}
