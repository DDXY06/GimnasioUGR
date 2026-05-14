package com.example.gimnasiougr.Controllers.Admin;

import com.example.gimnasiougr.Models.EntrenadorDTO;
import com.example.gimnasiougr.Services.EntrenadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/entrenadores")
public class AdminEntrenadorController {

    private final EntrenadorService entrenadorService;

    @GetMapping()
    public String listarEntrenadores(
            @RequestParam(required = false, defaultValue = "nombre") String tipoFiltro,
            @RequestParam(required = false, defaultValue = "") String textoBusqueda,
            Model model) {

        List<EntrenadorDTO> entrenadores = entrenadorService.buscarPorFiltro(tipoFiltro, textoBusqueda);

        model.addAttribute("entrenadores", entrenadores);
        model.addAttribute("tipoFiltro", tipoFiltro);
        model.addAttribute("textoBusqueda", textoBusqueda);
        return "admin/entrenadores";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        EntrenadorDTO entrenador = new EntrenadorDTO();
        model.addAttribute("entrenador", entrenador);
        model.addAttribute("esNuevo", true);
        return "admin/entrenador-form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        EntrenadorDTO entrenadorDTO = entrenadorService.buscarPorId(id);

        if (entrenadorDTO == null) return "redirect:/admin/entrenadores";

        model.addAttribute("entrenador", entrenadorDTO);
        model.addAttribute("esNuevo", false);
        return "admin/entrenador-form";
    }

    @PostMapping("/guardar")
    public String guardarEntrenador(
            @Valid @ModelAttribute("entrenador") EntrenadorDTO entrenadorDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (entrenadorDTO.getId() == null && (entrenadorDTO.getContrasenia() == null || entrenadorDTO.getContrasenia().isBlank())) {
            result.rejectValue("contrasenia", "error.entrenador", "La contraseña es obligatoria para nuevos registros");
        }

        if (result.hasErrors()) {
            model.addAttribute("esNuevo", entrenadorDTO.getId() == null);
            return "admin/entrenador-form";
        }

        try {
            entrenadorService.guardar(entrenadorDTO);
            redirectAttributes.addFlashAttribute("exito", "Operación realizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el entrenador: " + e.getMessage());
        }

        return "redirect:/admin/entrenadores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEntrenador(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean eliminado = entrenadorService.eliminar(id);
            if (eliminado) {
                redirectAttributes.addFlashAttribute("exito", "Entrenador eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "El entrenador no existe.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el entrenador.");
        }
        return "redirect:/admin/entrenadores";
    }
}
