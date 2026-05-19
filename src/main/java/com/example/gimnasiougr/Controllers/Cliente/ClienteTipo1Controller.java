package com.example.gimnasiougr.Controllers.Cliente;

import com.example.gimnasiougr.Controllers.LoginController;
import com.example.gimnasiougr.Models.SolicitudCambioDTO;
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

        Usuario usuario = LoginController.clienteLogeadoGlobal;
        if (usuario == null) return "redirect:/";

        model.addAttribute("clases", clasesService.obtenerClasesTipo1ConEstado(usuario.getId()));
        return "cliente/clases-tipo1";
    }

    @PostMapping("/solicitar-cambio")
    public String solicitarCambio(@ModelAttribute SolicitudCambioDTO solicitud,
                                  RedirectAttributes redirectAttributes) {
        Usuario usuario = LoginController.clienteLogeadoGlobal;
        if (usuario == null) return "redirect:/";

        try {
            clasesService.procesarSolicitudCambioClaseTipo1(usuario.getId(), solicitud.getCupoId(), solicitud.getClaseCambioId());
            redirectAttributes.addFlashAttribute("exito", "Solicitud enviada con éxito.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cliente/clases-tipo1";
    }
}