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

    // Construtor
    public Batalha(Jogador jogador, Pokemon pokemonAdversario) {
        this.jogador = jogador;
        this.pokemonAdversario = pokemonAdversario;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    // O Loop Principal
    public void iniciarBatalha() {
        Pokemon pokemonAliado = jogador.getPokemon(); // Pega o Pokémon do jogador
        System.out.println("Um " + pokemonAdversario.getNome() + " selvagem apareceu!");

        boolean batalhaAtiva = true;

        while (batalhaAtiva && pokemonAliado.getVida() > 0 && pokemonAdversario.getVida() > 0) {
            System.out.println("\n--- TURNO ---");
            System.out.println(pokemonAliado.getNome() + " (HP: " + pokemonAliado.getVida() + ")");
            System.out.println(pokemonAdversario.getNome() + " (HP: " + pokemonAdversario.getVida() + ")");
            System.out.println("O que você deseja fazer?");
            System.out.println("1. Atacar");
            System.out.println("2. Usar Poção");
            System.out.println("3. Correr");
            System.out.print("Escolha: ");

            int escolha = scanner.nextInt();

            switch (escolha) {
                case 1:
                    executarAtaques(pokemonAliado, pokemonAdversario);
                    break;
                case 2:
                    System.out.println("Você usou uma poção!");
                    jogador.curarPokemon(pokemonAliado);
                    turnoAdversario(pokemonAdversario, pokemonAliado);
                    break;
                case 3:
                    if (tentarFugir()) {
                        System.out.println("Você fugiu com sucesso!");
                        batalhaAtiva = false; // Quebra o laço
                    } else {
                        System.out.println("Você não conseguiu fugir!");
                        turnoAdversario(pokemonAdversario, pokemonAliado);
                    }
                    break;
                default:
                    System.out.println("Escolha inválida! Você perdeu a vez.");
                    turnoAdversario(pokemonAdversario, pokemonAliado);
                    break;
            }
        }

        encerrarBatalha(pokemonAliado, pokemonAdversario);
    }

    // Controle de Velocidade no Turno
    private void executarAtaques(Pokemon aliado, Pokemon adversario) {
        // Se o aliado for mais rápido, ataca primeiro
        if (obterVelocidade(aliado) >= obterVelocidade(adversario)) {
            boolean morreu = menuAtaque(aliado, adversario);
            if (!morreu) {
                turnoAdversario(adversario, aliado);
            }
        } else {
            // Adversário é mais rápido
            turnoAdversario(adversario, aliado);
            if (aliado.getVida() > 0) {
                menuAtaque(aliado, adversario);
            }
        }
    }

    // Menu de Movimentos
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

        int escolha = scanner.nextInt() - 1;

        if (escolha >= 0 && escolha < movimentos.size()) {
            Movimento movEscolhido = movimentos.get(escolha);
            realizarAtaque(atacante, defensor, movEscolhido);
        } else {
            System.out.println("Movimento inválido! Tropeçou e perdeu a vez.");
        }

        return defensor.getVida() <= 0;
    }

    // Inteligência do Adversário
    private void turnoAdversario(Pokemon atacante, Pokemon defensor) {
        if (atacante.getVida() <= 0) return;
        
        System.out.println("\nTurno do adversário!");
        // Escolhe um ataque aleatório
        List<Movimento> movimentos = obterMovimentosLimitados(atacante);
        if (movimentos.isEmpty()) {
            System.out.println("O adversário não tem movimentos disponíveis.");
            return;
        }
        int indexAleatorio = random.nextInt(movimentos.size());
        Movimento movAdversario = movimentos.get(indexAleatorio);
        realizarAtaque(atacante, defensor, movAdversario);
    }

    // Matemática da Luta (Precisão, PP, Multiplicador e Dano)
    private void realizarAtaque(Pokemon atacante, Pokemon defensor, Movimento movimento) {
        System.out.println(atacante.getNome() + " usou " + movimento.getNome() + "!");

        // Checa PP
        if (movimento.getPpAtual() <= 0) {
            System.out.println("Mas não tem PP suficiente!");
            return;
        }
        movimento.setPpAtual(movimento.getPpAtual() - 1); // Gasta 1 PP

        // Checa Precisão
        int chanceAcerto = random.nextInt(100) + 1;
        if (chanceAcerto > movimento.getPrecisao()) {
            System.out.println("O ataque errou!");
            return;
        }

        // Calcula Multiplicador de Vantagem/Desvantagem usando sua classe BattleMultiplier
        double multiplicador = BattleMultiplier.getMultiplier(movimento.getTipo(), obterTipo(defensor));
        
        if (multiplicador > 1.0) System.out.println("Foi super efetivo!");
        if (multiplicador < 1.0 && multiplicador > 0) System.out.println("Não foi muito efetivo...");
        if (multiplicador == 0) {
            System.out.println("Não teve efeito nenhum!");
            return;
        }

        // Fórmula de Dano
        int danoCalculado = (int) (((obterAtaque(atacante) * movimento.getDano()) / obterDefesa(defensor)) * multiplicador);
        int danoFinal = Math.max(1, danoCalculado); // Garante que o dano mínimo seja 1

        // Aplica o dano e evita vida negativa
        int novaVida = defensor.getVida() - danoFinal;
        defensor.setVida(Math.max(0, novaVida)); 

        System.out.println("Causou " + danoFinal + " de dano!");
    }

    private Tipo obterTipo(Pokemon pokemon) {
        if (pokemon.getTipos() != null && !pokemon.getTipos().isEmpty()) {
            return pokemon.getTipos().get(0);
        }
        return Tipo.NORMAL;
    }

    private int obterAtaque(Pokemon pokemon) {
        Integer nivel = pokemon.getNivel();
        return 10 + (nivel != null ? nivel : 1) * 2;
    }

    private int obterDefesa(Pokemon pokemon) {
        Integer nivel = pokemon.getNivel();
        return 10 + (nivel != null ? nivel : 1);
    }

    private int obterVelocidade(Pokemon pokemon) {
        Integer nivel = pokemon.getNivel();
        return 10 + (nivel != null ? nivel : 1);
    }

    private List<Movimento> obterMovimentosLimitados(Pokemon pokemon) {
        List<Movimento> movimentos = pokemon.getMovimentos();
        if (movimentos == null || movimentos.isEmpty()) {
            return List.of();
        }
        return movimentos.size() <= 4 ? movimentos : movimentos.subList(0, 4);
    }

    // Fuga Simples
    private boolean tentarFugir() {
        int chance = random.nextInt(100);
        return chance > 50; // 50% de chance de conseguir fugir
    }

    // Mensagem Final
    private void encerrarBatalha(Pokemon aliado, Pokemon adversario) {
        System.out.println("\n--- FIM DE BATALHA ---");
        if (aliado.getVida() <= 0) {
            System.out.println("Seu Pokémon desmaiou. Você perdeu!");
        } else if (adversario.getVida() <= 0) {
            System.out.println("O " + adversario.getNome() + " selvagem desmaiou. Você venceu!");
        }
    }
}