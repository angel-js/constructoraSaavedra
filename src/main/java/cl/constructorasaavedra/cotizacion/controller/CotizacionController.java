package cl.constructorasaavedra.cotizacion.controller;

import cl.constructorasaavedra.cotizacion.dto.CotizacionDto;
import cl.constructorasaavedra.cotizacion.model.Cotizacion;
import cl.constructorasaavedra.cotizacion.model.Usuario;
import cl.constructorasaavedra.cotizacion.repository.CotizacionRepository;
import cl.constructorasaavedra.cotizacion.service.CotizacionService;
import cl.constructorasaavedra.cotizacion.service.TrabajoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/cotizacion")
public class CotizacionController {

    //Injection of dependencies
    @Autowired
    private TrabajoService trabajoService;
    private CotizacionService cotizacionService;
    private final CotizacionRepository cotizacionRepository;
    private static final Logger log = LoggerFactory.getLogger(CotizacionController.class);
    public CotizacionController(CotizacionService cotizacionService,
                                CotizacionRepository cotizacionRepository) {
        this.cotizacionService = cotizacionService;
        this.cotizacionRepository = cotizacionRepository;
    }

    /* READ */
    @RequestMapping("/home")
    public String  homePage(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = trabajoService.buscarUsuarioXMail(auth.getName());
        model.addAttribute("user", user);
        return "home/Home";
    }

    @RequestMapping("/buscar")
    public String buscarCotizaciones(Model model) {
        log.info("Search of all cotizaciones PATH:/buscar");
        log.info("Retorno de Lista en HTML con JPA");
        return "cotizacion/CotizacionList";
    }

    @PostMapping("/busquedaConParametros")
    public String busqueda(@RequestParam("q") String consulta, Model model) {
        log.info("Search of cotizacion with any parameter PATH:/busquedaConParametros");
        log.info("The parameter is "+ consulta);
        //List<Cotizacion> todos = cotizacionService.findByMotivoContaining(consulta, status);
        List<Cotizacion> todos = cotizacionService.findByConsultaContaining(consulta);
        model.addAttribute("cotizacion", todos);
        model.addAttribute("consulta", consulta);
        if(!todos.isEmpty()){
            log.info("Return a list of JPA parameters");
            return "cotizacion/CotizacionBuscado";
        } else {
            log.debug("The request is empty!");
            log.debug("The Request has empty parameters");
            return "cotizacion/CotizacionErrorView";
        }
    }
    @RequestMapping("/detalle/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        Optional<Cotizacion> cotOptional = cotizacionService.findById(id);
        log.info("COTIZACION OBJECT BEFORE JPA ------------> " + cotOptional);
        if (cotOptional.isPresent()) { // Se valida si el objeto viene en el array y se desempaqueta
            log.info("The object is ready to inyect and is not empty");
            Cotizacion cot = cotOptional.get(); // Para ser pasado en nuevo objeto
            model.addAttribute("cotizacion", cot); // Y finalmente mostrado en la vista
            return "cotizacion/CotizacionDetalle";
        } else {
            log.debug("The object is empty");
            return "cotizacion/CotizacionErrorView";
        }
    }
    /* CREATE */
    @RequestMapping("/crear")
    public String crearCotizaciones(Model model) {
        log.info("Create of cotizacion PATH:/crear");
        model.addAttribute("departamentos", cotizacionService.buscarDepart());
        model.addAttribute("cotizacion", new CotizacionDto());
        model.addAttribute("status", cotizacionService.findAllStatus());
        return "cotizacion/CotizacionCreate";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid CotizacionDto cotizacionDto, BindingResult result, Model model) {
        if (result.hasErrors()){
            log.debug("Errors in the creation of the quotation");
            model.addAttribute("departamentos", cotizacionService.buscarDepart());
            model.addAttribute("errM", "Uno de los campos esta vacío!!!");
            model.addAttribute("cotizacion", new CotizacionDto());
            model.addAttribute("status", cotizacionService.findAllStatus());
            return "cotizacion/CotizacionCreate";
        }
        log.info("Save of cotizacion PATH:/crear ---> " + cotizacionDto);
        Cotizacion cotizacion = new Cotizacion();
        System.out.println("------------------> " + cotizacionDto.getMotivo());
        if(cotizacionDto.getMotivo() == null || cotizacionDto.getMotivo() == "") {
            model.addAttribute("errM", "Uno de los campos esta vacío!!!");
            model.addAttribute("departamentos", cotizacionService.buscarDepart());
            model.addAttribute("cotizacion", new CotizacionDto());
            model.addAttribute("status", cotizacionService.findAllStatus());
            return "cotizacion/CotizacionCreate";
        }
        try {
            cotizacion.setMotivo(cotizacionDto.getMotivo());
            cotizacion.setDescripcion(cotizacionDto.getDescripcion());
            cotizacion.setMonto(cotizacionDto.getMonto());
            cotizacion.setDepartamento(cotizacionDto.getDepartamento());
            cotizacion.setStatus(cotizacionDto.getStatus());
            cotizacionService.guardar(cotizacion);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return "cotizacion/CotizacionCreate/";
        }
        return "redirect:/admin/cotizacion/home/";
    }

    /* UPDATE */
    @GetMapping("/update/{id}")
    public String showUpdateCotizacionForm(@PathVariable Integer id, Model model) {
        log.info("UPDATE of cotizacion PATH:/update/{id}");
        Optional<Cotizacion> cotizacion = cotizacionService.findById(id);
        if (cotizacion.isPresent()){
            Cotizacion cot = cotizacion.get();
            model.addAttribute("cotizacion", cot);
            model.addAttribute("departamentos", cotizacionService.buscarDepart());
            model.addAttribute("status", cotizacionService.findAllStatus());
            return "cotizacion/CotizacionUpdateForm";
        } else {
            log.debug("The object is empty");
            return "cotizacion/CotizacionErrorView";
        }
    }

    @PostMapping("/update/{id}")
    public String updateCotizacion(@PathVariable Integer id, @ModelAttribute Cotizacion cotizacion) {
        log.info("This is save instance of supervisor PATH: /update/{id}");
        System.out.println("COTIZACION STATUS RECIBIDO------------------------->: " + cotizacion.getStatus());
        log.info("Update info  ------> " + cotizacion);
        cotizacionService.update(id, cotizacion);
        return "redirect:/admin/cotizacion/buscar";
    }

    /* DELETE */
    @PostMapping("/delete/{id}")
    public String deleteCotizacion(@PathVariable Integer id, @RequestParam(required = false) String _method) {
        if ("delete".equals(_method)) { /* If delete comes in HTML method execute this code*/
            log.info("This is delete of Cotizacion PATH: /delete/{id}");
            log.info("Cotizacion supervisor of DATABASE  ------> " + id);
            cotizacionService.deleteCotizacion(id);
            return "redirect:/admin/cotizacion/buscar";
        } else {
            log.debug("Error to eliminate  ------> " + id);
            return "Error404";
        }
    }

}
