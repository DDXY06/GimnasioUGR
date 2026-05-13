package com.example.gimnasiougr.Controllers.Cliente;

import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;


@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteDefaultController {

    private final ClienteRepository clienteRepository;

    @GetMapping({"", "/", "/index"})
    public String index(@SessionAttribute("usuarioLogeado") Usuario usuario, Model model) {

        model.addAttribute("cliente", clienteRepository.findByUsuario(usuario).orElseThrow());

        return "cliente/index";
    }
}