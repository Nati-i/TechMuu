package testeMaven.com.techmuu.dao;

import testeMaven.com.techmuu.model.Administrador;
import testeMaven.com.techmuu.database.ConexaoSupabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AdministradorDAO {

  
    public static Administrador buscarPorId(int id) {
        String sql = "SELECT id_administrador, nome, email, telefone, nivel_acesso FROM administrador WHERE id_administrador = ?";
        Administrador admin = null;

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                admin = new Administrador(
                    rs.getInt("id_administrador"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    rs.getString("nivel_acesso")
                );
                System.out.println("✓ Administrador encontrado: " + admin.getNome());
            } else {
                System.out.println("✗ Administrador com ID " + id + " não encontrado");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao buscar administrador: " + e.getMessage());
        }

        return admin;
    }

   
    public static Administrador buscarPorEmail(String email) {
        String sql = "SELECT id_administrador, nome, email, telefone, nivel_acesso FROM administrador WHERE email = ?";
        Administrador admin = null;

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                admin = new Administrador(
                    rs.getInt("id_administrador"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    rs.getString("nivel_acesso")
                );
                System.out.println("✓ Administrador encontrado: " + admin.getNome());
            } else {
                System.out.println("✗ Administrador com email " + email + " não encontrado");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao buscar administrador: " + e.getMessage());
        }

        return admin;
    }

   
    public static List<Administrador> listarTodos() {
        List<Administrador> administradores = new ArrayList<>();
        String sql = "SELECT id_administrador, nome, email, telefone, nivel_acesso FROM administrador";

        try (Connection conexao = ConexaoSupabase.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Administrador admin = new Administrador(
                    rs.getInt("id_administrador"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    rs.getString("nivel_acesso")
                );
                administradores.add(admin);
            }

            System.out.println("✓ " + administradores.size() + " administradores listados");

        } catch (Exception e) {
            System.err.println("✗ Erro ao listar administradores: " + e.getMessage());
        }

        return administradores;
    }

    
    public static List<Administrador> listarPorNivel(String nivelAcesso) {
        List<Administrador> administradores = new ArrayList<>();
        String sql = "SELECT id_administrador, nome, email, telefone, nivel_acesso FROM administrador WHERE nivel_acesso = ?";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, nivelAcesso);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Administrador admin = new Administrador(
                    rs.getInt("id_administrador"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    rs.getString("nivel_acesso")
                );
                administradores.add(admin);
            }

            System.out.println("✓ " + administradores.size() + " administradores com nível '" + nivelAcesso + "' encontrados");

        } catch (Exception e) {
            System.err.println("✗ Erro ao listar administradores por nível: " + e.getMessage());
        }

        return administradores;
    }

    
    public static boolean inserir(Administrador admin, String senhaHash) {
        String sql = "INSERT INTO administrador (nome, email, telefone, nivel_acesso, senha_hash) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, admin.getNome());
            pstmt.setString(2, admin.getEmail());
            pstmt.setString(3, admin.getTelefone());
            pstmt.setString(4, admin.getNivelAcesso());
            pstmt.setString(5, senhaHash);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Administrador inserido com sucesso: " + admin.getNome());
                return true;
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao inserir administrador: " + e.getMessage());
        }

        return false;
    }

    
    public static boolean atualizar(Administrador admin) {
        String sql = "UPDATE administrador SET nome = ?, email = ?, telefone = ?, nivel_acesso = ? WHERE id_administrador = ?";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, admin.getNome());
            pstmt.setString(2, admin.getEmail());
            pstmt.setString(3, admin.getTelefone());
            pstmt.setString(4, admin.getNivelAcesso());
            pstmt.setInt(5, admin.getId());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Administrador atualizado com sucesso: " + admin.getNome());
                return true;
            } else {
                System.out.println("✗ Nenhum administrador foi atualizado (ID não encontrado)");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao atualizar administrador: " + e.getMessage());
        }

        return false;
    }

    
    public static boolean deletar(int id) {
        String sql = "DELETE FROM administrador WHERE id_administrador = ?";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Administrador deletado com sucesso (ID: " + id + ")");
                return true;
            } else {
                System.out.println("✗ Nenhum administrador foi deletado (ID não encontrado)");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao deletar administrador: " + e.getMessage());
        }

        return false;
    }

    
    public static void main(String[] args) {
        System.out.println("=== TESTANDO ADMINISTRADORDAO ===\n");

        // 1. Listar todos os administradores
        System.out.println("1. Listando administradores:");
        List<Administrador> admins = listarTodos();
        admins.forEach(System.out::println);

        // 2. Inserir novo administrador
        System.out.println("\n2. Inserindo novo administrador:");
        Administrador novoAdmin = new Administrador(0, "Maria Santos", "maria@teste.com", "(11) 91234-5678", "alto");
        inserir(novoAdmin, "hash_senha_maria123"); // Passa o hash da senha

        // 3. Buscar por email
        System.out.println("\n3. Buscando administrador por email:");
        Administrador adminEncontrado = buscarPorEmail("maria@teste.com");
        if (adminEncontrado != null) {
            System.out.println(adminEncontrado);
            System.out.println("Tem permissão para ação crítica? " + adminEncontrado.temPermissao("ação_critica"));
        }

        // 4. Listar por nível de acesso
        System.out.println("\n4. Listando administradores com nível 'alto':");
        List<Administrador> adminsAltoNivel = listarPorNivel("alto");
        adminsAltoNivel.forEach(System.out::println);

        // 5. Atualizar administrador (se foi encontrado)
        if (adminEncontrado != null) {
            System.out.println("\n5. Atualizando nível de acesso:");
            Administrador adminAtualizado = new Administrador(
                adminEncontrado.getId(),
                adminEncontrado.getNome(),
                adminEncontrado.getEmail(),
                adminEncontrado.getTelefone(),
                "médio"
            );
            atualizar(adminAtualizado);
        }

        

        System.out.println("\n=== FIM DOS TESTES ===");
    }
}
