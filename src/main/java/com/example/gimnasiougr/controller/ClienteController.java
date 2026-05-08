package com.example.gimnasiougr.controller;

import com.example.gimnasiougr.Models.Cliente;
import com.example.gimnasiougr.Services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "admin/Cliente-form";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute("cliente") Cliente cliente) {
        if (cliente.getRol() == null || cliente.getRol().isEmpty()) {
            cliente.setRol("CLIENTE");
        }
        clienteService.guardar(cliente);
        return "redirect:/admin/clientes";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);
        model.addAttribute("cliente", cliente);
        return "admin/cliente-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Long id) {
        clienteService.eliminar(id);
        return "redirect:/admin/clientes";
    }

    @GetMapping
    public String listarClientes(
            @RequestParam(required = false) String tipoFiltro,
            @RequestParam(required = false) String textoBusqueda,
            Model model) {

        List<Cliente> listaClientes;

        if (textoBusqueda != null && !textoBusqueda.isEmpty()) {
            listaClientes = clienteService.buscarPorFiltro(tipoFiltro, textoBusqueda);
            model.addAttribute("tipoFiltro", tipoFiltro);
            model.addAttribute("textoBusqueda", textoBusqueda);
        } else {
            listaClientes = clienteService.listarTodos();
        }

        model.addAttribute("clientes", listaClientes);
        return "admin/Clientes";
    }

    @Transactional
    @GetMapping("/{id}/bonos")
    public String verBonosCliente(@PathVariable("id") Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);

        if (cliente == null) {
            return "redirect:/admin/clientes";
        }

        model.addAttribute("cliente", cliente);
        return "admin/cliente-bonos";
    }
}