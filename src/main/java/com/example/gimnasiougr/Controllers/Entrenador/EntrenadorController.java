package com.example.gimnasiougr.Controllers.Entrenador;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.gimnasiougr.Models.ClaseDTO;
import com.example.gimnasiougr.Models.EntrenadorDTO;
import com.example.gimnasiougr.Models.SolicitudCambioDTO;
import com.example.gimnasiougr.Services.ClaseService;
import com.example.gimnasiougr.Services.EntrenadorService;
import com.example.gimnasiougr.Services.SolCambioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/entrenador/")
public class EntrenadorController {
    private final EntrenadorService entrenadorService;
    private final ClaseService claseService;
    private final SolCambioService solCambioService;
    @GetMapping("/clases")
    public String listarClases(@RequestParam("id") Long id, Model model) {
        EntrenadorDTO entrenador = entrenadorService.buscarPorId(id);
        if (entrenador != null) {
            model.addAttribute("entrenador", entrenador);
            List<ClaseDTO> clases = claseService.buscarPorFiltro("entrenador", entrenador.getNombre());
            model.addAttribute("clases", clases);
        } else {
            return "redirect:/entrenador/index";
        }
        return "entrenador/clases";
    }

    @GetMapping("/solicitudes")
    public String solicitudes(@RequestParam("id") Long id, Model model) {
        List<ClaseDTO> listaClases = claseService.buscarClasesTipo2Pendiente();
        model.addAttribute("clases", listaClases);
        List<SolicitudCambioDTO> listarCambios = solCambioService.listarCambios();
        model.addAttribute("solCambios", listarCambios);
        return "entrenador/solicitudes";
    }

    @PostMapping("/solicitudes/aceptar")
    public String aceptarSolicitud(@RequestParam("id") Long id, @RequestParam("idClase") Long idClase) {
        ClaseDTO claseDTO = claseService.buscarPorId(idClase);
        if (claseDTO != null) {
            claseService.aceptarClaseTipo2(claseDTO);
            return "redirect:/entrenador/solicitudes?id=" + claseDTO.getEntrenadorId();
        }
        return "redirect:/entrenador/index";
    }

    @PostMapping("/solicitudes/rechazar")
    public String rechazarSolicitud(@RequestParam("id") Long id, @RequestParam("idClase") Long idClase) {
        ClaseDTO claseDTO = claseService.buscarPorId(idClase);
        if (claseDTO != null) {
            claseService.rechazarClaseTipo2(claseDTO);
            return "redirect:/entrenador/solicitudes?id=" + claseDTO.getEntrenadorId();
        }
        return "redirect:/entrenador/index";
    }

    @PostMapping("/solicitudes/cambio/aceptar")
    public String aceptarCambioSolicitud(@RequestParam("id") Long id, @RequestParam("idSolicitud") Long idSolicitud) {
        SolicitudCambioDTO solicitudDTO = solCambioService.buscarPorId(idSolicitud);
        if (solicitudDTO != null) {
            solCambioService.aceptarCambio(solicitudDTO);
        }
        return "redirect:/entrenador/solicitudes?id=" + id;
    }

    @PostMapping("/solicitudes/cambio/rechazar")
    public String rechazarCambioSolicitud(@RequestParam("id") Long id, @RequestParam("idSolicitud") Long idSolicitud) {
        SolicitudCambioDTO solicitudDTO = solCambioService.buscarPorId(idSolicitud);
        if (solicitudDTO != null) {
            solCambioService.rechazarCambio(solicitudDTO);
        }
        return "redirect:/entrenador/solicitudes?id=" + id;
    }

}
