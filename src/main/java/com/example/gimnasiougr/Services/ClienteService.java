package com.example.gimnasiougr.Services;

import com.example.gimnasiougr.Models.BonoDTO;
import com.example.gimnasiougr.Models.Cliente;
import com.example.gimnasiougr.Models.ClienteDTO;
import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Repositories.ClienteRepository;
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
        }
        dto.setNombre(cliente.getNombre());
        dto.setDni(cliente.getDni());
        dto.setTelf(cliente.getTelf());
        dto.setCorreo(cliente.getCorreo());
        dto.setDireccion(cliente.getDireccion());
        dto.setContrasenia(cliente.getContrasenia());

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

    public Cliente mapToEntity(ClienteDTO dto) {
        if (dto == null) return null;
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setNombre(dto.getNombre());
        cliente.setDni(dto.getDni());
        cliente.setTelf(dto.getTelf());
        cliente.setCorreo(dto.getCorreo());
        cliente.setDireccion(dto.getDireccion());
        cliente.setContrasenia(dto.getContrasenia());

        // La asignación de la entidad Usuario se hace con una referencia básica
        // Las relaciones más complejas deberían ser obtenidas de BD.
        if (dto.getUsuarioId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioId());
            cliente.setUsuario(usuario);
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
        clienteRepository.deleteById(id);
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
