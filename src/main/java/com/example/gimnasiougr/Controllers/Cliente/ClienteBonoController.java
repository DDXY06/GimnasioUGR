package com.example.gimnasiougr.Controllers.Cliente;

import com.example.gimnasiougr.Models.BonoDTO;
import com.example.gimnasiougr.Services.BonoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteBonoController {

    private final BonoService bonoService;

    public ClienteBonoController(BonoService bonoService) {
        this.bonoService = bonoService;
    }

    @GetMapping("/mis-bonos")
    public String misBonos(HttpSession session, Model model) {
        Long clienteId = (Long) session.getAttribute("clienteId");
        String nombreCliente = (String) session.getAttribute("nombreCliente");

        List<BonoDTO> bonos = bonoService.buscarPorClienteId(clienteId);

        model.addAttribute("bonos", bonos);
        model.addAttribute("nombreCliente", nombreCliente);
        return "cliente/mis-bonos";
    }
}