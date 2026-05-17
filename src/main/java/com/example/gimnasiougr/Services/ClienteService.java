package com.example.gimnasiougr.Services;

import com.example.gimnasiougr.Models.*;
import com.example.gimnasiougr.Repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final BonoService bonoService;

    public List<ClienteDTO> listarTodos() {
        List<ClienteDTO> clienteDTOS = new ArrayList<>();
        for (Cliente cliente : clienteRepository.findAll()) {
            clienteDTOS.add(mapToDTO(cliente));
        }
        return clienteDTOS;
    }

    @Transactional
    public ClienteDTO guardar(ClienteDTO clienteDTO) {
        Cliente cliente = mapToEntity(clienteDTO);
        Cliente guardado = clienteRepository.save(cliente);
        return mapToDTO(guardado);
    }

    public ClienteDTO buscarPorId(Long id) {
        return mapToDTO(clienteRepository.findById(id).orElse(null));
    }

    @Transactional
    public boolean eliminar(Long id) {
        //Comprobamos que el cliente exista
        if (clienteRepository.existsById(id)) {
            clienteRepository.findById(id).ifPresent(cliente -> {
                clienteRepository.delete(cliente);
            });
            return true;
        }
        return false;
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

    public ClienteDTO buscarPorUsuarioId(Long usuarioId) {
        Cliente cliente = clienteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró ningún cliente asociado al ID de usuario: " + usuarioId));

        return this.mapToDTO(cliente);
    }

    public List<ClienteDTO> listarClientesPorListaCupos(List<CupoDTO> listaCupos){
        ArrayList<ClienteDTO> listaClientes = new ArrayList<>();

        for(CupoDTO cupo: listaCupos){
            listaClientes.add(  this.buscarPorId( cupo.getClienteId() )  );
        }

        return listaClientes;
    }

    public List<ClienteDTO> listarClientesDisponiblesParaClase(Long claseId) {
        List<Cliente> clientesDisponibles = clienteRepository.findClientesNoInscritosEnClase(claseId);
        List<ClienteDTO> clientesDTO = new ArrayList<>();

        for (Cliente cliente : clientesDisponibles) {
            ClienteDTO dto = mapToDTO(cliente);
            clientesDTO.add(dto);
        }

        return clientesDTO;
    }

    public ClienteDTO mapToDTO(Cliente cliente) {
        if (cliente == null) return null;
        ClienteDTO cdto = new ClienteDTO();
        cdto.setId(cliente.getId());
        if (cliente.getUsuario() != null) {
            cdto.setUsuarioId(cliente.getUsuario().getId());
            cdto.setCorreo(cliente.getUsuario().getCorreo());
        }
        cdto.setNombre(cliente.getNombre());
        cdto.setDni(cliente.getDni());
        cdto.setTelf(cliente.getTelf());
        cdto.setDireccion(cliente.getDireccion());

        //Comprobamos que tenga una lista de bonos
        if (cliente.getBonos() != null) {
            List<BonoDTO> bonoDTOs = new ArrayList<>();
            for (Bono bono : cliente.getBonos()) {
                BonoDTO bDto = new BonoDTO();
                bDto.setId(bono.getId());
                if (bono.getCliente() != null) {
                    bDto.setClienteId(bono.getCliente().getId());
                }
                bDto.setTipo(bono.getTipo());
                bDto.setMaxCupos(bono.getMaxCupos());
                bDto.setFechaCompra(bono.getFechaCompra());
                bonoDTOs.add(bDto);
            }
            cdto.setBonos(bonoDTOs);
        }
        return cdto;
    }

    private Cliente mapToEntity(ClienteDTO cdto) {
        Cliente cliente = new Cliente();

        // Si el DTO trae un ID, significa que estamos editando un cliente existente.
        // Lo buscamos en la base de datos para no perder sus datos.
        if (cdto.getId() != null) {
            cliente = clienteRepository.findById(cdto.getId()).orElse(new Cliente());
        }

        // Asignamos los campos que SÍ existen en tu modelo
        cliente.setDni(cdto.getDni());
        cliente.setNombre(cdto.getNombre());
        cliente.setTelf(cdto.getTelf());
        cliente.setDireccion(cdto.getDireccion());

        // Si el cliente no tiene un usuario asignado (es nuevo), lo creamos
        if (cliente.getUsuario() == null) {
            Usuario usuario = new Usuario();
            usuario.setRol(TipoUsuario.CLIENTE);
            cliente.setUsuario(usuario);
        }

        // Asignamos el correo y contraseña al Usuario
        if (cdto.getCorreo() != null) {
            cliente.getUsuario().setCorreo(cdto.getCorreo());
        }

        // Solo actualizamos la contraseña si nos enviaron una nueva
        if (cdto.getContrasenia() != null && !cdto.getContrasenia().isEmpty()) {
            cliente.getUsuario().setContrasenia(cdto.getContrasenia());
        }

        //Si el ultimo bono no tiene fecha significa que es nuevo
        //Le damos la fecha de la transacción
        if (cdto.getBonos() != null && !cdto.getBonos().isEmpty()) {
            if(cdto.getBonos().getLast().getFechaCompra() == null) {
                cdto.getBonos().getLast().setFechaCompra(LocalDate.now());
            }

            List<Bono> bonos = new ArrayList<>();
            for(BonoDTO bDto : cdto.getBonos()) {
                Bono bono = bonoService.mapToEntity(bDto);
                if (bono.getCliente() == null) {bono.setCliente(cliente);}
                bonos.add(bono);
            }
            cliente.setBonos(bonos);
        }

        return cliente;
    }
}
