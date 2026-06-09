package model;

import enums.Tipo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Pokemon {

    private Integer id;
    private String nome;
    private List<Tipo> tipos;
    private Integer vida;
    private Integer vidaMaxima;
    private Integer nivel;
    private Integer xp = 0;
    private List<Movimento> movimentos;

    public Pokemon(
            Integer id,
            String nome,
            List<Tipo> tipos,
            Integer vida,
            Integer nivel,
            List<Movimento> movimentos
    ) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID do Pokémon deve ser maior que zero.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do Pokémon não pode ser vazio ou nulo.");
        }
        if (tipos == null || tipos.isEmpty()) {
            throw new IllegalArgumentException("O Pokémon deve ter pelo menos um tipo.");
        }
        if (tipos.contains(null)) {
            throw new IllegalArgumentException("A lista de tipos não pode conter elementos nulos.");
        }
        if (vida == null || vida <= 0) {
            throw new IllegalArgumentException("A vida inicial do Pokémon deve ser maior que zero.");
        }
        if (nivel == null || nivel < 1 || nivel > 100) {
            throw new IllegalArgumentException("O nível do Pokémon deve estar entre 1 e 100.");
        }

        this.id = id;
        this.nome = nome;
        this.tipos = new ArrayList<>(tipos);
        this.vidaMaxima = vida;
        this.vida = vida;
        this.vidaMaxima = vida;

        this.nivel = nivel;

        this.movimentos = new ArrayList<>();
        if (movimentos != null) {
            for (Movimento mov : movimentos) {
                if (mov != null && this.movimentos.size() < 4) {
                    this.movimentos.add(mov);
                }
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<Tipo> getTipos() {
        return new ArrayList<>(tipos);
    }

    public Integer getVida() {
        return vida;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    public void setVida(Integer vida) {
        if (vida == null) {
            return;
        }

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
        if (vidaMaxima == null || vidaMaxima <= 0) {
            throw new IllegalArgumentException("A vida máxima deve ser maior que zero.");
        }
        this.vidaMaxima = vidaMaxima;
        if (this.vida > this.vidaMaxima) {
            this.vida = this.vidaMaxima;
        }
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        if (nivel == null || nivel < 1 || nivel > 100) {
            throw new IllegalArgumentException("Nível inválido! O nível deve ser de 1 a 100.");
        }
        this.nivel = nivel;
    }

    public List<Movimento> getMovimentos() {
        // Correção: Retorna uma cópia para blindar a lista interna contra alterações externas diretas
        return new ArrayList<>(movimentos);
    }

    public void adicionarMovimento(Movimento movimento) {
        if (movimento == null) {
            throw new IllegalArgumentException("Não é possível adicionar um movimento nulo.");
        }
        if (movimentos.size() >= 4) {
            System.out.println(nome + " já possui 4 movimentos! Esqueça um para aprender outro.");
            return;
        }
        movimentos.add(movimento);
    }

    public boolean estaDesmaiado() {
        return vida <= 0;
    }

    public void ganharXp(int quantidade) {
        this.xp += quantidade;
        System.out.println(nome + " ganhou " + quantidade + " XP!");

        while (xp >= nivel * 100) {
            xp -= nivel * 100;
            subirNivel();
        }
    }

    private void subirNivel() {
        this.nivel++;
        System.out.println(nome + " subiu para o nível " + nivel + "!");
    }

    @Override
    public String toString() {
        if (tipos == null) return nome + " | Sem tipos";

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