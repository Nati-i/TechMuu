package testeMaven.com.techmuu.dao;

import testeMaven.com.techmuu.model.Alerta;
import testeMaven.com.techmuu.database.ConexaoSupabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO {
 
    public static Alerta buscarPorId(int id) {
        String sql = "SELECT id_alerta, tipo_alerta, descricao, nivel, resolvido FROM alerta WHERE id_alerta = ?";
        Alerta alerta = null;

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                alerta = new Alerta(
                    rs.getInt("id_alerta"),
                    rs.getString("tipo_alerta"),
                    rs.getString("descricao"),
                    rs.getString("nivel")
                );
                if (rs.getBoolean("resolvido")) {
                    alerta.resolverAlerta();
                }
                System.out.println("✓ Alerta encontrado: " + alerta.getTipoAlerta());
            } else {
                System.out.println("✗ Alerta com ID " + id + " não encontrado");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao buscar alerta: " + e.getMessage());
        }

        return alerta;
    }

    public static List<Alerta> listarTodos() {
        List<Alerta> alertas = new ArrayList<>();
        String sql = "SELECT id_alerta, tipo_alerta, descricao, nivel, resolvido FROM alerta";

        try (Connection conexao = ConexaoSupabase.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Alerta alerta = new Alerta(
                    rs.getInt("id_alerta"),
                    rs.getString("tipo_alerta"),
                    rs.getString("descricao"),
                    rs.getString("nivel")
                );
                if (rs.getBoolean("resolvido")) {
                    alerta.resolverAlerta();
                }
                alertas.add(alerta);
            }

            System.out.println("✓ " + alertas.size() + " alertas listados");

        } catch (Exception e) {
            System.err.println("✗ Erro ao listar alertas: " + e.getMessage());
        }

        return alertas;
    }

    
    public static List<Alerta> listarAbertos() {
        List<Alerta> alertas = new ArrayList<>();
        String sql = "SELECT id_alerta, tipo_alerta, descricao, nivel FROM alerta WHERE resolvido = false";

        try (Connection conexao = ConexaoSupabase.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Alerta alerta = new Alerta(
                    rs.getInt("id_alerta"),
                    rs.getString("tipo_alerta"),
                    rs.getString("descricao"),
                    rs.getString("nivel")
                );
                alertas.add(alerta);
            }

            System.out.println("✓ " + alertas.size() + " alertas abertos encontrados");

        } catch (Exception e) {
            System.err.println("✗ Erro ao listar alertas abertos: " + e.getMessage());
        }

        return alertas;
    }

 
    public static List<Alerta> listarResolvidos() {
        List<Alerta> alertas = new ArrayList<>();
        String sql = "SELECT id_alerta, tipo_alerta, descricao, nivel FROM alerta WHERE resolvido = true";

        try (Connection conexao = ConexaoSupabase.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Alerta alerta = new Alerta(
                    rs.getInt("id_alerta"),
                    rs.getString("tipo_alerta"),
                    rs.getString("descricao"),
                    rs.getString("nivel")
                );
                alerta.resolverAlerta();
                alertas.add(alerta);
            }

            System.out.println("✓ " + alertas.size() + " alertas resolvidos encontrados");

        } catch (Exception e) {
            System.err.println("✗ Erro ao listar alertas resolvidos: " + e.getMessage());
        }

        return alertas;
    }

    
    public static List<Alerta> listarPorNivel(String nivel) {
        List<Alerta> alertas = new ArrayList<>();
        String sql = "SELECT id_alerta, tipo_alerta, descricao, nivel, resolvido FROM alerta WHERE nivel = ?";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, nivel);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Alerta alerta = new Alerta(
                    rs.getInt("id_alerta"),
                    rs.getString("tipo_alerta"),
                    rs.getString("descricao"),
                    rs.getString("nivel")
                );
                if (rs.getBoolean("resolvido")) {
                    alerta.resolverAlerta();
                }
                alertas.add(alerta);
            }

            System.out.println("✓ " + alertas.size() + " alertas com nível '" + nivel + "' encontrados");

        } catch (Exception e) {
            System.err.println("✗ Erro ao listar alertas por nível: " + e.getMessage());
        }

        return alertas;
    }

    public static List<Alerta> listarPorTipo(String tipo) {
        List<Alerta> alertas = new ArrayList<>();
        String sql = "SELECT id_alerta, tipo_alerta, descricao, nivel, resolvido FROM alerta WHERE tipo_alerta = ?";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, tipo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Alerta alerta = new Alerta(
                    rs.getInt("id_alerta"),
                    rs.getString("tipo_alerta"),
                    rs.getString("descricao"),
                    rs.getString("nivel")
                );
                if (rs.getBoolean("resolvido")) {
                    alerta.resolverAlerta();
                }
                alertas.add(alerta);
            }

            System.out.println("✓ " + alertas.size() + " alertas do tipo '" + tipo + "' encontrados");

        } catch (Exception e) {
            System.err.println("✗ Erro ao listar alertas por tipo: " + e.getMessage());
        }

        return alertas;
    }

  
    public static boolean inserir(Alerta alerta, int idVaca) {
        String sql = "INSERT INTO alerta (id_vaca, tipo_alerta, descricao, nivel, resolvido) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, idVaca);
            pstmt.setString(2, alerta.getTipoAlerta());
            pstmt.setString(3, "Descrição do alerta"); // A classe Alerta não tem getter para descrição
            pstmt.setString(4, alerta.getNivel());
            pstmt.setBoolean(5, alerta.isResolvido());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Alerta inserido com sucesso: " + alerta.getTipoAlerta());
                return true;
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao inserir alerta: " + e.getMessage());
        }

        return false;
    }

   
    public static boolean marcarComoResolvido(int id) {
        String sql = "UPDATE alerta SET resolvido = true WHERE id_alerta = ?";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Alerta marcado como resolvido (ID: " + id + ")");
                return true;
            } else {
                System.out.println("✗ Nenhum alerta foi atualizado (ID não encontrado)");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao marcar alerta como resolvido: " + e.getMessage());
        }

        return false;
    }

  
    public static boolean atualizarNivel(int id, String novoNivel) {
        String sql = "UPDATE alerta SET nivel = ? WHERE id_alerta = ?";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, novoNivel);
            pstmt.setInt(2, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Nível do alerta atualizado para: " + novoNivel);
                return true;
            } else {
                System.out.println("✗ Nenhum alerta foi atualizado (ID não encontrado)");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao atualizar nível do alerta: " + e.getMessage());
        }

        return false;
    }

    public static boolean deletar(int id) {
        String sql = "DELETE FROM alerta WHERE id_alerta = ?";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Alerta deletado com sucesso (ID: " + id + ")");
                return true;
            } else {
                System.out.println("✗ Nenhum alerta foi deletado (ID não encontrado)");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao deletar alerta: " + e.getMessage());
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println("=== TESTANDO ALERTADAO ===\n");

        // 1. Listar alertas abertos
        System.out.println("1. Listando alertas abertos:");
        List<Alerta> alertasAbertos = listarAbertos();
        alertasAbertos.forEach(System.out::println);

        // 2. Listar alertas por nível
        System.out.println("\n2. Listando alertas de nível 'alto':");
        List<Alerta> alertasAltos = listarPorNivel("alto");
        alertasAltos.forEach(System.out::println);

        // 3. Inserir novo alerta
        System.out.println("\n3. Inserindo novo alerta:");
        Alerta novoAlerta = new Alerta(0, "Saúde", "Temperatura elevada detectada", "alto");
        inserir(novoAlerta, 1); // ID da vaca = 1

        // 4. Buscar alerta por ID (se houver)
        if (!alertasAbertos.isEmpty()) {
            System.out.println("\n4. Buscando primeiro alerta:");
            Alerta alerta = buscarPorId(alertasAbertos.get(0).getId());
            if (alerta != null) {
                System.out.println(alerta);
                System.out.println("Cor do alerta: " + alerta.obterCor());
            }
        }

        // 5. Marcar alerta como resolvido
        if (!alertasAbertos.isEmpty()) {
            System.out.println("\n5. Marcando primeiro alerta como resolvido:");
            marcarComoResolvido(alertasAbertos.get(0).getId());
        }

        // 6. Listar alertas resolvidos
        System.out.println("\n6. Listando alertas resolvidos:");
        List<Alerta> alertasResolvidos = listarResolvidos();
        System.out.println("Total de alertas resolvidos: " + alertasResolvidos.size());

     

        System.out.println("\n=== FIM DOS TESTES ===");
    }
}