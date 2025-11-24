package testeMaven.com.techmuu.model;

public class Produtor extends Pessoa {

	    private String senhaHash;

	    public Produtor(int id, String nome, String email, String telefone, String senhaHash) {
	        super(id, nome, email, telefone);
	        this.senhaHash = senhaHash;
	    }

	    public String getSenhaHash() { return senhaHash; }

	    @Override
	    public String obterPapel() {
	        return "Produtor Rural";
	    }

	    public boolean autenticar(String email, String senha) {
	        return this.getEmail().equals(email) && this.senhaHash.equals(senha);
	    }
	}