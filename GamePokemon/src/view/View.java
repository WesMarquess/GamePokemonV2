package view;

import model.Jogador;
import model.Pokemon;
import repository.JogadorRepository;
import repository.PokemonRepository;
import service.JogadorService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class View {

    public static final String quebraLinha = "\n";
    public static Jogador jogador;
    private PokemonRepository pokemonRepository;
    private JogadorService jogadorService;

    public View() throws SQLException {
        this.pokemonRepository = new PokemonRepository();
    }

    public void menu(Scanner input) throws Exception {
        String menu = """
                +--------------------------------+
                |         POKEMON GAME           |
                +--------------------------------+
                |  1 - Iniciar novo jogo         |
                |  2 - Carregar jogo             |
                |  3 - Creditos                  |
                |  4 - Sair                      |
                +--------------------------------+
                """;

        int opcao = 0;
        while (opcao < 1 || opcao > 4) {
            System.out.println(menu);
            System.out.print("Escolha: ");
            try {
                opcao = Integer.parseInt(input.nextLine().trim());
                if (opcao < 1 || opcao > 4) {
                    System.out.println("Opcao invalida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero valido.");
            }
        }

        switch (opcao) {
            case 1 -> iniciarNovoJogo(input);
            case 2 -> carregarJogo(input);
            case 3 -> creditos();
            case 4 -> System.exit(0);
        }
    }

    private void iniciarNovoJogo(Scanner input) throws SQLException {
        historia();
        List<Pokemon> opcoes = pokemonRepository.buscarAleatorios();
        Pokemon escolhido = exibirEscolhaPokemon(input, opcoes);
        jogador = cadastrarJogador(input, escolhido);
    }

    private void carregarJogo(Scanner input) {
        System.out.print("Digite seu ID de jogador: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());
            JogadorRepository jogadorRepository = new JogadorRepository();
            jogador = jogadorRepository.buscarPorId(id);
            if (jogador == null) {
                System.out.println("Jogador nao encontrado.");
            } else {
                jogadorService = new JogadorService(jogador);
                System.out.println("Bem vindo de volta, " + jogador.getNome() + "!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar jogo: " + e.getMessage());
        }
    }

    public Jogador cadastrarJogador(Scanner input, Pokemon pokemonEscolhido) {
        String nomeJogador;

        while (true) {
            System.out.println(quebraLinha);
            System.out.println("Insira seu nome de jogador:");
            nomeJogador = input.nextLine().trim();

            if (nomeJogador.matches("^(?=.{3,}$)[A-Za-zÀ-ÿ]+(?: [A-Za-zÀ-ÿ]+)*$")) {
                break;
            }
            System.out.println("Nome inválido! Digite pelo menos 3 letras.");
        }

        Jogador novoJogador = new Jogador(null, nomeJogador, pokemonEscolhido, 5);
        jogadorService = new JogadorService(novoJogador);
        jogadorService.cadastrar(novoJogador);
        return novoJogador;
    }

    public Pokemon exibirEscolhaPokemon(Scanner input, List<Pokemon> opcoes) {
        System.out.println("+==================================+");
        System.out.println("|       ESCOLHA SEU POKEMON        |");

        for (int i = 0; i < opcoes.size(); i++) {
            Pokemon pokemon = opcoes.get(i);
            String tipos = pokemon.getTipos().stream()
                    .map(t -> t.name())
                    .collect(Collectors.joining(", "));

            System.out.println("+----------------------------------+");
            System.out.printf("| %d. %s|%n", i + 1, pokemon.getNome());
            System.out.printf("|Tipo: %s|%n", tipos);
            System.out.printf("|Vida: %d|%n", pokemon.getVida());
        }

        System.out.println("+==================================+");

        int escolha = 0;
        while (escolha < 1 || escolha > opcoes.size()) {
            System.out.print("Escolha (1-" + opcoes.size() + "): ");
            try {
                escolha = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero valido.");
            }
        }
        return opcoes.get(escolha - 1);
    }

    public void salvarProgresso() {
        if (jogadorService == null) {
            System.out.println("Nenhum jogador ativo para salvar.");
            return;
        }
        jogadorService.salvarProgresso();
    }

    public StringBuilder historia() {
        StringBuilder sb = new StringBuilder();
        sb.append(quebraLinha);
        sb.append("Parabens! Voce foi escolhido para ser um Mestre Pokemon!");
        sb.append(quebraLinha);
        sb.append("Pokemons sao criaturas fofas mas que podem ser muito poderosas.");
        sb.append(quebraLinha);
        sb.append("Agora chegou sua hora. Escolha seu primeiro monstrinho!");
        sb.append(quebraLinha);
        System.out.println(sb);
        return sb;
    }

    public StringBuilder creditos() {
        StringBuilder sb = new StringBuilder();
        sb.append(quebraLinha);
        sb.append("Desenvolvedores:");
        sb.append(quebraLinha);
        sb.append("  Daniel Alves de Souza");
        sb.append(quebraLinha);
        sb.append("  Pablo Eduardo de Sousa Fernandes");
        sb.append(quebraLinha);
        sb.append("  Pedro Henrique de Paula");
        sb.append(quebraLinha);
        sb.append("  Weslley Lima Marques da Silva");
        sb.append(quebraLinha);
        sb.append("Se divirta ao jogar!");
        System.out.println(sb);
        return sb;
    }
}