package com.example.gestion.dao;

import com.example.gestion.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Modulo de persistencia de datos
 * @author RaulJRG
 * @version 1.0
 */
@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, Long> {

    public Usuario findUsuarioById(Long id);

}
