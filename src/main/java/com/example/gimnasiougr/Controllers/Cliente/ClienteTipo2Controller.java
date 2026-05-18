package com.example.gimnasiougr.Controllers.Cliente;

import com.example.gimnasiougr.Controllers.LoginController;
import com.example.gimnasiougr.Models.ClaseDTO;
import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Services.ClienteClasesService;
import com.example.gimnasiougr.Services.DeporteService;
import com.example.gimnasiougr.Services.EntrenadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteTipo2Controller {

    private final ClienteClasesService clasesService;
    private final DeporteService deporteService;
    private final EntrenadorService entrenadorService;

    @GetMapping("/clases-tipo2")
    public String misClasesTipo2(Model model) {
        Usuario usuario = LoginController.clienteLogeadoGlobal;
        if (usuario == null) return "redirect:/";

        model.addAttribute("clases", clasesService.obtenerClasesTipo2(usuario.getId()));
        model.addAttribute("deportes", deporteService.listarTodos());
        model.addAttribute("entrenadores", entrenadorService.listarTodos());

        return "cliente/clases-tipo2";
    }

    @PostMapping("/solicitar-tipo2")
    public String solicitarClase(@ModelAttribute ClaseDTO solicitud,
                                 RedirectAttributes redirectAttributes) {
        Usuario usuario = LoginController.clienteLogeadoGlobal;
        if (usuario == null) return "redirect:/";

        try {
            clasesService.solicitarClaseTipo2(usuario.getId(), solicitud.getDeporteId(), solicitud.getEntrenadorId(), solicitud.getFecha(), solicitud.getHora());
            redirectAttributes.addFlashAttribute("exito", "Solicitud enviada correctamente.");
        } catch (IllegalArgumentException e) { // MEJORA: Captura de excepción de negocio específica
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/cliente/clases-tipo2";
    }
}