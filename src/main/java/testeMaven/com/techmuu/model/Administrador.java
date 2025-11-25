package testeMaven.com.techmuu.model;

public class Administrador extends Pessoa {

	    private String nivelAcesso;

	    public Administrador(int id, String nome, String email, String telefone, String nivelAcesso) {
	        super(id, nome, email, telefone);
	        this.nivelAcesso = nivelAcesso;
	    }

	    public String getNivelAcesso() { return nivelAcesso; }

	    @Override
	    public String obterPapel() {
	        return "Administrador (" + nivelAcesso + ")";
	    }

	    public boolean temPermissao(String acao) {
	        return nivelAcesso.equals("alto");
	    }
	}