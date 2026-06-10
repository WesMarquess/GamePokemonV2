package repository;

import connection.DatabaseConnection;
import enums.TipoItem;
import model.Item;
import model.Jogador;
import model.Pokemon;

import java.sql.*;

public class JogadorRepository {

    public void salvar(Jogador jogador) throws SQLException {
        if (jogador.getId() == null) {
            inserir(jogador);
        } else {
            atualizar(jogador);
        }
    }

    private void inserir(Jogador jogador) throws SQLException {
        String sql = """
                    INSERT INTO jogador (nome, pokemon_id, vida_atual, nivel_atual, xp_atual, qtd_pocao, qtd_pokebola, qtd_reviver)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, jogador.getNome());
            stmt.setInt(2, jogador.getPokemon().getId());
            stmt.setInt(3, jogador.getPokemon().getVida());
            stmt.setInt(4, jogador.getPokemon().getNivel());
            stmt.setInt(5, jogador.getPokemon().getXp());
            stmt.setInt(6, quantidadeItem(jogador, TipoItem.POCAO));
            stmt.setInt(7, quantidadeItem(jogador, TipoItem.POKEBOLA));
            stmt.setInt(8, quantidadeItem(jogador, TipoItem.REVIVER));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                jogador.setId(rs.getInt(1));
            }
        }
    }

    private void atualizar(Jogador jogador) throws SQLException {
        String sql = """
                    UPDATE jogador SET nome = ?, pokemon_id = ?, vida_atual = ?, nivel_atual = ?,
                    xp_atual = ?, qtd_pocao = ?, qtd_pokebola = ?, qtd_reviver = ?
                    WHERE id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, jogador.getNome());
            stmt.setInt(2, jogador.getPokemon().getId());
            stmt.setInt(3, jogador.getPokemon().getVida());
            stmt.setInt(4, jogador.getPokemon().getNivel());
            stmt.setInt(5, jogador.getPokemon().getXp());
            stmt.setInt(6, quantidadeItem(jogador, TipoItem.POCAO));
            stmt.setInt(7, quantidadeItem(jogador, TipoItem.POKEBOLA));
            stmt.setInt(8, quantidadeItem(jogador, TipoItem.REVIVER));
            stmt.setInt(9, jogador.getId());
            stmt.executeUpdate();
        }
    }

    private int quantidadeItem(Jogador jogador, TipoItem tipo) {
        return jogador.getItens().stream()
                .filter(i -> i.getTipo() == tipo)
                .map(Item::getQuantidade)
                .findFirst()
                .orElse(0);
    }

    public Jogador buscarPorId(int id) throws SQLException {
        String sql = """
                    SELECT j.id, j.nome, j.vida_atual, j.nivel_atual, j.pokemon_id,
                           j.xp_atual, j.qtd_pocao, j.qtd_pokebola, j.qtd_reviver
                    FROM jogador j
                    WHERE j.id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                PokemonRepository pokemonRepository = new PokemonRepository();
                Pokemon pokemon = pokemonRepository.buscarPorId(rs.getInt("pokemon_id"));
                pokemon.setVida(rs.getInt("vida_atual"));
                pokemon.setNivel(rs.getInt("nivel_atual"));
                pokemon.setXp(rs.getInt("xp_atual"));

                Jogador jogador = new Jogador(rs.getInt("id"), rs.getString("nome"));
                jogador.adicionarPokemon(pokemon);
                jogador.setQuantidadeItem(TipoItem.POCAO, rs.getInt("qtd_pocao"));
                jogador.setQuantidadeItem(TipoItem.POKEBOLA, rs.getInt("qtd_pokebola"));
                jogador.setQuantidadeItem(TipoItem.REVIVER, rs.getInt("qtd_reviver"));

                return jogador;
            }
            return null;
        }
    }
}