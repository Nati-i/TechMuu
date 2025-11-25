package testeMaven.com.techmuu.model;

public class Animal {
    private int id;
    private String identificador;
    private String raca;
    private String estadoReprodutivo;
    private double ultimaTemperatura;

    public Animal(int id, String identificador, String raca, String estadoReprodutivo) {
        this.id = id;
        this.identificador = identificador;
        this.raca = raca;
        this.estadoReprodutivo = estadoReprodutivo;
        this.ultimaTemperatura = 38.5;
    }

    public int getId() { return id; }
    public String getIdentificador() { return identificador; }
    public String getRaca() { return raca; }
    public String getEstadoReprodutivo() { return estadoReprodutivo; }
    public double getUltimaTemperatura() { return ultimaTemperatura; }

    public boolean atualizarTemperatura(double temperatura) {
        if (temperatura > 37 && temperatura < 40) {
            this.ultimaTemperatura = temperatura;
            return true;
        }
        return false;
    }

    public boolean temFebre() {
        return ultimaTemperatura > 39;
    }

    @Override
    public String toString() {
        return "Animal [" + identificador + ", Raça: " + raca + ", Temp: " + ultimaTemperatura + "°C]";
    }

	public void setEstadoReprodutivo(String string) {
		
	}
}