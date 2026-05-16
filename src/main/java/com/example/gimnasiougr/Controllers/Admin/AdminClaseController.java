package com.example.gimnasiougr.Controllers.Admin;

import java.util.List;

import com.example.gimnasiougr.Models.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.gimnasiougr.Services.ClaseService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/clases/")
public class AdminClaseController {
    private final ClaseService claseService;
    private final com.example.gimnasiougr.Services.DeporteService deporteService;
    private final com.example.gimnasiougr.Services.EntrenadorService entrenadorService;
    private final com.example.gimnasiougr.Services.CupoService cupoService;
    private final com.example.gimnasiougr.Services.ClienteService clienteService;

    @ModelAttribute("tiposClase")
    public List<TipoClase> tiposClase() {
        return List.of(TipoClase.values());
    }

    @ModelAttribute("deportes")
    public List<Deporte> deportes() {
        return deporteService.listarTodos();
    }

    @ModelAttribute("entrenadores")
    public List<com.example.gimnasiougr.Models.EntrenadorDTO> entrenadores() {
        return entrenadorService.listarTodos();
    }

    @ModelAttribute("estados")
    public List<com.example.gimnasiougr.Models.Estado> estados() {
        return List.of(com.example.gimnasiougr.Models.Estado.values());
    }

    @GetMapping()
    public String listarTodos(
            @RequestParam(required = false, defaultValue = "tipo") String tipoFiltro,
            @RequestParam(required = false, defaultValue = "") String textoBusqueda,
            Model model) {

        List<ClaseDTO> clasesDTO = claseService.buscarPorFiltro(tipoFiltro, textoBusqueda);
        model.addAttribute("clases", clasesDTO);
        model.addAttribute("tipoFiltro", tipoFiltro);
        model.addAttribute("textoBusqueda", textoBusqueda);

        return "admin/clases";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarClase(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean eliminado = claseService.eliminar(id);
            if (eliminado) {
                redirectAttributes.addFlashAttribute("exito", "Clase eliminada correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se encontró la clase a eliminar.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al intentar eliminar la clase.");
        }
        return "redirect:/admin/clases/";
    }

    @GetMapping("/deporte/nuevo")
    public String nuevoDeporte(Model model) {
        model.addAttribute("deporte", new Deporte());
        return "admin/deporte-form";
    }

    @PostMapping("/deporte/nuevo")
    public String guardarDeporte(
            @Valid @ModelAttribute("deporte") Deporte deporte,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "admin/deporte-form";
        }

        Deporte guardado = deporteService.guardar(deporte);
        if (guardado != null) {
            redirectAttributes.addFlashAttribute("exito", "Deporte añadido correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al añadir el deporte.");
        }
        
        return "redirect:/admin/clases/";
    }

    @GetMapping("/nuevo")
    public String nuevaClase(Model model) {
        model.addAttribute("clase", new ClaseDTO());
        return "admin/clase-form";
    }

    @GetMapping("/editar/{id}")
    public String editarClase(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        ClaseDTO claseDTO = claseService.buscarPorId(id);
        if (claseDTO == null) {
            redirectAttributes.addFlashAttribute("error", "La clase especificada no existe.");
            return "redirect:/admin/clases/";
        }
        model.addAttribute("clase", claseDTO);
        return "admin/clase-form";
    }

    @PostMapping("/guardar")
    public String guardarClase(@Valid @ModelAttribute("clase") ClaseDTO claseDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/clase-form";
        }

        ClaseDTO guardado = claseService.guardar(claseDTO);
        if (guardado != null) {
            redirectAttributes.addFlashAttribute("exito", "Clase guardada correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la clase.");
        }

        return "redirect:/admin/clases/";
    }

    @GetMapping("/{idClase}/clientes")
    public String listarClientesClase(@PathVariable("idClase") Long id, Model model) {

        ClaseDTO claseDTO = claseService.buscarPorId(id);
        List<CupoDTO> cuposDTO = cupoService.buscarPorIdClase(id);
        List<ClienteDTO> clientesDTO = clienteService.listarClientesPorListaCupos(cuposDTO);
        List<ClienteDTO> clientesDisponibles = clienteService.listarClientesDisponiblesParaClase(id);

        model.addAttribute("clase", claseDTO);
        model.addAttribute("clientesInscritos", clientesDTO);
        model.addAttribute("clientesDisponibles", clientesDisponibles);

        return "admin/clase-clientes";
    }

    @PostMapping("/{idCliente}/clientes/add")
    public String addCliente(@PathVariable Long idCliente,
                                @RequestParam Long clienteId,
                                RedirectAttributes redirectAttributes) {
        try {
            claseService.addCliente(idCliente, clienteId);
            redirectAttributes.addFlashAttribute("exito", "Cliente inscrito correctamente en la clase.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al inscribir cliente: " + e.getMessage());
        }
        return "redirect:/admin/clases/";
    }


    @PostMapping("/{idClase}/clientes/remove/{idCliente}")
    public String removeCliente(@PathVariable Long idClase,
                                @PathVariable Long idCliente,
                                RedirectAttributes redirectAttributes) {
        try {
            claseService.removeCliente(idClase, idCliente); // o desinscribirCliente
            redirectAttributes.addFlashAttribute("exito", "Cliente eliminado de la clase correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar cliente: " + e.getMessage());
        }
        return "redirect:/admin/clases/";
    }



}
