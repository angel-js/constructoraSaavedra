package cl.constructorasaavedra.cotizacion.controller;

import cl.constructorasaavedra.cotizacion.model.Item;
import cl.constructorasaavedra.cotizacion.model.ItemForm;
import cl.constructorasaavedra.cotizacion.repository.ItemRepository;
import cl.constructorasaavedra.cotizacion.service.TrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/admin/trabajo/item")
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private TrabajoService trabajoService;

    @PostMapping("/update")
    public RedirectView update(@ModelAttribute("itemForm") ItemForm itemForm) {
        for (Item item : itemForm.getItems()) {
            System.out.println("item ---->>>>>>>> " + item);
            trabajoService.actualizarItem(item.getId(), item.getNombre(), item.getMonto());
        }
        return new RedirectView("/admin/trabajo/home", true) ;
    }
}
