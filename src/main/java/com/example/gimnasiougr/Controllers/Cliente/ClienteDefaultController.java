package com.example.gimnasiougr.Controllers.Cliente;

import com.example.gimnasiougr.Controllers.LoginController;
import com.example.gimnasiougr.Models.*;
import com.example.gimnasiougr.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteDefaultController {

    private final ClienteRepository clienteRepository;

    @GetMapping({"", "/", "/index"})
    public String index(Model model) {
        Usuario usuario = LoginController.usuarioLogeadoGlobal;
        if (usuario == null) return "redirect:/";

        model.addAttribute("cliente", clienteRepository.findByUsuario(usuario).orElseThrow());
        return "cliente/index";
    }

}