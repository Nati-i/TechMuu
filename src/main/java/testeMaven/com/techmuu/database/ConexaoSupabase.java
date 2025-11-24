

package testeMaven.com.techmuu.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexaoSupabase {
  
	private static final String URL = "jdbc:postgresql://aws-1-us-east-2.pooler.supabase.com:5432/postgres";
	private static final String USER = "postgres.hrlalscrodzrtoqqvlcc"; 
	private static final String PASSWORD = "linhaperola123.";
    
    public static Connection conectar() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("✓ Driver PostgreSQL carregado");
            
            Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✓ Conectado ao Supabase!");
            return conexao;
            
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Driver não encontrado");
            throw new SQLException("Driver PostgreSQL não está no classpath");
        }
    }
    
  
    public static void testarConexao() {
        try {
            Connection conexao = conectar();
            if (conexao != null) {
                System.out.println("✓ Teste bem-sucedido!");
                conexao.close();
            }
        } catch (SQLException e) {
            System.err.println("✗ Erro: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        testarConexao();
    }
}
