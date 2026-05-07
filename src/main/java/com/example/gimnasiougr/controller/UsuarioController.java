package com.example.gimnasiougr.controller;

import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;



    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/usuario-form";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
        try {
            usuarioService.guardar(usuario);
            return "redirect:/admin/usuarios";

        } catch (Exception e) {
            model.addAttribute("error", "No se ha podido guardar. Comprueba que los datos sean correctos.");
            return "admin/usuario-form";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        return "admin/usuario-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id) {
        usuarioService.eliminar(id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping
    public String listarUsuarios(
            @RequestParam(required = false) String tipoFiltro,
            @RequestParam(required = false) String textoBusqueda,
            Model model) {

        List<Usuario> listaUsuarios;

        if (textoBusqueda != null && !textoBusqueda.isEmpty()) {
            listaUsuarios = usuarioService.buscarPorFiltro(tipoFiltro, textoBusqueda);
            model.addAttribute("tipoFiltro", tipoFiltro);
            model.addAttribute("textoBusqueda", textoBusqueda);
        } else {
            listaUsuarios = usuarioService.listarTodos();
        }

        model.addAttribute("usuarios", listaUsuarios);
        return "admin/usuarios";
    }

    @Transactional
    @GetMapping("/{id}/bonos")
    public String verBonosUsuario(@PathVariable("id") Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);

        if (usuario == null) {
            return "redirect:/admin/usuarios";
        }


        model.addAttribute("usuario", usuario);
        return "admin/usuario-bonos";
    }
}