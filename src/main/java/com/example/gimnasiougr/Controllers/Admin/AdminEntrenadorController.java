package com.example.gimnasiougr.Controllers.Admin;

import com.example.gimnasiougr.Models.ClienteDTO;
import com.example.gimnasiougr.Models.EntrenadorDTO;
import com.example.gimnasiougr.Services.EntrenadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/entrenadores")
public class AdminEntrenadorController {
    private final EntrenadorService entrenadorService;

    @GetMapping()
    public String listarEntrenadores(@RequestParam(required = false, defaultValue = "cliente") String tipoFiltro,
                                     @RequestParam(required = false, defaultValue = "") String textoBusqueda,
                                     Model model) {
        List<EntrenadorDTO> entrenadores = entrenadorService.buscarPorFiltro(tipoFiltro, textoBusqueda);

        model.addAttribute("entrenadores", entrenadores);
        model.addAttribute("tipoFiltro", tipoFiltro);
        model.addAttribute("textoBusqueda", textoBusqueda);
        return "admin/entrenadores";
    }
}
