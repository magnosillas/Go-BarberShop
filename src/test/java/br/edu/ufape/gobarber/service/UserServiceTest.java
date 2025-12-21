package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.login.Role;
import br.edu.ufape.gobarber.model.login.User;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade User e suas operações básicas.
 * Estes testes validam a criação, modificação e integridade dos dados de usuários.
 */
@SpringBootTest
public class UserServiceTest {

    /**
     * Testa a criação de um usuário com campos básicos.
     */
    @Test
    public void testUserCreation() {
        User user = new User();
        user.setIdUser(1);
        user.setLogin("admin@gobarber.com");
        user.setPassword("senhaSegura123");

        assertEquals(1, user.getIdUser());
        assertEquals("admin@gobarber.com", user.getLogin());
        assertEquals("senhaSegura123", user.getPassword());
    }

    /**
     * Testa a modificação de dados do usuário.
     */
    @Test
    public void testUpdateUserData() {
        User user = new User();
        user.setLogin("user@test.com");
        user.setPassword("password123");

        assertEquals("user@test.com", user.getLogin());
        assertEquals("password123", user.getPassword());

        user.setLogin("newuser@test.com");
        assertEquals("newuser@test.com", user.getLogin());
    }

    /**
     * Testa o construtor padrão do User.
     */
    @Test
    public void testDefaultConstructor() {
        User user = new User();

        assertNull(user.getIdUser());
        assertNull(user.getLogin());
        assertNull(user.getPassword());
        assertNull(user.getRole());
    }

    /**
     * Testa a implementação de UserDetails - getUsername.
     */
    @Test
    public void testGetUsername() {
        User user = new User();
        user.setLogin("barbeiro@gobarber.com");

        assertEquals("barbeiro@gobarber.com", user.getUsername());
        assertEquals(user.getLogin(), user.getUsername());
    }

    /**
     * Testa a implementação de UserDetails - getPassword.
     */
    @Test
    public void testGetPassword() {
        User user = new User();
        user.setPassword("minhasenha");

        assertEquals("minhasenha", user.getPassword());
    }

    /**
     * Testa a associação de Role ao User.
     */
    @Test
    public void testUserWithRole() {
        Role role = new Role();
        role.setIdRole(1);
        role.setName("ROLE_ADMIN");

        User user = new User();
        user.setRole(role);

        assertNotNull(user.getRole());
        assertEquals("ROLE_ADMIN", user.getRole().getName());
    }

    /**
     * Testa métodos de verificação de conta.
     */
    @Test
    public void testAccountVerificationMethods() {
        User user = new User();

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    /**
     * Testa a lista de authorities (roles).
     */
    @Test
    public void testGetAuthorities() {
        User user = new User();

        assertNotNull(user.getAuthorities());
        assertTrue(user.getAuthorities().isEmpty());
    }

    /**
     * Testa emails com formatos diferentes.
     */
    @Test
    public void testDifferentEmailFormats() {
        User user = new User();

        user.setLogin("user@domain.com");
        assertEquals("user@domain.com", user.getLogin());

        user.setLogin("user.name@domain.com.br");
        assertEquals("user.name@domain.com.br", user.getLogin());

        user.setLogin("user+tag@domain.com");
        assertEquals("user+tag@domain.com", user.getLogin());
    }

    /**
     * Testa a desigualdade entre usuários.
     */
    @Test
    public void testUserInequality() {
        User user1 = new User();
        user1.setIdUser(1);
        user1.setLogin("user1@test.com");

        User user2 = new User();
        user2.setIdUser(2);
        user2.setLogin("user2@test.com");

        assertNotEquals(user1.getIdUser(), user2.getIdUser());
        assertNotEquals(user1.getLogin(), user2.getLogin());
    }

    /**
     * Testa diferentes tipos de Role.
     */
    @Test
    public void testDifferentRoles() {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        Role barberRole = new Role();
        barberRole.setName("ROLE_BARBER");

        Role secretaryRole = new Role();
        secretaryRole.setName("ROLE_SECRETARY");

        User admin = new User();
        admin.setRole(adminRole);

        User barber = new User();
        barber.setRole(barberRole);

        User secretary = new User();
        secretary.setRole(secretaryRole);

        assertEquals("ROLE_ADMIN", admin.getRole().getName());
        assertEquals("ROLE_BARBER", barber.getRole().getName());
        assertEquals("ROLE_SECRETARY", secretary.getRole().getName());
    }
}
