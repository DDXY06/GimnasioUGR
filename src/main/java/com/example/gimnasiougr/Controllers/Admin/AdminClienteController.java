package com.example.gimnasiougr.Controllers.Admin;

import com.example.gimnasiougr.Models.BonoDTO;
import com.example.gimnasiougr.Models.ClienteDTO;
import com.example.gimnasiougr.Models.TipoBono;
import com.example.gimnasiougr.Models.TipoClase;
import com.example.gimnasiougr.Services.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminClienteController {

    private final ClienteService clienteService;

    @ModelAttribute("tiposBonos")
    public List<TipoBono> listarTiposBonos() {
        return List.of(TipoBono.values());
    }

    @GetMapping("/clientes")
    public String listarClientes(
            @RequestParam(required = false, defaultValue = "nombre") String tipoFiltro,
            @RequestParam(required = false, defaultValue = "") String textoBusqueda,
            Model model) {

        // Solucionado: Se llama al método correcto del servicio y manejamos DTOs
        List<ClienteDTO> clientes = clienteService.buscarPorFiltro(tipoFiltro, textoBusqueda);

        model.addAttribute("clientes", clientes);
        model.addAttribute("tipoFiltro", tipoFiltro);
        model.addAttribute("textoBusqueda", textoBusqueda);
        return "admin/clientes";
    }

    @GetMapping("/clientes/nuevo")
    public String nuevoForm(Model model) {
        ClienteDTO cliente = new ClienteDTO();
        List<BonoDTO> primerBono = new ArrayList<>();
        cliente.setBonos(primerBono);
        model.addAttribute("cliente", new ClienteDTO());
        model.addAttribute("esNuevo", true);
        return "admin/cliente-form";
    }

    @GetMapping("/clientes/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        ClienteDTO clienteDTO = clienteService.buscarPorId(id);

        if (clienteDTO == null) return "redirect:/admin/clientes";

        model.addAttribute("cliente", clienteDTO);
        model.addAttribute("esNuevo", false);
        return "admin/cliente-form";
    }

    @PostMapping("/clientes/guardar")
    public String guardarCliente(
            @Valid @ModelAttribute("cliente") ClienteDTO clienteDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (clienteDTO.getId() == null && (clienteDTO.getContrasenia() == null || clienteDTO.getContrasenia().isBlank())) {
            result.rejectValue("contrasenia", "error.cliente", "La contraseña es obligatoria para nuevos registros");
        }

        if (result.hasErrors()) {
            model.addAttribute("esNuevo", clienteDTO.getId() == null);
            return "admin/cliente-form";
        }

        try {
            // Solucionado: El método en el servicio se llama guardar y sirve para crear o actualizar
            clienteService.guardar(clienteDTO);
            redirectAttributes.addFlashAttribute("exito", "Operación realizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el cliente: " + e.getMessage());
        }

        return "redirect:/admin/clientes";
    }

    @GetMapping("/clientes/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            /*Recibe booleano, hacer algo si no existe solo por si acaso*/
            clienteService.eliminar(id);
            redirectAttributes.addFlashAttribute("exito", "Cliente eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el cliente.");
        }
        return "redirect:/admin/clientes";
    }
}