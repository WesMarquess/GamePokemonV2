package model;

import enums.TipoItem;

import java.util.ArrayList;
import java.util.List;

public class Jogador {

    private Integer id;
    private String nome;
    private List<Pokemon> pokemons;
    private List<Item> itens;

    public Jogador(Integer id, String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do jogador não pode ser vazio ou nulo.");
        }

        this.id = id;
        this.nome = nome;
        this.pokemons = new ArrayList<>();
        this.itens = new ArrayList<>();

        itens.add(new Item(TipoItem.POKEBOLA, 10));
        itens.add(new Item(TipoItem.POCAO, 5));
        itens.add(new Item(TipoItem.REVIVER, 2));
    }

    public void adicionarPokemon(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Não é possível adicionar um Pokémon nulo.");
        }
        pokemons.add(pokemon);
    }

    public boolean curarPokemon(Pokemon pokemon) {
        if (pokemon == null) {
            System.out.println("Erro: Nenhum Pokémon foi selecionado para cura.");
            return false;
        }

        Item pocao = buscarItem(TipoItem.POCAO);

        if (pocao == null || !pocao.possuiQuantidade()) {
            System.out.println("Sem poções disponíveis!");
            return false;
        }

        if (pokemon.getVida() <= 0) {
            System.out.println(pokemon.getNome() + " está desmaiado e não pode ser curado com Poção!");
            return false;
        }
        if (pokemon.getVida().equals(pokemon.getVidaMaxima())) {
            System.out.println(pokemon.getNome() + " já está com a vida máxima!");
            return false;
        }

        pokemon.setVida(pokemon.getVida() + 20);
        pocao.diminuirQuantidade();

        System.out.println(pokemon.getNome() + " recuperou de vida! Vida atual: " + pokemon.getVida() + "/" + pokemon.getVidaMaxima());
        return true;
    }

    public boolean reviverPokemon(Pokemon pokemon) {
        if (pokemon == null) {
            System.out.println("Erro: Nenhum Pokémon foi selecionado para reviver.");
            return false;
        }

        Item reviver = buscarItem(TipoItem.REVIVER);

        if (reviver == null || !reviver.possuiQuantidade()) {
            System.out.println("Sem Reviver no inventário!");
            return false;
        }

        if (pokemon.getVida() > 0) {
            System.out.println(pokemon.getNome() + " já está acordado e pronto para lutar!");
            return false;
        }

        pokemon.setVida(pokemon.getVidaMaxima() / 2);
        reviver.diminuirQuantidade();

        System.out.println(pokemon.getNome() + " voltou à batalha com metade da vida máxima!");
        return true;
    }

    public boolean capturarPokemon(Pokemon pokemon) {
        if (pokemon == null) {
            System.out.println("Erro: Não há nenhum Pokémon para capturar.");
            return false;
        }
        if (pokemon.getVida() <= 0) {
            System.out.println("O " + pokemon.getNome() + " selvagem desmaiou! Não é possível capturá-lo.");
            return false;
        }
        if (!usarPokebola()) {
            return false;
        }

        double chance = (1 - ((double) pokemon.getVida() / pokemon.getVidaMaxima())) * 100;
        chance = Math.max(10, chance);
        chance = Math.min(90, chance);

        if (Math.random() * 100 <= chance) {
            adicionarPokemon(pokemon);
            System.out.println(pokemon.getNome() + " foi capturado com sucesso!");
            return true;
        }

        System.out.println(pokemon.getNome() + " escapou da Pokébola!");
        return false;
    }

    public boolean usarPokebola() {
        Item pokebola = buscarItem(TipoItem.POKEBOLA);

        if (pokebola == null || !pokebola.possuiQuantidade()) {
            System.out.println("Você não tem Pokébolas!");
            return false;
        }

        pokebola.diminuirQuantidade();
        return true;
    }

    private Item buscarItem(TipoItem tipo) {
        if (tipo == null) return null;

        for (Item item : itens) {
            if (item.getTipo() == tipo) {
                return item;
            }
        }
        return null;
    }

    public void setQuantidadeItem(TipoItem tipo, int quantidade) {
        Item item = buscarItem(tipo);
        if (item != null) {
            item.setQuantidade(quantidade);
        }
    }

    public void mostrarItens() {
        System.out.println("\n=== ITENS ===");
        for (int i = 0; i < itens.size(); i++) {
            Item item = itens.get(i);
            System.out.println(i + " - " + item.getTipo() + " x" + item.getQuantidade());
        }
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

    public List<Pokemon> getPokemons() {
        return new ArrayList<>(pokemons);
    }

    public List<Item> getItens() {
        return itens;
    }

    public Pokemon getPokemon() {
        if (pokemons.isEmpty()) {
            return null;
        }
        return pokemons.get(0);
    }
}