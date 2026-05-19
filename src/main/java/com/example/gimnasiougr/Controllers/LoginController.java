package com.example.gimnasiougr.Controllers;

import com.example.gimnasiougr.Models.TipoUsuario;
import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioRepository usuarioRepository;
    public static Usuario clienteLogeadoGlobal = null;
    public static Usuario entrenadorLogeado = null;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("correo") String correo,
            @RequestParam("contrasenia") String contrasenia,
            Model model) {

        Optional<Usuario> resultado = usuarioRepository.findByCorreoAndContrasenia(correo, contrasenia);

        if (resultado.isEmpty()) {
            model.addAttribute("loginError", "Correo o contraseña incorrectos.");
            return "index";
        }

        Usuario usuario = resultado.get();


        if (usuario.getRol() == TipoUsuario.ADMINISTRADOR) {
            return "redirect:/admin/index";
        } else if (usuario.getRol() == TipoUsuario.ENTRENADOR) {
            entrenadorLogeado = usuario;
            return "redirect:/entrenador";
        } else {
            clienteLogeadoGlobal = usuario;
            return "redirect:/cliente/index";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        clienteLogeadoGlobal = null;
        return "redirect:/";
    }
}