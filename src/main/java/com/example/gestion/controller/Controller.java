package com.example.gestion.controller;

import com.example.gestion.model.Usuario;
import com.example.gestion.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

/** API rest que exhibe las operaciones del recurso Usuario
 * @author RaulJRG
 * @version 1.0
 */
@RestController
@RequestMapping("/usuarios")
public class Controller {

    /** Instancia del servicio del usuario*/
    @Autowired
    private UsuarioService usuarioService;

    /** Busca a todos los usuarios (por pagina)
     * @param page numero de
     * @return un response entity con la pagina de usuarios (nula si no se encuentra)
     */
    @GetMapping()
    public ResponseEntity<Page<Usuario>> getAllUsuarios(@RequestParam int page){
        try {
            //Busca la página de usuarios
            Page<Usuario> pagina = usuarioService.getAllUsuarios(page);
            //Evalua si la pagina es válida
            if(page<pagina.getTotalPages() && page>=0)
                //Se devuelven a los usuarios encontrados en la página correspondiente
                return ResponseEntity.status(HttpStatus.OK).body(usuarioService.getAllUsuarios(page));
            else
                //Devuelve un status de NOT_FOUND con body nulo
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch(Exception e){
            //Indica que no se pudo actualizar y un status de error interno del servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /** Busca a un usuario por id
     * @param id identificador del usuario
     * @return un response entity con el usuario o un mensaje de un posible error
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<?>> getUsuarioById(@PathVariable Long id){
        try {
            //Si el usuario existe
            if(usuarioService.ifExisits(id)) {
                //Lo obtiene y lo envía con status OK
                Usuario usuario = usuarioService.getUsuarioById(id);
                //Si el usuario está activo
                if(usuario.getStatus().equalsIgnoreCase("Active"))
                    //Lo devuelve un estado 200
                    return ResponseEntity.status(HttpStatus.OK).body(Optional.of(usuario));
                //Si no está activo
                else
                    //Lo devuelve con estado 500
                    return ResponseEntity.status(500).body(Optional.of(usuario));
            }
            //Si no existe, envia un cuerpo vacío y una respuesta NOT_FOUND
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.of("El usuario no existe"));
        }
        catch(Exception e){
            //Indica que no se pudo crear y un status de error interno del servidor
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(Optional.of("Error creando al usuario "+e.getMessage()));
        }
    }

    /**Crea un usuario
     * @param usuario objeto Usuario a crear
     * @return un response entity con un mensaje del resultado obtenido
     */
    @PostMapping()
    public ResponseEntity<String> crearUsuario(@Valid @RequestBody() Usuario usuario){
        try {
            if(!usuarioService.ifExisits(usuario.getId())) {
                usuarioService.crearUsuario(usuario);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Usuario creado con éxito");}
            else
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Ya existe el usuario con id ".concat(usuario.getId().toString()));
        }
        catch(DataAccessException e){
            //Indica el error con el acceso a los datos y un status de conflicto
            return ResponseEntity.status(HttpStatus.CONFLICT).
                    body("Error creando al usuario: ".concat(e.getMostSpecificCause().getMessage()));
        }
    }

    /** Actualiza parcialmente un usuario por id
     * @param id identificador del usuario
     * @param campos campos del usuario que se envían para actualizar
     * @return un response entity con un mensaje del resultado obtenido
     */
    @PatchMapping("/{id}")
    public ResponseEntity<String> editarCamposUsuario(@PathVariable(name = "id") Long id, @RequestBody Map<Object,Object> campos){
        try{
            //Si el usuario existe
            if(usuarioService.ifExisits(id)){
                //Se recupera el usuario con el id
                Usuario usuario = usuarioService.getUsuarioById(id);
                //Se usa reflection para actualizar los campos recibidos
                campos.forEach((k,v) ->{
                    Field field = ReflectionUtils.findField(Usuario.class, (String) k);
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, usuario, v);
                });
                //Se guardan los valores en la base de datos
                usuarioService.actualizarUsuario(usuario);
                //Devuelve status OK e indica que se creó con éxito
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se ha actualizado con éxito el usuario con id".concat(id.toString()));
            }
            //Si no existe
            else
                //Devuelve un status NOT_FOUND e indica que no se encontró
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No existe el usuario con id ".concat(id.toString()));
        }
        catch(Exception e){
            //Indica que no se pudo actualizar y un status de error interno del servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error actualizando al usuario con id "+id+": "+e.getMessage());
        }
    }

    /**Actualiza completamente un usuario
     * @param usuario objeto usuario a actualizar
     * @return un response entity con un mensaje del resultado obtenido
     */
    @PutMapping("")
    public ResponseEntity<String> guardarObjetoUsuario(@RequestBody Usuario usuario){
        try{
            //Si el usuario existe
            if(usuarioService.ifExisits(usuario.getId())) {
                //Lo actualiza
                usuarioService.actualizarUsuario(usuario);
                //Devuelve status OK e indica que se creó con éxito
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Se ha actualizado con éxito el usuario con id".concat(usuario.getId().toString()));
            }
            //Si no existe, devuelve un status NOT_FOUND indicando que no se encontro al usuario
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).
                        body("No existe el usuario con id ".concat(usuario.getId().toString()));

        }
        catch(Exception e){
            //Indica que no se pudo actualizar y un status de error interno del servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error actualizando al usuario con id "+usuario.getId()+": "+e.getMessage());
        }
    }

    /** Elimina a un usuario con su id
     * @param id identificador del usuario
     * @return un response entity con un mensaje del resultado obtenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id){
        try {
            //Si el usuario existe
            if(usuarioService.ifExisits(id)) {
                //Se elimina al usuario y se indica con un estado OK
                usuarioService.removeUsuario(id);
                return ResponseEntity.status(HttpStatus.OK).body("Se ha eliminado al usuario con id " + id);
            }
            //Si no existe, devuelve un status NOT_FOUND indicando que no se encontro al usuario
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).
                        body("No existe el usuario con id ".concat(id.toString()));
        }
        catch(Exception e){
            //Indica que no se pudo actualizar y un status de error interno del servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error actualizando al usuario con id "+id+": "+e.getMessage());
        }
    }

}
