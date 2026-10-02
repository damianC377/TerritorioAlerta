package TerritorioAlerta.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import TerritorioAlerta.domain.model.User;
import TerritorioAlerta.domain.model.Enums.Role;

/** Pruebas JUnit del cambio de rol usando los servicios y PostgreSQL reales. */
// Inicia Spring para utilizar el servicio y el repositorio reales.
@SpringBootTest
// Revierte el usuario y el cambio al concluir cada prueba.
@Transactional
class ChangeUserRoleServiceTest {

    // Inyecta el servicio de cambio de rol bajo prueba.
    @Autowired
    private ChangeUserRoleService changeUserRoleService;

    // Inyecta el servicio real que crea los usuarios de prueba.
    @Autowired
    private CreateUserService createUserService;

    // Inyecta el servicio de consulta para verificar el cambio persistido.
    @Autowired
    private FindByIdUserService findByIdUserService;

    // Comprueba que el servicio guarda un nuevo rol y que se puede leer después.
    @Test
    void cambiarRol_usuarioPersistido_actualizaRolAnalyst() throws Exception {
        // Inserta el usuario inicial en PostgreSQL.
        User saved = createUserService.createUser(newUser());
        // Cambia el rol usando el servicio real.
        changeUserRoleService.changeUserRole(saved, Role.Analyst);
        // Vuelve a consultar el usuario desde la base de datos.
        User updated = findByIdUserService.findByIdUser(saved.getId_user());
        // Comprueba que el nuevo rol quedó persistido.
        assertEquals(Role.Analyst, updated.getRole());
    }

    // Comprueba que también persiste correctamente el rol de administrador.
    @Test
    void cambiarRol_usuarioPersistido_actualizaRolAdmin() throws Exception {
        // Inserta un usuario real para modificarlo.
        User saved = createUserService.createUser(newUser());
        // Asigna el rol de administrador y guarda el cambio.
        changeUserRoleService.changeUserRole(saved, Role.Admin);
        // Lee otra vez el usuario a través de su servicio de búsqueda.
        User updated = findByIdUserService.findByIdUser(saved.getId_user());
        // Comprueba que el rol recuperado es el de administrador.
        assertEquals(Role.Admin, updated.getRole());
    }

    // Comprueba el camino negativo cuando no se proporciona un usuario.
    @Test
    void cambiarRol_usuarioNulo_lanzaExcepcion() {
        // Confirma que el servicio rechaza el usuario nulo.
        assertThrows(Exception.class, () -> changeUserRoleService.changeUserRole(null, Role.Admin));
    }

    // Prepara un usuario completo con un email único para la base de datos.
    private User newUser() {
        // Crea el objeto que se insertará.
        User user = new User();
        // Asigna un nombre de prueba.
        user.setName("JUnit");
        // Asigna un apellido de prueba.
        user.setLastname("Integration");
        // Genera un email irrepetible.
        user.setEmail("junit-" + UUID.randomUUID() + "@example.test");
        // Completa la contraseña.
        user.setPassword("test-password");
        // Completa la comuna.
        user.setCommune("Test commune");
        // Completa el barrio.
        user.setNeighborhood("Test neighborhood");
        // Asigna un rol inicial válido.
        user.setRole(Role.User);
        // Completa la fecha de creación.
        user.setCreation_date(LocalDateTime.now());
        // Devuelve el usuario preparado.
        return user;
    }
    // ==================== AQUÍ EMPIEZAN LAS PRUEBAS MOCKITO ====================

    // Caso Mockito exitoso: cambia a Analyst y guarda el usuario.
    @Test
    void mockito_cambiarRol_usuarioValido_asignaAnalystYGuarda() throws Exception {
        // Crea un UserPort falso para interceptar la operación save.
        TerritorioAlerta.domain.port.UserPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.UserPort.class);
        // Construye el servicio de roles usando el puerto simulado.
        ChangeUserRoleService mockService = new ChangeUserRoleService(mockPort);
        // Prepara el usuario que recibirá el nuevo rol.
        User user = new User();
        // Simula que guardar devuelve el usuario recibido.
        org.mockito.Mockito.when(mockPort.save(user)).thenReturn(user);
        // Ejecuta el cambio de rol con el servicio aislado.
        mockService.changeUserRole(user, Role.Analyst);
        // Comprueba que el usuario ahora tiene el rol Analyst.
        org.junit.jupiter.api.Assertions.assertEquals(Role.Analyst, user.getRole());
        // Comprueba que el servicio pidió guardar ese usuario.
        org.mockito.Mockito.verify(mockPort).save(user);
    }

    // Caso Mockito exitoso adicional: asigna el rol Admin y guarda.
    @Test
    void mockito_cambiarRol_usuarioValido_asignaAdminYGuarda() throws Exception {
        // Crea otro puerto falso, independiente del resto de pruebas.
        TerritorioAlerta.domain.port.UserPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.UserPort.class);
        // Construye otra instancia del servicio con la dependencia falsa.
        ChangeUserRoleService mockService = new ChangeUserRoleService(mockPort);
        // Prepara el usuario que se modificará.
        User user = new User();
        // Configura la respuesta simulada del guardado.
        org.mockito.Mockito.when(mockPort.save(user)).thenReturn(user);
        // Ejecuta el cambio de rol hacia Admin.
        mockService.changeUserRole(user, Role.Admin);
        // Comprueba que el usuario tiene el rol Admin.
        org.junit.jupiter.api.Assertions.assertEquals(Role.Admin, user.getRole());
        // Comprueba que el servicio delegó el guardado al puerto.
        org.mockito.Mockito.verify(mockPort).save(user);
    }

    // Caso Mockito negativo: un usuario nulo no se puede guardar ni actualizar.
    @Test
    void mockito_cambiarRol_usuarioNulo_lanzaExcepcion() {
        // Crea un puerto falso para observar si se intenta guardar algo.
        TerritorioAlerta.domain.port.UserPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.UserPort.class);
        // Construye el servicio que recibirá el usuario nulo.
        ChangeUserRoleService mockService = new ChangeUserRoleService(mockPort);
        // Comprueba que el servicio lanza una excepción de validación.
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class,
                () -> mockService.changeUserRole(null, Role.Admin));
        // Comprueba que save no fue llamado en la ruta de error.
        org.mockito.Mockito.verify(mockPort, org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any(User.class));
    }
}