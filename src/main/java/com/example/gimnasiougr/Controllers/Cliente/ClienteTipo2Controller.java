package com.example.gimnasiougr.Controllers.Cliente;

import com.example.gimnasiougr.Controllers.LoginController;
import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Repositories.DeporteRepository;
import com.example.gimnasiougr.Repositories.EntrenadorRepository;
import com.example.gimnasiougr.Services.ClienteClasesService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteTipo2Controller {

    private final ClienteClasesService clasesService;
    private final DeporteRepository deporteRepository;
    private final EntrenadorRepository entrenadorRepository;

    @GetMapping("/clases-tipo2")
    public String misClasesTipo2(Model model) {
        Usuario usuario = LoginController.usuarioLogeadoGlobal;
        if (usuario == null) return "redirect:/";

        model.addAttribute("clases", clasesService.obtenerClasesTipo2(usuario));
        model.addAttribute("deportes", deporteRepository.findAll());
        model.addAttribute("entrenadores", entrenadorRepository.findAll());

        return "cliente/clases-tipo2";
    }

    @PostMapping("/solicitar-tipo2")
    public String solicitarClase(@RequestParam Long deporteId,
                                 @RequestParam Long entrenadorId,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
                                 RedirectAttributes redirectAttributes) {

        Usuario usuario = LoginController.usuarioLogeadoGlobal;
        if (usuario == null) return "redirect:/";

        try {
            clasesService.solicitarClaseTipo2(usuario, deporteId, entrenadorId, fecha, hora);
            redirectAttributes.addFlashAttribute("exito", "Solicitud enviada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
        }

        return "redirect:/cliente/clases-tipo2";
    }
}