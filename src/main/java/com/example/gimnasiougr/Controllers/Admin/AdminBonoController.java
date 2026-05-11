package com.example.gimnasiougr.Controllers.Admin;

import com.example.gimnasiougr.Models.BonoDTO;
import com.example.gimnasiougr.Models.TipoBono;
import com.example.gimnasiougr.Services.BonoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import  com.example.gimnasiougr.Services.ClienteService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminBonoController {
    private final BonoService bonoService;
    private final ClienteService clienteService;

    @GetMapping("/bonos")
    public String listarBonos(Model model) {
        List<BonoDTO> listaBonos = bonoService.listarTodos();
        model.addAttribute("listaBonos", listaBonos);
        return "admin/bonos";
    }


    @GetMapping("/bonos/nuevo")
    public String nuevoForm(Model model){
        model.addAttribute("bono", new BonoDTO());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("tiposBonos", TipoBono.values());
        model.addAttribute("esNuevo", true);
        return "admin/bono-form";
    }

    @GetMapping("/bonos/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes flash) {
        BonoDTO bonoDTO = bonoService.buscarPorId(id);

        if (bonoDTO == null) {
            flash.addFlashAttribute("error", "El bono no existe.");
            return "redirect:/admin/bonos";
        }

        model.addAttribute("bono", bonoDTO);
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("tiposBonos", TipoBono.values());
        model.addAttribute("esNuevo", false);

        return "admin/bono-form";
    }

    @PostMapping("/bonos/guardar")
    public String guardarBono(
            @Valid @ModelAttribute("bono") BonoDTO bonoDTO,
            BindingResult result,
            Model model,
            RedirectAttributes flash) {

        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("tiposBonos", TipoBono.values());
            model.addAttribute("esNuevo", bonoDTO.getId() == null);
            return "admin/bono-form";
        }

        try {
            bonoService.guardar(bonoDTO);

            flash.addFlashAttribute("exito", "¡Bono guardado correctamente!");

        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar el bono: " + e.getMessage());
        }

        return "redirect:/admin/bonos";
    }

    @GetMapping("/bonos/eliminar/{id}")
    public String eliminarBono(@PathVariable Long id, RedirectAttributes flash) {
        try {
            bonoService.eliminar(id);
            flash.addFlashAttribute("exito", "Bono eliminado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: No se pudo eliminar el bono.");
        }
        return "redirect:/admin/bonos";
    }

}
