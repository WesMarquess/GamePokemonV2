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

        this.id = id;
        this.nome = nome;

        this.pokemons = new ArrayList<>();

        this.itens = new ArrayList<>();

        itens.add(new Item(TipoItem.POKEBOLA, 10));
        itens.add(new Item(TipoItem.POCAO, 5));
        itens.add(new Item(TipoItem.REVIVER, 2));
    }

    public void adicionarPokemon(Pokemon pokemon) {
        pokemons.add(pokemon);
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void mostrarItens() {

        System.out.println("\n=== ITENS ===");

        for (int i = 0; i < itens.size(); i++) {

            Item item = itens.get(i);

            System.out.println(
                    i + " - "
                            + item.getTipo()
                            + " x"
                            + item.getQuantidade()
            );
        }
    }

    private Item buscarItem(TipoItem tipo) {

        for (Item item : itens) {

            if (item.getTipo() == tipo) {
                return item;
            }
        }

        return null;
    }

    public boolean curarPokemon(Pokemon pokemon) {

        Item pocao = buscarItem(TipoItem.POCAO);

        if (pocao == null || !pocao.possuiQuantidade()) {

            System.out.println("Sem poções!");

            return false;
        }

        if (pokemon.getVida() <= 0) {

            System.out.println("Pokemon desmaiado!");

            return false;
        }

        pokemon.setVida(
                pokemon.getVida() + 20
        );

        pocao.diminuirQuantidade();

        System.out.println(
                pokemon.getNome()
                        + " recuperou 20 de vida!"
        );

        return true;
    }

    public boolean reviverPokemon(Pokemon pokemon) {

        Item reviver =
                buscarItem(TipoItem.REVIVER);

        if (reviver == null || !reviver.possuiQuantidade()) {

            System.out.println("Sem Reviver!");

            return false;
        }

        if (pokemon.getVida() > 0) {

            System.out.println("Pokemon já está vivo!");

            return false;
        }

        pokemon.setVida(pokemon.getVidaMaxima() / 2);

        reviver.diminuirQuantidade();

        System.out.println(pokemon.getNome() + " voltou à batalha!");

        return true;
    }

    public boolean usarPokebola() {

        Item pokebola =
                buscarItem(TipoItem.POKEBOLA);

        if (pokebola == null || !pokebola.possuiQuantidade()) {

            System.out.println("Sem Pokebolas!");

            return false;
        }

        pokebola.diminuirQuantidade();

        return true;
    }

    public boolean capturarPokemon(Pokemon pokemon) {

        if (!usarPokebola()) {
            return false;
        }

        double chance = (1 -((double) pokemon.getVida()/ pokemon.getVidaMaxima()))* 100;

        chance = Math.max(10, chance);
        chance = Math.min(90, chance);

        if (Math.random() * 100 <= chance) {adicionarPokemon(pokemon);
            System.out.println(pokemon.getNome()+ " foi capturado!");
            return true;
        }

        System.out.println(pokemon.getNome()+ " escapou!");

        return false;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

}