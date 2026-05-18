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
@RequestMapping("/admin/clientes")
public class AdminClienteController {

    private final ClienteService clienteService;


    @GetMapping()
    public String listarClientes(
            @RequestParam(required = false, defaultValue = "nombre") String tipoFiltro,
            @RequestParam(required = false, defaultValue = "") String textoBusqueda,
            Model model) {

        List<ClienteDTO> clientes = clienteService.buscarPorFiltro(tipoFiltro, textoBusqueda);

        model.addAttribute("clientes", clientes);
        model.addAttribute("tipoFiltro", tipoFiltro);
        model.addAttribute("textoBusqueda", textoBusqueda);
        return "admin/clientes";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setBonos(new ArrayList<>());
        model.addAttribute("cliente", cliente);
        model.addAttribute("tiposBonos", TipoBono.values());
        model.addAttribute("esNuevo", true);
        return "admin/cliente-form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        ClienteDTO clienteDTO = clienteService.buscarPorId(id);

        if (clienteDTO == null){
            return "redirect:/admin/clientes";
        }

        model.addAttribute("cliente", clienteDTO);
        model.addAttribute("esNuevo", false);
        return "admin/cliente-form";
    }

    @PostMapping("/guardar")
    public String guardarCliente(
            @Valid @ModelAttribute("cliente") ClienteDTO clienteDTO,
            RedirectAttributes redirectAttributes) {

        try {
            clienteService.guardar(clienteDTO);

            redirectAttributes.addFlashAttribute("exito", "Cliente guardado correctamente.");
            return "redirect:/admin/clientes";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error" , e);

            return "redirect:/admin/clientes";
        }
    }

    @GetMapping("/eliminar/{id}")
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