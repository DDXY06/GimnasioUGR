package com.example.gimnasiougr.Controllers.Cliente;

import com.example.gimnasiougr.Controllers.LoginController;
import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Services.ClienteClasesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteTipo1Controller {

    private final ClienteClasesService clasesService;

    @GetMapping("/clases-tipo1")
    public String clasesTipo1(Model model) {

        Usuario usuarioLogeado = LoginController.usuarioLogeadoGlobal;
        if (usuarioLogeado == null) return "redirect:/";

        model.addAttribute("clases", clasesService.obtenerClasesTipo1ConEstado(usuarioLogeado));

        return "cliente/clases-tipo1";
    }

    @PostMapping("/solicitar-cambio")
    public String solicitarCambio(@RequestParam Long cupoId,
                                  @RequestParam Long claseCambioId,
                                  RedirectAttributes redirectAttributes) {

        Usuario usuarioLogeado = LoginController.usuarioLogeadoGlobal;
        if (usuarioLogeado == null) return "redirect:/";

        try {
            clasesService.procesarSolicitudCambio(usuarioLogeado, cupoId, claseCambioId);
            redirectAttributes.addFlashAttribute("exito", "Solicitud de cambio enviada con éxito.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        // Corregido: volvemos a la misma página, no a horario-tipo1
        return "redirect:/cliente/clases-tipo1";
    }
}