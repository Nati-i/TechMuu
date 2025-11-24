

	import testeMaven.com.techmuu.dao.VacaDAO;
	import testeMaven.com.techmuu.database.ConexaoSupabase;
	import testeMaven.com.techmuu.model.*;
	import java.util.List;

	public class TesteIntegracao {
	    public static void main(String[] args) {
	        System.out.println("========================================");
	        System.out.println("  TESTE DE INTEGRAÇÃO TECHMUU");
	        System.out.println("========================================\n");
	        
	        // Teste 1: Conexão
	        System.out.println("[1] TESTANDO CONEXÃO...");
	        ConexaoSupabase.testarConexao();
	        
	        // Teste 2: Vacas
	        System.out.println("\n[2] OBTENDO VACAS...");
	        List<String> vacas = VacaDAO.obterTodasVacas();
	        for (String vaca : vacas) {
	            System.out.println("  ✓ " + vaca);
	        }
	        
	        // Teste 3: Alertas
	        System.out.println("\n[3] OBTENDO ALERTAS...");
	        List<String> alertas = VacaDAO.obterAlertasAbertos();
	        for (String alerta : alertas) {
	            System.out.println("  ⚠️  " + alerta);
	        }
	        
	        // Teste 4: Classes de modelo
	        System.out.println("\n[4] TESTANDO CLASSES DE MODELO...");
	        Produtor produtor = new Produtor(1, "João", "joao@email.com", "111111", "senha123");
	        System.out.println("  ✓ " + produtor.toString());
	        
	        Animal vaca = new Animal(1, "BR-123", "Holandesa", "lactante");
	        System.out.println("  ✓ " + vaca.toString());
	        
	        Alerta alerta = new Alerta(1, "Temperatura Alta", "Animal com febre", "alto");
	        System.out.println("  ✓ " + alerta.toString());
	        
	        System.out.println("\n========================================");
	        System.out.println("  ✓ TESTE CONCLUÍDO!");
	        System.out.println("========================================");
	    }
	}