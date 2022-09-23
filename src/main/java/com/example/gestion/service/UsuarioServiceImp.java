package com.example.gestion.service;

import com.example.gestion.dao.UsuarioDAO;
import com.example.gestion.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/** Implementación del servicio para gestionar a la entidad Usuario
 * @author RaulJRG
 * @version 1.0
 */
@Service
public class UsuarioServiceImp implements UsuarioService {

    /**Objeto de persistencia de datos*/
    @Autowired
    private UsuarioDAO usuarioDAO;

    /**Obtiene una página de usuarios
     * @param page indica la pagina que se desea consultar
     * @return la pagina correspondiente de usuarios
     */
    public Page<Usuario> getAllUsuarios(int page){
        //Devuelve la página indicada con paginación de 10 en 10
        return usuarioDAO.findAll(PageRequest.of(page,10));
    }

    /**Obtiene a un usuario dado su id
     * @param id el identificador del usuario
     * @return el usuario encontrado
     */
    public Usuario getUsuarioById(Long id){
        return usuarioDAO.findUsuarioById(id);
    }

    /**Determina si un usuario existe o no dado su id
     * @param id el identificador del usuario
     * @return un booleano que indica si existe o no
     */
    public boolean ifExisits(Long id){
        return usuarioDAO.existsById(id);
    }

    /**Crea a un usuario en la base de datos
     * @param usuario el identificador del usuario
     */
    public void crearUsuario(Usuario usuario) {
        usuarioDAO.save(usuario);
    }

    /**Actualiza a un usuario en la base de datos
     * @param usuario objeto usuario que se desea actualizar
     */
    public void actualizarUsuario(Usuario usuario){
        usuarioDAO.save(usuario);
    }

    /**Actualiza a un usuario en la base de datos
     * @param id el identificador del usuario
     */
    public void removeUsuario(Long id){
        usuarioDAO.deleteById(id);
    }

}
