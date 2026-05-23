package model;

public class Jogador {
    private Integer id;
    private String nome;
    private Pokemon pokemon;
    private Integer pocao;

    public Jogador(Integer id, String nome, Pokemon pokemon, Integer pocao) {
        this.id = id;
        this.nome = nome;
        this.pokemon = pokemon;
        this.pocao = pocao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Integer getPocao() {
        return pocao;
    }

    public void setPocao(Integer pocao) {
        this.pocao = pocao;
    }

    public Integer curarPokemon(Pokemon pokemon) {
        if (pokemon.getVida() <= 0) {
            System.out.println("Pokemon Desmaiou");
            return pokemon.getVida();
        }
        pokemon.setVida(pokemon.getVida() + 50);
        if (pokemon.getVida() > 50) {
            pokemon.setVida(50);
        }
        pocao -= 1;
        return pocao;
    }
}
