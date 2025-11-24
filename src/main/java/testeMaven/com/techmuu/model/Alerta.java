package testeMaven.com.techmuu.model;

public class Alerta {
    private int id;
    private String tipoAlerta;
    private String descricao;
    private String nivel;
    private boolean resolvido;

    public Alerta(int id, String tipoAlerta, String descricao, String nivel) {
        this.id = id;
        this.tipoAlerta = tipoAlerta;
        this.descricao = descricao;
        this.nivel = nivel;
        this.resolvido = false;
    }

    public int getId() { return id; }
    public String getTipoAlerta() { return tipoAlerta; }
    public String getNivel() { return nivel; }
    public boolean isResolvido() { return resolvido; }

    public void resolverAlerta() {
        this.resolvido = true;
    }

    public String obterCor() {
        switch (nivel.toLowerCase()) {
            case "alto": return "#FF0000";
            case "médio": return "#FFA500";
            case "baixo": return "#FFFF00";
            default: return "#00FF00";
        }
    }

    @Override
    public String toString() {
        return id + " - " + tipoAlerta + " [" + nivel + "] - " + (resolvido ? "Resolvido" : "Aberto");
    }
}