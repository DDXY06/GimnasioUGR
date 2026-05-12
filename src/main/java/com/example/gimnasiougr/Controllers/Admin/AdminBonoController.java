package com.example.gimnasiougr.Controllers.Admin;

import com.example.gimnasiougr.Models.BonoDTO;
import com.example.gimnasiougr.Models.TipoBono;
import com.example.gimnasiougr.Services.BonoService;
import com.example.gimnasiougr.Services.ClienteService;
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
@RequestMapping("/admin/bonos")
public class AdminBonoController {

    private final BonoService bonoService;
    private final ClienteService clienteService;

    @GetMapping()
    public String listarBonos(
            @RequestParam(required = false, defaultValue = "cliente") String tipoFiltro,
            @RequestParam(required = false, defaultValue = "") String textoBusqueda,
            Model model) {

        List<BonoDTO> bonos = bonoService.buscarPorFiltro(tipoFiltro, textoBusqueda);

        model.addAttribute("bonos", bonos);
        model.addAttribute("tipoFiltro", tipoFiltro);
        model.addAttribute("textoBusqueda", textoBusqueda);
        return "admin/bonos";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("bono", new BonoDTO());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("tiposBonos", TipoBono.values());
        model.addAttribute("esNuevo", true);
        return "admin/bono-form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        BonoDTO bonoDTO = bonoService.buscarPorId(id);

        if (bonoDTO == null) return "redirect:/admin/bonos";

        model.addAttribute("bono", bonoDTO);
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("tiposBonos", TipoBono.values());
        model.addAttribute("esNuevo", false);
        return "admin/bono-form";
    }

    @PostMapping("/guardar")
    public String guardarBono(
            @Valid @ModelAttribute("bono") BonoDTO bonoDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("tiposBonos", TipoBono.values());
            model.addAttribute("esNuevo", bonoDTO.getId() == null);
            return "admin/bono-form";
        }

        try {
            bonoService.guardar(bonoDTO);
            redirectAttributes.addFlashAttribute("exito", "Operación realizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el bono: " + e.getMessage());
        }

        return "redirect:/admin/bonos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarBono(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bonoService.eliminar(id);
            redirectAttributes.addFlashAttribute("exito", "Bono eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el bono.");
        }
        return "redirect:/admin/bonos";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        BonoDTO bono = bonoService.buscarDetalleConCupos(id);
        if (bono == null) return "redirect:/admin/bonos";
        model.addAttribute("bono", bono);
        return "admin/bono-detalle";
    }
}