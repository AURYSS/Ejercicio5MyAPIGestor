package com.mx.cbtis.gestor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.mx.cbtis.gestor.controller.UsuarioController;
import com.mx.cbtis.gestor.modelo.Usuario;
import com.mx.cbtis.gestor.repository.UsuarioRepository;

@SpringBootTest(classes = {
    UsuarioController.class
})
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
class Ejercicio5MyApiGestorApplicationTests {

    @Autowired
    private UsuarioController usuarioController;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    void contextLoads() {
        assertNotNull(usuarioController);
    }

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

        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());
        assertNotNull(usuarioController.getUsuario(999));
    }

    @Test
    void testCrudUsuario() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        doNothing().when(usuarioRepository).delete(any(Usuario.class));
        when(usuarioRepository.findAll()).thenReturn(Collections.singletonList(usuario));

        assertEquals(1, usuarioController.addUsuario(usuario));
        assertEquals(1, usuarioController.updateUsuario(usuario));
        assertEquals(1, usuarioController.deleteUsuario(usuario));
        assertEquals(1, usuarioController.getUsuarios().size());

        // Cubre rama false cuando usuario es null (flag = 0)
        assertEquals(0, usuarioController.addUsuario(null));
        assertEquals(0, usuarioController.updateUsuario(null));
        assertEquals(0, usuarioController.deleteUsuario(null));
    }
}
