package model;

import model.enums.Tipo;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Pokemon {
    private Integer id;
    private String nome;
    private List<Tipo> tipos;
    private Integer vida;
    private Integer nivel;
    private List<Movimento> movimentos = new ArrayList<>();

    public Pokemon(Integer id, String nome, List<Tipo> tipos, Integer vida, Integer nivel, List<Movimento> movimentos) {
        this.id = id;
        this.nome = nome;
        this.tipos = tipos;
        this.vida = vida;
        this.nivel = nivel;
        this.movimentos = movimentos;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<Tipo> getTipos() {
        return tipos;
    }

    public Integer getVida() {
        return vida;
    }

    public void setVida(Integer vida) {
        this.vida = vida;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public List<Movimento> getMovimentos() {
        return movimentos;
    }

    @Override
    public String toString() {
        String tiposFormatados = tipos.stream()
                .map(t -> t.name())
                .collect(Collectors.joining(", "));

        return String.format("%s | Vida: %d| Tipos: %s", nome, vida, tiposFormatados);
    }
}
