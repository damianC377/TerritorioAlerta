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

/** Pruebas JUnit de las operaciones administrativas con PostgreSQL real. */
// Inicia Spring y conecta el servicio a los repositorios de la aplicación.
@SpringBootTest
// Revierte los datos creados y modificados al terminar cada prueba.
@Transactional
class AdminServiceTest {

    // Inyecta el servicio administrativo real que se va a probar.
    @Autowired
    private AdminService adminService;

    // Inyecta el servicio que crea los usuarios iniciales.
    @Autowired
    private CreateUserService createUserService;

    // Inyecta el servicio de búsqueda para leer los cambios persistidos.
    @Autowired
    private FindByIdUserService findByIdUserService;

    // Comprueba que AdminService cambia el rol y lo persiste en PostgreSQL.
    @Test
    void cambiarRol_usuarioPersistido_actualizaRolJal() throws Exception {
        // Inserta un usuario real con su rol inicial.
        User saved = createUserService.createUser(newUser());
        // Asigna el rol Jal usando AdminService.
        adminService.changeUserRole(saved, Role.Jal);
        // Recupera el usuario desde la base para no depender solo del objeto en memoria.
        User updated = findByIdUserService.findByIdUser(saved.getId_user());
        // Comprueba que PostgreSQL contiene el rol actualizado.
        assertEquals(Role.Jal, updated.getRole());
    }

    // Comprueba que el rol Analyst también se persiste por la ruta administrativa.
    @Test
    void cambiarRol_usuarioPersistido_actualizaRolAnalyst() throws Exception {
        // Crea un usuario real antes de cambiarlo.
        User saved = createUserService.createUser(newUser());
        // Solicita el cambio de rol al servicio administrativo.
        adminService.changeUserRole(saved, Role.Analyst);
        // Lee el usuario actualizado desde PostgreSQL.
        User updated = findByIdUserService.findByIdUser(saved.getId_user());
        // Comprueba el valor almacenado.
        assertEquals(Role.Analyst, updated.getRole());
    }

    // Comprueba que el servicio rechaza la falta de usuario.
    @Test
    void cambiarRol_usuarioNulo_lanzaExcepcion() {
        // Confirma que pasar null produce una excepción de dominio.
        assertThrows(Exception.class, () -> adminService.changeUserRole(null, Role.User));
    }

    // Construye un usuario válido para persistir antes del cambio de rol.
    private User newUser() {
        // Crea el objeto de dominio.
        User user = new User();
        // Asigna el nombre de prueba.
        user.setName("JUnit");
        // Asigna el apellido de prueba.
        user.setLastname("Integration");
        // Crea un email único para no chocar con otros datos.
        user.setEmail("junit-" + UUID.randomUUID() + "@example.test");
        // Completa la contraseña.
        user.setPassword("test-password");
        // Completa la comuna.
        user.setCommune("Test commune");
        // Completa el barrio.
        user.setNeighborhood("Test neighborhood");
        // Asigna el rol antes de probar el cambio.
        user.setRole(Role.User);
        // Define la fecha de creación.
        user.setCreation_date(LocalDateTime.now());
        // Devuelve el usuario listo para insertar.
        return user;
    }
    // ==================== AQUÍ EMPIEZAN LAS PRUEBAS MOCKITO ====================

    // Caso Mockito exitoso: asigna el rol Jal y manda guardar al usuario.
    @Test
    void mockito_adminService_usuarioValido_asignaJalYGuarda() throws Exception {
        // Crea un puerto falso para comprobar la llamada a save sin PostgreSQL.
        TerritorioAlerta.domain.port.UserPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.UserPort.class);
        // Construye AdminService conectado al puerto simulado.
        AdminService mockService = new AdminService(mockPort);
        // Prepara el usuario que se va a actualizar.
        User user = new User();
        // Simula la respuesta que entregaría el puerto al guardar.
        org.mockito.Mockito.when(mockPort.save(user)).thenReturn(user);
        // Ejecuta el cambio administrativo al rol Jal.
        mockService.changeUserRole(user, Role.Jal);
        // Comprueba que el modelo recibió el nuevo rol.
        org.junit.jupiter.api.Assertions.assertEquals(Role.Jal, user.getRole());
        // Comprueba que el puerto recibió exactamente ese usuario.
        org.mockito.Mockito.verify(mockPort).save(user);
    }

    // Caso Mockito exitoso adicional: cambia al rol Analyst y guarda el usuario.
    @Test
    void mockito_adminService_usuarioValido_asignaAnalystYGuarda() throws Exception {
        // Crea un mock separado para el segundo caso positivo.
        TerritorioAlerta.domain.port.UserPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.UserPort.class);
        // Construye el servicio con la dependencia simulada.
        AdminService mockService = new AdminService(mockPort);
        // Prepara al usuario que recibirá el nuevo rol.
        User user = new User();
        // Configura la respuesta del guardado.
        org.mockito.Mockito.when(mockPort.save(user)).thenReturn(user);
        // Ejecuta el cambio administrativo al rol Analyst.
        mockService.changeUserRole(user, Role.Analyst);
        // Comprueba que el objeto tiene el rol solicitado.
        org.junit.jupiter.api.Assertions.assertEquals(Role.Analyst, user.getRole());
        // Comprueba que el servicio pidió guardar al usuario.
        org.mockito.Mockito.verify(mockPort).save(user);
    }

    // Caso Mockito negativo: un usuario nulo genera una excepción y no se guarda.
    @Test
    void mockito_adminService_usuarioNulo_lanzaExcepcion() {
        // Crea un UserPort falso para comprobar que save no se ejecuta.
        TerritorioAlerta.domain.port.UserPort mockPort = org.mockito.Mockito.mock(
                TerritorioAlerta.domain.port.UserPort.class);
        // Construye el servicio administrativo con ese mock.
        AdminService mockService = new AdminService(mockPort);
        // Comprueba que la entrada nula produce una excepción.
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class,
                () -> mockService.changeUserRole(null, Role.User));
        // Comprueba que la ruta de error no envió ningún usuario a guardar.
        org.mockito.Mockito.verify(mockPort, org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any(User.class));
    }
}