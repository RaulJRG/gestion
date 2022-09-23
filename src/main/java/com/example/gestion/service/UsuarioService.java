package com.example.gestion.service;

import com.example.gestion.model.Usuario;
import org.springframework.data.domain.Page;

/** Interfaz del servicio para gestionar a la entidad Usuario
 * @author RaulJRG
 * @version 1.0
 */
public interface UsuarioService {

    public Page<Usuario> getAllUsuarios(int page);

    public Usuario getUsuarioById(Long id);

    public boolean ifExisits(Long id);

    public void crearUsuario(Usuario usuario);

    public void actualizarUsuario(Usuario usuario);

    public void removeUsuario(Long id);

}

