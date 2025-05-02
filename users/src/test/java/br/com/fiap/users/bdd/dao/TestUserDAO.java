package br.com.fiap.users.bdd.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DAO para operações de banco de dados nos testes
 */
@Component
public class TestUserDAO {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Verifica se um usuário com o email fornecido existe
     */
    public boolean userExistsByEmail(String email) {
        List<Map<String, Object>> users = jdbcTemplate.queryForList(
            "SELECT * FROM tbl_user WHERE email = ?", email);
        return !users.isEmpty();
    }
    
    /**
     * Verifica se um usuário com o ID fornecido existe
     */
    public boolean userExistsById(Long id) {
        List<Map<String, Object>> users = jdbcTemplate.queryForList(
            "SELECT * FROM tbl_user WHERE id = ?", id);
        return !users.isEmpty();
    }
    
    /**
     * Busca um usuário pelo email
     */
    public Optional<Map<String, Object>> findUserByEmail(String email) {
        List<Map<String, Object>> users = jdbcTemplate.queryForList(
            "SELECT * FROM tbl_user WHERE email = ?", email);
        
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
    
    /**
     * Busca um usuário pelo ID
     */
    public Optional<Map<String, Object>> findUserById(Long id) {
        List<Map<String, Object>> users = jdbcTemplate.queryForList(
            "SELECT * FROM tbl_user WHERE id = ?", id);
        
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
    
    /**
     * Remove um usuário pelo email
     */
    public boolean deleteUserByEmail(String email) {
        try {
            int rowsAffected = jdbcTemplate.update("DELETE FROM tbl_user WHERE email = ?", email);
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Erro ao remover usuário por email: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Remove um usuário pelo ID
     */
    public boolean deleteUserById(Long id) {
        try {
            int rowsAffected = jdbcTemplate.update("DELETE FROM tbl_user WHERE id = ?", id);
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Erro ao remover usuário por ID: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cria um usuário comum para testes
     */
    public boolean createTestUser(Long id, String name, String email, String password) {
        try {
            // Verificar e remover usuário se já existir (para evitar conflito)
            deleteUserByEmail(email);
            
            // Inserir novo usuário
            jdbcTemplate.update(
                "INSERT INTO tbl_user (id, email, name, password, role) VALUES (?, ?, ?, ?, ?)",
                id, email, name, passwordEncoder.encode(password), "USER"
            );
            
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao criar usuário para teste: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cria um administrador para testes
     */
    public boolean createAdminUser(Long id, String name, String email, String password) {
        try {
            // Verificar e remover usuário se já existir (para evitar conflito)
            deleteUserByEmail(email);
            
            // Inserir novo usuário
            jdbcTemplate.update(
                "INSERT INTO tbl_user (id, email, name, password, role) VALUES (?, ?, ?, ?, ?)",
                id, email, name, passwordEncoder.encode(password), "ADMIN"
            );
            
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao criar admin para teste: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtém o próximo ID disponível para novos usuários
     */
    public Long getNextAvailableId() {
        try {
            Integer maxId = jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(id), 0) FROM tbl_user", Integer.class);
            return maxId != null ? Long.valueOf(maxId + 1) : 1L;
        } catch (Exception e) {
            System.err.println("Erro ao obter próximo ID: " + e.getMessage());
            return 9999L; // ID grande para minimizar conflitos
        }
    }
    
    /**
     * Garante que um usuário admin existe para testes (cria se necessário)
     */
    public boolean ensureAdminExists(String email, String password) {
        if (userExistsByEmail(email)) {
            return true;
        }
        
        return createAdminUser(getNextAvailableId(), "Admin Teste", email, password);
    }
    
    /**
     * Limpa todos os usuários do banco de dados
     * CUIDADO: Use apenas em ambiente de teste!
     */
    public void clearAllUsers() {
        try {
            jdbcTemplate.update("DELETE FROM tbl_user");
            System.out.println("Todos os usuários foram removidos do banco de dados de teste");
        } catch (Exception e) {
            System.err.println("Erro ao limpar usuários: " + e.getMessage());
        }
    }
}
