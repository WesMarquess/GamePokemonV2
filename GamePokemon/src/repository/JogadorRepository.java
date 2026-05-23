package repository;

import connection.DatabaseConnection;
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
                    INSERT INTO jogador (nome, pokemon_id, vida_atual, nivel_atual, pocoes)
                    VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, jogador.getNome());
            stmt.setInt(2, jogador.getPokemon().getId());
            stmt.setInt(3, jogador.getPokemon().getVida());
            stmt.setInt(4, jogador.getPokemon().getNivel());
            stmt.setInt(5, jogador.getPocao());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                jogador.setId(rs.getInt(1));
            }
        }
    }

    private void atualizar(Jogador jogador) throws SQLException {
        String sql = """
                    UPDATE jogador SET nome = ?, pokemon_id = ?, vida_atual = ?, nivel_atual = ?, pocoes = ?
                    WHERE id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, jogador.getNome());
            stmt.setInt(2, jogador.getPokemon().getId());
            stmt.setInt(3, jogador.getPokemon().getVida());
            stmt.setInt(4, jogador.getPokemon().getNivel());
            stmt.setInt(5, jogador.getPocao());
            stmt.setInt(6, jogador.getId());
            stmt.executeUpdate();
        }
    }

    public Jogador buscarPorId(int id) throws SQLException {
        String sql = """
                    SELECT j.id, j.nome, j.pocoes, j.vida_atual, j.nivel_atual, j.pokemon_id
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

                return new Jogador(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        pokemon,
                        rs.getInt("pocoes")
                );
            }
            return null;
        }
    }
}