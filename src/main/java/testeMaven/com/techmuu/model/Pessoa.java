package testeMaven.com.techmuu.model;

public abstract class Pessoa {
    private int id;
    private String nome;
    private String email;
    private String telefone;

    public Pessoa(int id, String nome, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }

    public abstract String obterPapel();

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome + ", Email: " + email + ", Papel: " + obterPapel();
    }
}