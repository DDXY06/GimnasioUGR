package com.example.gimnasiougr.Controllers.Entrenador;

import com.example.gimnasiougr.Controllers.LoginController;
import com.example.gimnasiougr.Models.Entrenador;
import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Repositories.EntrenadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/entrenador")
public class EntrenadorDefaultController {
    private final EntrenadorRepository entrenadorRepository;

    @GetMapping({"", "/","/index"})
    public String index(Model model) {
        Usuario usuario = LoginController.entrenadorLogeado;
        if (usuario == null) return "redirect:/";
        Optional<Entrenador> entrenador = entrenadorRepository.findFirstByUsuario(usuario);
        if (entrenador.isPresent()) {
            model.addAttribute("entrenador", entrenador.get());
        }else{
            return "redirect:/";
        }
        return "entrenador/index";
    }


}
