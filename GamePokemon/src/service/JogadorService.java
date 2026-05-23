package service;

import model.Jogador;
import repository.JogadorRepository;

import java.sql.SQLException;

public class JogadorService {

    private final Jogador jogador;
    private final JogadorRepository repository;

    public JogadorService(Jogador jogador) {
        this.jogador = jogador;
        this.repository = new JogadorRepository();
    }

    public Jogador cadastrar(Jogador jogador) {
        try {
            repository.salvar(jogador);
            System.out.println("Jogador cadastrado com sucesso!");
            System.out.println("Nome: " + jogador.getNome());
            System.out.println("Pokemon escolhido: " + jogador.getPokemon());
        } catch (SQLException e) {
            System.out.println("Erro ao salvar jogador: " + e.getMessage());
        }
        return jogador;
    }

    public void salvarProgresso() {
        try {
            repository.salvar(jogador);
            System.out.println("Progresso salvo com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar progresso: " + e.getMessage());
        }
    }
}