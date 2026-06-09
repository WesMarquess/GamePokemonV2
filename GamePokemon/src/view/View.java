package view;

import battle.Batalha;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import model.Jogador;
import model.Pokemon;
import repository.JogadorRepository;
import repository.PokemonRepository;
import service.JogadorService;

public class View {
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

        boolean continuarJogando = true;
        while (continuarJogando) {
            List<Pokemon> novasOpcoes = pokemonRepository.buscarAleatorios();
            Pokemon adversario = escolherAdversario(escolhido, novasOpcoes);
            boolean venceu = iniciarBatalha(adversario, input);

            if (!venceu) {
                System.out.println("Fim de jogo!");
                break;
            }

            boolean respostaValida = false;
            while (!respostaValida) {
                System.out.println("\nDeseja continuar jogando? (1 - Sim / 2 - Não)");
                String resposta = input.nextLine().trim();
                if (resposta.equals("1")) {
                    respostaValida = true;

                    boolean salvarValido = false;
                    while (!salvarValido) {
                        System.out.println("Deseja salvar o progresso? (1 - Sim / 2 - Não)");
                        String salvar = input.nextLine().trim();
                        if (salvar.equals("1")) {
                            salvarProgresso();
                            salvarValido = true;
                        } else if (salvar.equals("2")) {
                            salvarValido = true;
                        } else {
                            System.out.println("Opcao invalida! Digite 1 para Sim ou 2 para Nao.");
                        }
                    }
                } else if (resposta.equals("2")) {
                    continuarJogando = false;
                    respostaValida = true;
                } else {
                    System.out.println("Opcao invalida! Digite 1 para Sim ou 2 para Nao.");
                }
            }
        }
    }

    private void carregarJogo(Scanner input) throws Exception {
        System.out.print("Digite seu ID de jogador: ");
        try {
            int id = Integer.parseInt(input.nextLine().trim());
            JogadorRepository jogadorRepository = new JogadorRepository();
            jogador = jogadorRepository.buscarPorId(id);

            if (jogador == null) {
                System.out.println("Jogador nao encontrado.");
                return;
            }

            jogadorService = new JogadorService(jogador);
            System.out.println("Bem vindo de volta, " + jogador.getNome() + "!");
            System.out.println("Pokemon: " + jogador.getPokemon().getNome() +
                    " | HP: " + jogador.getPokemon().getVida() +
                    "/" + jogador.getPokemon().getVidaMaxima());

            menuJogoContinuado(input);

        } catch (NumberFormatException e) {
            System.out.println("Digite um numero valido.");
        } catch (Exception e) {
            System.out.println("Erro ao carregar jogo: " + e.getMessage());
        }
    }

    private void menuJogoContinuado(Scanner input) throws SQLException {
        String menu = """
                +--------------------------------+
                |         O QUE DESEJA?          |
                +--------------------------------+
                |  1 - Batalhar                  |
                |  2 - Salvar progresso          |
                |  3 - Voltar ao menu principal  |
                +--------------------------------+
                """;

        int opcao = 0;
        while (opcao != 3) {
            System.out.println(menu);
            System.out.print("Escolha: ");
            try {
                opcao = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero valido.");
                continue;
            }

            switch (opcao) {
                case 1 -> {
                    List<Pokemon> opcoes = pokemonRepository.buscarAleatorios();
                    Pokemon adversario = escolherAdversario(jogador.getPokemon(), opcoes);
                    boolean venceu = iniciarBatalha(adversario, input);
                    if (!venceu) {
                        System.out.println("Fim de jogo!");
                        return;
                    }
                    System.out.println("Deseja salvar o progresso? (1 - Sim / 2 - Não)");
                    if (input.nextLine().trim().equals("1")) salvarProgresso();
                }
                case 2 -> salvarProgresso();
                case 3 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("Opcao invalida.");
            }
        }
    }

    public Jogador cadastrarJogador(Scanner input, Pokemon pokemonEscolhido) {
        String nomeJogador;

        while (true) {
            System.out.println("Insira seu nome de jogador:");
            nomeJogador = input.nextLine().trim();

            if (nomeJogador.matches("^(?=.{3,}$)[A-Za-zÀ-ÿ]+(?: [A-Za-zÀ-ÿ]+)*$")) {
                break;
            }
            System.out.println("Nome inválido! Digite pelo menos 3 letras.");
        }

        Jogador novoJogador = new Jogador(null, nomeJogador);

        if (pokemonEscolhido != null) {
            novoJogador.adicionarPokemon(pokemonEscolhido);
        }

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

    private Pokemon escolherAdversario(Pokemon escolhido, List<Pokemon> opcoes) {
        for (Pokemon pokemon : opcoes) {
            if (!pokemon.equals(escolhido)) {
                return pokemon;
            }
        }
        return escolhido;
    }

    private boolean iniciarBatalha(Pokemon adversario, Scanner input) {
        adversario.setVida(adversario.getVidaMaxima());
        System.out.println("Adversario: " + adversario.getNome());
        Batalha batalha = new Batalha(jogador, adversario, input);
        return batalha.iniciarBatalha();
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
        sb.append("\nParabens! Voce foi escolhido para ser um Mestre Pokemon!");
        sb.append("\nPokemons sao criaturas fofas mas que podem ser muito poderosas.");
        sb.append("\nAgora chegou sua hora. Escolha seu primeiro monstrinho!");
        System.out.println(sb);
        return sb;
    }

    public StringBuilder creditos() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nDesenvolvedores:");
        sb.append("\nDaniel Alves de Souza");
        sb.append("\nPablo Eduardo de Sousa Fernandes");
        sb.append("\nPedro Henrique de Paula");
        sb.append("\nWeslley Lima Marques da Silva");
        sb.append("\nSe divirta ao jogar!");
        System.out.println(sb);
        return sb;
    }
}