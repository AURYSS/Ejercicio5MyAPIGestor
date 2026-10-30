package com.mx.cbtis.gestor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.mx.cbtis.gestor.controller.AsignacionesController;
import com.mx.cbtis.gestor.controller.ComentariosController;
import com.mx.cbtis.gestor.controller.ProyectosController;
import com.mx.cbtis.gestor.controller.TareasController;
import com.mx.cbtis.gestor.controller.UsuarioController;
import com.mx.cbtis.gestor.modelo.Asignaciones;
import com.mx.cbtis.gestor.modelo.Comentarios;
import com.mx.cbtis.gestor.modelo.Proyectos;
import com.mx.cbtis.gestor.modelo.Tareas;
import com.mx.cbtis.gestor.modelo.Usuario;
import com.mx.cbtis.gestor.repository.AsignacionesRepository;
import com.mx.cbtis.gestor.repository.ComentariosRepository;
import com.mx.cbtis.gestor.repository.ProtectosRepository;
import com.mx.cbtis.gestor.repository.TareasRepository;
import com.mx.cbtis.gestor.repository.UsuarioRepository;

@SpringBootTest
class Ejercicio5MyApiGestorApplicationTests {

    @Autowired
    private UsuarioController usuarioController;

    @Autowired
    private ProyectosController proyectosController;

    @Autowired
    private TareasController tareasController;

    @Autowired
    private AsignacionesController asignacionesController;

    @Autowired
    private ComentariosController comentariosController;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private ProtectosRepository proyectosRepository;

    @MockBean
    private TareasRepository tareasRepository;

    @MockBean
    private AsignacionesRepository asignacionesRepository;

    @MockBean
    private ComentariosRepository comentariosRepository;

    @Test
    void contextLoads() {
        assertNotNull(usuarioController);
        assertNotNull(proyectosController);
        assertNotNull(tareasController);
        assertNotNull(asignacionesController);
        assertNotNull(comentariosController);
    }

    // --- PRUEBAS DE USUARIO CONTROLLER ---
    @Test
    void testUsuarioLoginExitoso() {
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId_usuario(1);
        usuarioMock.setNombre("Aurora");
        usuarioMock.setCorreo_electronico("aurora@test.com");
        usuarioMock.setContrasena("1234");

        when(usuarioRepository.findByCorreoPass("aurora@test.com", "1234"))
                .thenReturn(Optional.of(usuarioMock));

        Usuario query = new Usuario();
        query.setCorreo_electronico("aurora@test.com");
        query.setContrasena("1234");

        Usuario resultado = usuarioController.getUsuarioLogin(query);
        assertEquals("Aurora", resultado.getNombre());
        assertEquals(1, resultado.getId_usuario());
    }

    @Test
    void testUsuarioLoginFallido() {
        when(usuarioRepository.findByCorreoPass("invalido@test.com", "0000"))
                .thenReturn(Optional.empty());

        Usuario query = new Usuario();
        query.setCorreo_electronico("invalido@test.com");
        query.setContrasena("0000");

        Usuario resultado = usuarioController.getUsuarioLogin(query);
        assertEquals("000", resultado.getNombre());
    }

    @Test
    void testBuscarUsuarioPorId() {
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId_usuario(5);
        usuarioMock.setNombre("TestUser");

        when(usuarioRepository.findById(5)).thenReturn(Optional.of(usuarioMock));

        Usuario res = usuarioController.getUsuario(5);
        assertEquals("TestUser", res.getNombre());
    }

    @Test
    void testAddUsuario() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        assertEquals(1, usuarioController.addUsuario(usuario));
    }

    // --- PRUEBAS DE PROYECTOS CONTROLLER ---
    @Test
    void testBuscarProyectoPorId() {
        Proyectos p = new Proyectos();
        p.setId_proyecto(10);
        p.setNombre("Proyecto 1");

        when(proyectosRepository.findById(10)).thenReturn(Optional.of(p));

        Proyectos res = proyectosController.getProyecto(10);
        assertEquals("Proyecto 1", res.getNombre());
    }

    @Test
    void testCrudProyecto() {
        Proyectos p = new Proyectos();
        when(proyectosRepository.save(any(Proyectos.class))).thenReturn(p);
        doNothing().when(proyectosRepository).delete(any(Proyectos.class));
        when(proyectosRepository.findAll()).thenReturn(Collections.singletonList(p));

        assertEquals(1, proyectosController.addProyecto(p));
        assertEquals(1, proyectosController.updateProyecto(p));
        assertEquals(1, proyectosController.deleteProyecto(p));
        assertEquals(1, proyectosController.getProyectos().size());
    }

    // --- PRUEBAS DE TAREAS CONTROLLER ---
    @Test
    void testBuscarTareaPorId() {
        Tareas t = new Tareas();
        t.setId_tarea(3);
        t.setNombre("Tarea Test");

        when(tareasRepository.findById(3)).thenReturn(Optional.of(t));

        Tareas res = tareasController.getTarea(3);
        assertEquals("Tarea Test", res.getNombre());
    }

    @Test
    void testCrudTareas() {
        Tareas t = new Tareas();
        t.setId_proyecto(2);

        when(tareasRepository.findByIdProyecto(2)).thenReturn(Collections.singletonList(t));
        when(tareasRepository.save(any(Tareas.class))).thenReturn(t);
        doNothing().when(tareasRepository).delete(any(Tareas.class));
        when(tareasRepository.findAll()).thenReturn(Collections.singletonList(t));

        assertEquals(1, tareasController.getTarea(t).size());
        assertEquals(1, tareasController.addTareas(t));
        assertEquals(1, tareasController.updateTareas(t));
        assertEquals(1, tareasController.deleteTareas(t));
        assertEquals(1, tareasController.getTareas().size());
    }

    // --- PRUEBAS DE ASIGNACIONES CONTROLLER ---
    @Test
    void testCrudAsignaciones() {
        Asignaciones a = new Asignaciones();
        a.setId_asignacion(1);

        when(asignacionesRepository.findById(1)).thenReturn(Optional.of(a));
        when(asignacionesRepository.save(any(Asignaciones.class))).thenReturn(a);
        doNothing().when(asignacionesRepository).delete(any(Asignaciones.class));
        when(asignacionesRepository.findAll()).thenReturn(Collections.singletonList(a));

        assertNotNull(asignacionesController.getAsignacion(1));
        assertEquals(1, asignacionesController.addAsignacion(a));
        assertEquals(1, asignacionesController.updateAsignacion(a));
        assertEquals(1, asignacionesController.deleteAsignacion(a));
        assertEquals(1, asignacionesController.getAsignaciones().size());
    }

    // --- PRUEBAS DE COMENTARIOS CONTROLLER ---
    @Test
    void testCrudComentarios() {
        Comentarios c = new Comentarios();
        c.setId_comentario(7);
        c.setId_tarea(4);

        when(comentariosRepository.findById(7)).thenReturn(Optional.of(c));
        when(comentariosRepository.findByTarea(4)).thenReturn(Collections.singletonList(c));
        when(comentariosRepository.save(any(Comentarios.class))).thenReturn(c);
        doNothing().when(comentariosRepository).delete(any(Comentarios.class));
        when(comentariosRepository.findAll()).thenReturn(Collections.singletonList(c));

        assertNotNull(comentariosController.getComentario(7));
        assertEquals(1, comentariosController.getComentario(c).size());
        assertEquals(1, comentariosController.addComentario(c));
        assertEquals(1, comentariosController.updateComentario(c));
        assertEquals(1, comentariosController.deleteComentario(c));
        assertEquals(1, comentariosController.getComentarios().size());
    }
}
