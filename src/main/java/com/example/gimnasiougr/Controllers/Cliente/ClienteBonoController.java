package com.example.gimnasiougr.Controllers.Cliente;

import com.example.gimnasiougr.Controllers.LoginController;
import com.example.gimnasiougr.Models.BonoDTO;
import com.example.gimnasiougr.Models.Cliente;
import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Repositories.ClienteRepository;
import com.example.gimnasiougr.Services.BonoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteBonoController {

    private final BonoService bonoService;
    private final ClienteRepository clienteRepository;

    @GetMapping("/mis-bonos")
    public String misBonos(Model model) {
        Usuario usuarioLogeado = LoginController.usuarioLogeadoGlobal;
        if (usuarioLogeado == null) {
            return "redirect:/";
        }

        Cliente cliente = clienteRepository.findByUsuario(usuarioLogeado).orElseThrow();
        List<BonoDTO> bonos = bonoService.buscarPorClienteId(cliente.getId());

        model.addAttribute("bonos", bonos);
        model.addAttribute("nombreCliente", cliente.getNombre());

        return "cliente/mis-bonos";
    }
}