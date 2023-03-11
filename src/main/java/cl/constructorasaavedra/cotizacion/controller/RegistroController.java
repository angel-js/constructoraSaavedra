package cl.constructorasaavedra.cotizacion.controller;

import cl.constructorasaavedra.cotizacion.dto.UsuarioRegistroDto;
import cl.constructorasaavedra.cotizacion.interfaces.UsuarioServicio;
import cl.constructorasaavedra.cotizacion.service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registro")
public class RegistroController {
    @Autowired
    private UsuarioServicio usuarioService;

    @Autowired
    private RegistroService registroService;

    @ModelAttribute("usuario")
    public UsuarioRegistroDto retornarNuevoUsuarioRegistroDto() {
        return new UsuarioRegistroDto();
    }

    @GetMapping
    public String mostrarFormularioDeRegistro(Model model) {
        model.addAttribute("roles",registroService.mostrarRoles() );
        return "registro/RegistroHome";
    }

    @PostMapping
    public String registrarCuentaDeUsuario(@ModelAttribute("usuario") UsuarioRegistroDto usuarioRegistroDto){
        usuarioService.guardar(usuarioRegistroDto);
        return "redirect:/registro?exito";
    }
}
