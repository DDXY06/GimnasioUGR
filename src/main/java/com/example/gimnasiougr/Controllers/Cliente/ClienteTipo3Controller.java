package com.example.gimnasiougr.Controllers.Cliente;

import com.example.gimnasiougr.Controllers.LoginController;
import com.example.gimnasiougr.Models.*;
import com.example.gimnasiougr.Repositories.*;
import com.example.gimnasiougr.Services.BonoService;
import com.example.gimnasiougr.Services.ClienteClasesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteTipo3Controller {

    private final ClienteClasesService clienteClasesService;

    @GetMapping("/clases-tipo3")
    public String verClasesTipo3(Model model) {
        Usuario usuario = LoginController.usuarioLogeadoGlobal;
        if (usuario == null) return "redirect:/";

        List<ClaseDTO> clasesDTO = clienteClasesService.obtenerClasesTipo3ConEstado(usuario);

        model.addAttribute("clases", clasesDTO);
        return "cliente/clases-tipo3";
    }


    @PostMapping("/apuntarse-tipo3")
    public String apuntarse(@RequestParam Long claseId, RedirectAttributes redirectAttributes) {

        Usuario usuario = LoginController.usuarioLogeadoGlobal;
        if (usuario == null) return "redirect:/";

        try {
            clienteClasesService.inscribirClaseTipo3(usuario, claseId);

            redirectAttributes.addFlashAttribute("exito", "¡Te has apuntado correctamente!");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cliente/clases-tipo3";
    }

}