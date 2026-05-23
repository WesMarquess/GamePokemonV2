package model.enums;

import java.util.ArrayList;
import java.util.List;

public enum Tipo {
    NORMAL,
    PLANTA,
    FOGO,
    AGUA,
    ELETRICO,
    VOADOR,
    PEDRA,
    PSIQUICO,
    FANTASMA,
    VENENOSO,
    INSETO,
    GELO;

    private List<Tipo> fraquezas = new ArrayList<>();
    private List<Tipo> vantagens = new ArrayList<>();
    private List<Tipo> imunidades = new ArrayList<>();

    public void adicionarFraqueza(Tipo tipo) {
        fraquezas.add(tipo);
    }

    public void adicionarVantagem(Tipo tipo) {
        vantagens.add(tipo);
    }

    public void adicionarImunidade(Tipo tipo) {
        imunidades.add(tipo);
    }

    public List<Tipo> getFraquezas() {
        return fraquezas;
    }

    public List<Tipo> getVantagens() {
        return vantagens;
    }

    public List<Tipo> getImunidades() {
        return imunidades;
    }
}