package com.example.gimnasiougr.Services;

import com.example.gimnasiougr.Models.*;
import com.example.gimnasiougr.Repositories.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteDTO mapToDTO(Cliente cliente) {
        if (cliente == null) return null;
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        if (cliente.getUsuario() != null) {
            dto.setUsuarioId(cliente.getUsuario().getId());
            dto.setCorreo(cliente.getUsuario().getCorreo());
        }
        dto.setNombre(cliente.getNombre());
        dto.setDni(cliente.getDni());
        dto.setTelf(cliente.getTelf());
        dto.setDireccion(cliente.getDireccion());

        if (cliente.getBonos() != null) {
            List<BonoDTO> bonoDTOs = cliente.getBonos().stream().map(bono -> {
                BonoDTO bDto = new BonoDTO();
                bDto.setId(bono.getId());
                if (bono.getCliente() != null) {
                    bDto.setUsuarioId(bono.getCliente().getId());
                    bDto.setUsuarioNombre(bono.getCliente().getNombre());
                }
                bDto.setTipo(bono.getTipo());
                bDto.setMaxCupos(bono.getMaxCupos());
                bDto.setFechaCompra(bono.getFechaCompra());
                return bDto;
            }).collect(Collectors.toList());
            dto.setBonos(bonoDTOs);
        }
        return dto;
    }

    private Cliente mapToEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();

        // Si el DTO trae un ID, significa que estamos editando un cliente existente.
        // Lo buscamos en la base de datos para no perder sus datos.
        if (dto.getId() != null) {
            cliente = clienteRepository.findById(dto.getId()).orElse(new Cliente());
        }

        // Asignamos los campos que SÍ existen en tu modelo
        cliente.setDni(dto.getDni());
        cliente.setNombre(dto.getNombre());
        cliente.setTelf(dto.getTelf());          // Aquí es telf, no telefono
        cliente.setDireccion(dto.getDireccion()); // Añadimos direccion

        // Si el cliente no tiene un usuario asignado (es nuevo), lo creamos
        if (cliente.getUsuario() == null) {
            Usuario usuario = new Usuario();
            usuario.setRol(TipoUsuario.CLIENTE);
            cliente.setUsuario(usuario);
        }

        // Asignamos el correo y contraseña al Usuario
        if (dto.getCorreo() != null) {
            cliente.getUsuario().setCorreo(dto.getCorreo());
        }

        // Solo actualizamos la contraseña si nos enviaron una nueva
        if (dto.getContrasenia() != null && !dto.getContrasenia().isEmpty()) {
            cliente.getUsuario().setContrasenia(dto.getContrasenia());
        }

        return cliente;
    }

    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO guardar(ClienteDTO clienteDTO) {
        Cliente cliente = mapToEntity(clienteDTO);
        Cliente guardado = clienteRepository.save(cliente);
        return mapToDTO(guardado);
    }

    public ClienteDTO buscarPorId(Long id) {
        return clienteRepository.findById(id).map(this::mapToDTO).orElse(null);
    }

    public void eliminar(Long id) {
        clienteRepository.findById(id).ifPresent(cliente -> {
            clienteRepository.delete(cliente);
        });
    }

    public List<ClienteDTO> buscarPorFiltro(String tipoFiltro, String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return listarTodos();
        }

        List<Cliente> clientes;
        if ("dni".equalsIgnoreCase(tipoFiltro)) {
            clientes = clienteRepository.findByDniContainingIgnoreCase(textoBusqueda);
        } else if ("nombre".equalsIgnoreCase(tipoFiltro)) {
            clientes = clienteRepository.findByNombreContainingIgnoreCase(textoBusqueda);
        } else {
            clientes = clienteRepository.findAll();
        }

        return clientes.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

}
