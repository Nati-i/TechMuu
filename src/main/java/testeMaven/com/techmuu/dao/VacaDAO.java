package testeMaven.com.techmuu.dao;

import testeMaven.com.techmuu.model.Animal;
import testeMaven.com.techmuu.database.ConexaoSupabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacaDAO {

    
    public static List<String> obterTodasVacas() {
        List<String> vacas = new ArrayList<>();
        String sql = "SELECT id_vaca, identificador, raca FROM vaca LIMIT 20";

        try (Connection conexao = ConexaoSupabase.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_vaca");
                String identificador = rs.getString("identificador");
                String raca = rs.getString("raca");
                vacas.add(id + " - " + identificador + " (" + raca + ")");
            }

            System.out.println("✓ " + vacas.size() + " vacas obtidas");

        } catch (Exception e) {
            System.err.println("✗ Erro ao obter vacas: " + e.getMessage());
        }

        return vacas;
    }

    
    public static Animal buscarPorId(int id) {
        String sql = "SELECT id_vaca, identificador, raca, estado_reprodutivo FROM vaca WHERE id_vaca = ?";
        Animal animal = null;

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                animal = new Animal(
                    rs.getInt("id_vaca"),
                    rs.getString("identificador"),
                    rs.getString("raca"),
                    rs.getString("estado_reprodutivo")
                );
                System.out.println("✓ Vaca encontrada: " + animal.getIdentificador());
            } else {
                System.out.println("✗ Vaca com ID " + id + " não encontrada");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao buscar vaca: " + e.getMessage());
        }

        return animal;
    }

    
    public static Animal buscarPorIdentificador(String identificador) {
        String sql = "SELECT id_vaca, identificador, raca, estado_reprodutivo FROM vaca WHERE identificador = ?";
        Animal animal = null;

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, identificador);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                animal = new Animal(
                    rs.getInt("id_vaca"),
                    rs.getString("identificador"),
                    rs.getString("raca"),
                    rs.getString("estado_reprodutivo")
                );
                System.out.println("✓ Vaca encontrada: " + animal.getIdentificador());
            } else {
                System.out.println("✗ Vaca com identificador " + identificador + " não encontrada");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao buscar vaca: " + e.getMessage());
        }

        return animal;
    }

   
    public static List<Animal> listarTodosAnimais() {
        List<Animal> animais = new ArrayList<>();
        String sql = "SELECT id_vaca, identificador, raca, estado_reprodutivo FROM vaca";

        try (Connection conexao = ConexaoSupabase.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Animal animal = new Animal(
                    rs.getInt("id_vaca"),
                    rs.getString("identificador"),
                    rs.getString("raca"),
                    rs.getString("estado_reprodutivo")
                );
                animais.add(animal);
            }

            System.out.println("✓ " + animais.size() + " animais listados");

        } catch (Exception e) {
            System.err.println("✗ Erro ao listar animais: " + e.getMessage());
        }

        return animais;
    }

  
    public static boolean inserir(Animal animal, int idProdutor) {
        String sql = "INSERT INTO vaca (id_produtor, identificador, raca, estado_reprodutivo) VALUES (?, ?, ?, ?)";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, idProdutor);
            pstmt.setString(2, animal.getIdentificador());
            pstmt.setString(3, animal.getRaca());
            pstmt.setString(4, animal.getEstadoReprodutivo());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Vaca inserida com sucesso: " + animal.getIdentificador());
                return true;
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao inserir vaca: " + e.getMessage());
        }

        return false;
    }

   
    public static boolean atualizar(Animal animal) {
        String sql = "UPDATE vaca SET identificador = ?, raca = ?, estado_reprodutivo = ? WHERE id_vaca = ?";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, animal.getIdentificador());
            pstmt.setString(2, animal.getRaca());
            pstmt.setString(3, animal.getEstadoReprodutivo());
            pstmt.setInt(4, animal.getId());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Vaca atualizada com sucesso: " + animal.getIdentificador());
                return true;
            } else {
                System.out.println("✗ Nenhuma vaca foi atualizada (ID não encontrado)");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao atualizar vaca: " + e.getMessage());
        }

        return false;
    }

    
    public static boolean deletar(int id) {
        String sql = "DELETE FROM vaca WHERE id_vaca = ?";

        try (Connection conexao = ConexaoSupabase.conectar();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("✓ Vaca deletada com sucesso (ID: " + id + ")");
                return true;
            } else {
                System.out.println("✗ Nenhuma vaca foi deletada (ID não encontrado)");
            }

        } catch (Exception e) {
            System.err.println("✗ Erro ao deletar vaca: " + e.getMessage());
        }

        return false;
    }

    
    public static List<String> obterAlertasAbertos() {
        List<String> alertas = new ArrayList<>();
        String sql = "SELECT id_alerta, tipo_alerta, nivel FROM alerta WHERE resolvido = false";

        try (Connection conexao = ConexaoSupabase.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_alerta");
                String tipo = rs.getString("tipo_alerta");
                String nivel = rs.getString("nivel");
                alertas.add(id + " - " + tipo + " [" + nivel + "]");
            }

            System.out.println("✓ " + alertas.size() + " alertas obtidos");

        } catch (Exception e) {
            System.err.println("✗ Erro ao obter alertas: " + e.getMessage());
        }

        return alertas;
    }

    
    public static void main(String[] args) {
        System.out.println("=== TESTANDO VACADAO ===\n");

        // 1. Listar todas as vacas
        System.out.println("1. Listando vacas:");
        List<String> vacas = obterTodasVacas();
        vacas.forEach(System.out::println);

        // 2. Inserir uma nova vaca
        System.out.println("\n2. Inserindo nova vaca:");
        Animal novaVaca = new Animal(0, "VACA-TESTE-001", "Holandesa", "Gestante");
        inserir(novaVaca, 1);

        // 3. Buscar por identificador
        System.out.println("\n3. Buscando vaca por identificador:");
        Animal vacaEncontrada = buscarPorIdentificador("VACA-TESTE-001");
        if (vacaEncontrada != null) {
            System.out.println(vacaEncontrada);
        }

        // 4. Atualizar vaca
        System.out.println("\n4. Atualizando estado reprodutivo:");
        if (vacaEncontrada != null) {
            vacaEncontrada.setEstadoReprodutivo("Lactante");
            atualizar(vacaEncontrada);
        }

        // 5. Listar todos os animais como objetos
        System.out.println("\n5. Listando todos os animais:");
        List<Animal> todosAnimais = listarTodosAnimais();
        System.out.println("Total: " + todosAnimais.size() + " animais");

      
        System.out.println("\n=== FIM DOS TESTES ===");
    }
}
