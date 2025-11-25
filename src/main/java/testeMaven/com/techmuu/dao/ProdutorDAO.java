package testeMaven.com.techmuu.dao;

import testeMaven.com.techmuu.model.Produtor;
import testeMaven.com.techmuu.database.ConexaoSupabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProdutorDAO {

    public static Produtor buscarPorId(int id) {
        String sql = "SELECT id_produtor, nome, email, telefone, senha_hash FROM produtor WHERE id_produtor = ?";
        Produtor produtor = null;

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                produtor = new Produtor(
                    rs.getInt("id_produtor"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    rs.getString("senha_hash")
                );
                System.out.println("✓ Produtor encontrado: " + produtor.getNome());
            } else {
                System.out.println("✗ Produtor com ID " + id + " não encontrado");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao buscar produtor: " + e.getMessage());
        }

        return produtor;
    }

    
    public static Produtor buscarPorEmail(String email) {
        String sql = "SELECT id_produtor, nome, email, telefone, senha_hash FROM produtor WHERE email = ?";
        Produtor produtor = null;

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                produtor = new Produtor(
                    rs.getInt("id_produtor"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    rs.getString("senha_hash")
                );
                System.out.println("✓ Produtor encontrado: " + produtor.getNome());
            } else {
                System.out.println("✗ Produtor com email " + email + " não encontrado");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao buscar produtor: " + e.getMessage());
        }

        return produtor;
    }

   
    public static List<Produtor> listarTodos() {
        List<Produtor> produtores = new ArrayList<>();
        String sql = "SELECT id_produtor, nome, email, telefone, senha_hash FROM produtor";

        try (Connection conexao = ConexaoSupabase.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produtor produtor = new Produtor(
                    rs.getInt("id_produtor"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone"),
                    rs.getString("senha_hash")
                );
                produtores.add(produtor);
            }

            System.out.println("✓ " + produtores.size() + " produtores listados");

        } catch (Exception e) {
            System.err.println("✗ Erro ao listar produtores: " + e.getMessage());
        }

        return produtores;
    }

   
    public static Produtor autenticar(String email, String senha) {
        Produtor produtor = buscarPorEmail(email);
        
        if (produtor != null && produtor.autenticar(email, senha)) {
            System.out.println("✓ Produtor autenticado com sucesso!");
            return produtor;
        }
        
        System.out.println("✗ Falha na autenticação");
        return null;
    }

    
    public static boolean inserir(Produtor produtor) {
        String sql = "INSERT INTO produtor (nome, email, telefone, senha_hash) VALUES (?, ?, ?, ?)";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, produtor.getNome());
            pstmt.setString(2, produtor.getEmail());
            pstmt.setString(3, produtor.getTelefone());
            pstmt.setString(4, produtor.getSenhaHash());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Produtor inserido com sucesso: " + produtor.getNome());
                return true;
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao inserir produtor: " + e.getMessage());
        }

        return false;
    }

    
    public static boolean atualizar(Produtor produtor) {
        String sql = "UPDATE produtor SET nome = ?, email = ?, telefone = ?, senha_hash = ? WHERE id_produtor = ?";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, produtor.getNome());
            pstmt.setString(2, produtor.getEmail());
            pstmt.setString(3, produtor.getTelefone());
            pstmt.setString(4, produtor.getSenhaHash());
            pstmt.setInt(5, produtor.getId());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Produtor atualizado com sucesso: " + produtor.getNome());
                return true;
            } else {
                System.out.println("✗ Nenhum produtor foi atualizado (ID não encontrado)");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao atualizar produtor: " + e.getMessage());
        }

        return false;
    }

    
    public static boolean deletar(int id) {
        String sql = "DELETE FROM produtor WHERE id_produtor = ?";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Produtor deletado com sucesso (ID: " + id + ")");
                return true;
            } else {
                System.out.println("✗ Nenhum produtor foi deletado (ID não encontrado)");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao deletar produtor: " + e.getMessage());
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println("=== TESTANDO PRODUTORDAO ===\n");

        // 1. Listar todos os produtores
        System.out.println("1. Listando produtores:");
        List<Produtor> produtores = listarTodos();
        produtores.forEach(System.out::println);

        // 2. Inserir novo produtor
        System.out.println("\n2. Inserindo novo produtor:");
        Produtor novoProdutor = new Produtor(0, "João da Silva", "joao@teste.com", "(11) 98765-4321", "senha123hash");
        inserir(novoProdutor);

        // 3. Buscar por email
        System.out.println("\n3. Buscando produtor por email:");
        Produtor produtorEncontrado = buscarPorEmail("joao@teste.com");
        if (produtorEncontrado != null) {
            System.out.println(produtorEncontrado);
        }

        // 4. Testar autenticação
        System.out.println("\n4. Testando autenticação:");
        Produtor produtorAutenticado = autenticar("joao@teste.com", "senha123hash");
        if (produtorAutenticado != null) {
            System.out.println("Login bem-sucedido: " + produtorAutenticado.getNome());
        }

        // 5. Atualizar produtor (se foi encontrado)
        if (produtorEncontrado != null) {
            System.out.println("\n5. Atualizando telefone do produtor:");
            Produtor produtorAtualizado = new Produtor(
                produtorEncontrado.getId(),
                produtorEncontrado.getNome(),
                produtorEncontrado.getEmail(),
                "(11) 91111-2222",
                produtorEncontrado.getSenhaHash()
            );
            atualizar(produtorAtualizado);
        }

 

        System.out.println("\n=== FIM DOS TESTES ===");
    }
}