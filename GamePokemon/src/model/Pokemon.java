package model;

import enums.Tipo;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Pokemon {

    private Integer id;
    private String nome;
    private List<Tipo> tipos;

    // vida atual
    private Integer vida;

    // vida máxima
    private Integer vidaMaxima;

    private Integer nivel;

    private List<Movimento> movimentos = new ArrayList<>();

    public Pokemon(
            Integer id,
            String nome,
            List<Tipo> tipos,
            Integer vida,
            Integer nivel,
            List<Movimento> movimentos
    ) {

        this.id = id;
        this.nome = nome;
        this.tipos = tipos;

        this.vida = vida;

        // ao criar o pokemon, a vida máxima será a vida inicial
        this.vidaMaxima = vida;

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

        if (vida < 0) {
            this.vida = 0;
        } else if (vida > vidaMaxima) {
            this.vida = vidaMaxima;
        } else {
            this.vida = vida;
        }
    }

    public Integer getVidaMaxima() {
        return vidaMaxima;
    }

    public void setVidaMaxima(Integer vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
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

    public boolean estaDesmaiado() {
        return vida <= 0;
    }

    @Override
    public String toString() {

        String tiposFormatados = tipos.stream()
                .map(Tipo::name)
                .collect(Collectors.joining(", "));

        return String.format(
                "%s | Vida: %d/%d | Nível: %d | Tipos: %s",
                nome,
                vida,
                vidaMaxima,
                nivel,
                tiposFormatados
        );
    }
}