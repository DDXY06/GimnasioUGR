package com.example.gimnasiougr.Controllers.Entrenador;

import com.example.gimnasiougr.Controllers.LoginController;
import com.example.gimnasiougr.Models.ClaseDTO;
import com.example.gimnasiougr.Models.Entrenador;
import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Repositories.EntrenadorRepository;
import com.example.gimnasiougr.Services.ClaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/entrenador/")
public class EntrenadorController {
    private final EntrenadorRepository entrenadorRepository;
    private final ClaseService claseService;
    @GetMapping("/clases")
    public String listarClases(@RequestParam("id") Long id, Model model) {
        Optional<Entrenador> entrenador = entrenadorRepository.findById(id);
        if (entrenador.isPresent()) {
            model.addAttribute("entrenador", entrenador.get());
            List<ClaseDTO> clases = claseService.buscarPorFiltro("entrenador", entrenador.get().getNombre());
            model.addAttribute("clases", clases);
        } else {
            return "redirect:/entrenador/index";
        }
        return "entrenador/clases";
    }
}
