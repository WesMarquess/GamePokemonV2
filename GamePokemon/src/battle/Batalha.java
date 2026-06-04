package battle;

import enums.Tipo;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import model.Jogador;
import model.Movimento;
import model.Pokemon;

public class Batalha {
    private Jogador jogador;
    private Pokemon pokemonAdversario;
    private Scanner scanner;
    private Random random;

    public Batalha(Jogador jogador, Pokemon pokemonAdversario) {
        if (jogador == null) {
            throw new IllegalArgumentException("O jogador não pode ser nulo para iniciar uma batalha.");
        }
        if (pokemonAdversario == null) {
            throw new IllegalArgumentException("O Pokémon adversário não pode ser nulo para iniciar uma batalha.");
        }
        this.jogador = jogador;
        this.pokemonAdversario = pokemonAdversario;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    // O Loop Principal
    public void iniciarBatalha() {
        Pokemon pokemonAliado = jogador.getPokemon();

        if (pokemonAliado == null) {
            System.out.println("Erro: Jogador não possui Pokémon para batalhar.");
            return;
        }

        System.out.println("Um " + pokemonAdversario.getNome() + " selvagem apareceu!");

        boolean batalhaAtiva = true;

        while (batalhaAtiva && !pokemonAliado.estaDesmaiado() && !pokemonAdversario.estaDesmaiado()) {
            System.out.println("\n--- TURNO ---");
            System.out.println(pokemonAliado.getNome() + " (HP: " + pokemonAliado.getVida() + ")");
            System.out.println(pokemonAdversario.getNome() + " (HP: " + pokemonAdversario.getVida() + ")");
            System.out.println("O que você deseja fazer?");
            System.out.println("1. Atacar");
            System.out.println("2. Usar Poção");
            System.out.println("3. Correr");
            System.out.print("Escolha: ");

            int escolha = -1;
            try {
                String entrada = scanner.nextLine().trim();

                if (entrada.matches("^[0-9]+$")) {
                    escolha = Integer.parseInt(entrada);
                } else {
                    System.out.println("Entrada inválida! Não use letras, espaços vazios, vírgulas ou aspas.");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("Erro ao ler a entrada.");
                continue;
            }

            switch (escolha) {
                case 1:
                    executarAtaques(pokemonAliado, pokemonAdversario);
                    break;
                case 2:
                    jogador.curarPokemon(pokemonAliado);
                    turnoAdversario(pokemonAdversario, pokemonAliado);
                    break;
                case 3:
                    if (tentarFugir()) {
                        System.out.println("Você fugiu com sucesso!");
                        batalhaAtiva = false;
                    } else {
                        System.out.println("Você não conseguiu fugir!");
                        turnoAdversario(pokemonAdversario, pokemonAliado);
                    }
                    break;
                default:
                    System.out.println("Opção inexistente! Escolha 1, 2 ou 3.");
                    break;
            }
        }
        encerrarBatalha(pokemonAliado, pokemonAdversario);
    }
    private void executarAtaques(Pokemon aliado, Pokemon adversario) {
        if (obterVelocidade(aliado) >= obterVelocidade(adversario)) {
            boolean morreu = menuAtaque(aliado, adversario);
            if (!morreu) {
                turnoAdversario(adversario, aliado);
            }
        } else {
            turnoAdversario(adversario, aliado);
            if (aliado.getVida() > 0) {
                menuAtaque(aliado, adversario);
            }
        }
    }
    private boolean menuAtaque(Pokemon atacante, Pokemon defensor) {
        List<Movimento> movimentos = obterMovimentosLimitados(atacante);
        if (movimentos.isEmpty()) {
            System.out.println("\nNenhum movimento disponível. O ataque falhou!");
            return false;
        }

        System.out.println("\nEscolha um movimento:");
        for (int i = 0; i < movimentos.size(); i++) {
            Movimento mov = movimentos.get(i);
            System.out.println((i + 1) + ". " + mov.getNome() + " (PP: " + mov.getPpAtual() + ")");
        }
        System.out.print("Escolha: ");

        int escolha = -1;
        try {
            String entrada = scanner.nextLine().trim();

            if (entrada.matches("^[0-9]+$")) {
                escolha = Integer.parseInt(entrada) - 1;
            } else {
                System.out.println("Entrada inválida! Não use letras, espaços vazios, vírgulas ou aspas.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler o movimento.");
        }

        if (escolha >= 0 && escolha < movimentos.size()) {
            Movimento movEscolhido = movimentos.get(escolha);
            realizarAtaque(atacante, defensor, movEscolhido);
        } else {
            System.out.println("Movimento inválido! Tropeçou e perdeu a vez.");
        }

        return defensor.getVida() <= 0;
    }

    private void turnoAdversario(Pokemon atacante, Pokemon defensor) {
        if (atacante.getVida() <= 0) return;

        System.out.println("\nTurno do adversário!");
        List<Movimento> movimentos = obterMovimentosLimitados(atacante);
        if (movimentos.isEmpty()) {
            System.out.println("O adversário não tem movimentos disponíveis.");
            return;
        }
        int indexAleatorio = random.nextInt(movimentos.size());
        Movimento movAdversario = movimentos.get(indexAleatorio);
        realizarAtaque(atacante, defensor, movAdversario);
    }
    private void realizarAtaque(Pokemon atacante, Pokemon defensor, Movimento movimento) {
        if (movimento == null) return;

        System.out.println(atacante.getNome() + " usou " + movimento.getNome() + "!");

        if (movimento.getPpAtual() <= 0) {
            System.out.println("Mas não tem PP suficiente!");
            return;
        }
        movimento.setPpAtual(movimento.getPpAtual() - 1);

        int chanceAcerto = random.nextInt(100) + 1;
        if (chanceAcerto > movimento.getPrecisao()) {
            System.out.println("O ataque errou!");
            return;
        }

        double multiplicador = BattleMultiplier.getMultiplier(movimento.getTipo(), obterTipo(defensor));

        if (multiplicador > 1.0) System.out.println("Foi super efetivo!");
        if (multiplicador < 1.0 && multiplicador > 0) System.out.println("Não foi muito efetivo...");
        if (multiplicador == 0) {
            System.out.println("Não teve efeito nenhum!");
            return;
        }

        int danoCalculado = (int) (((obterAtaque(atacante) * movimento.getDano()) / obterDefesa(defensor)) * multiplicador);
        int danoFinal = Math.max(1, danoCalculado);

        int novaVida = defensor.getVida() - danoFinal;
        defensor.setVida(Math.max(0, novaVida));

        System.out.println("Causou " + danoFinal + " de dano!");
    }

    private Tipo obterTipo(Pokemon pokemon) {
        if (pokemon != null && pokemon.getTipos() != null && !pokemon.getTipos().isEmpty()) {
            return pokemon.getTipos().get(0);
        }
        return Tipo.NORMAL;
    }

    private int obterAtaque(Pokemon pokemon) {
        if (pokemon == null) return 10;
        Integer nivel = pokemon.getNivel();
        return 10 + (nivel != null ? nivel : 1) * 2;
    }

    private int obterDefesa(Pokemon pokemon) {
        if (pokemon == null) return 10;
        Integer nivel = pokemon.getNivel();
        return 10 + (nivel != null ? nivel : 1);
    }

    private int obterVelocidade(Pokemon pokemon) {
        if (pokemon == null) return 10;
        Integer nivel = pokemon.getNivel();
        return 10 + (nivel != null ? nivel : 1);
    }

    private List<Movimento> obterMovimentosLimitados(Pokemon pokemon) {
        if (pokemon == null) return List.of();
        List<Movimento> movimentos = pokemon.getMovimentos();
        if (movimentos == null || movimentos.isEmpty()) {
            return List.of();
        }
        return movimentos.size() <= 4 ? movimentos : movimentos.subList(0, 4);
    }

    private boolean tentarFugir() {
        int chance = random.nextInt(100);
        return chance > 50;
    }

    private void encerrarBatalha(Pokemon aliado, Pokemon adversario) {
        System.out.println("\n--- FIM DE BATALHA ---");
        if (aliado != null && aliado.getVida() <= 0) {
            System.out.println("Seu Pokémon desmaiou. Você perdeu!");
        } else if (adversario != null && adversario.getVida() <= 0) {
            System.out.println("O " + adversario.getNome() + " selvagem desmaiou. Você venceu!");
        }
    }
}