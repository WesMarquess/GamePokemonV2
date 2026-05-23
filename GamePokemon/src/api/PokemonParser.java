package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Movimento;
import model.Pokemon;
import model.enums.Tipo;

import java.util.ArrayList;
import java.util.List;

public class PokemonParser {

    private static final int MOVIMENTOS_MAXIMOS = 4;
    private final PokemonApi api;

    public PokemonParser(PokemonApi api) {
        this.api = api;
    }

    public Pokemon parserPokemon(String json) throws Exception {
        JsonObject objeto = JsonParser.parseString(json).getAsJsonObject();

        Integer id = objeto.get("id").getAsInt();
        String nome = formatarNome(objeto.get("name").getAsString());
        Integer vida = extrairStatus(objeto, "hp");
        List<Tipo> tipos = extrarTipos(objeto);
        List<Movimento> movimentos = extrairMovimentos(objeto);

        return new Pokemon(id, nome, tipos, vida, 1, movimentos);
    }

    private int extrairStatus(JsonObject object, String nomeStats) {
        JsonArray stats = object.getAsJsonArray("stats");

        for (JsonElement element : stats) {

            JsonObject jsonObject = element.getAsJsonObject();
            String nome = jsonObject.getAsJsonObject("stat").get("name").getAsString();

            if (nome.equals(nomeStats)) {
                return jsonObject.get("base_stat").getAsInt();
            }
        }
        return 50;
    }

    private List<Tipo> extrarTipos(JsonObject obj) {
        List<Tipo> tipos = new ArrayList<>();
        JsonArray array = obj.getAsJsonArray("types");

        for (JsonElement elemento : array) {
            String nomeApi = elemento.getAsJsonObject()
                    .getAsJsonObject("type")
                    .get("name").getAsString();
            Tipo tipo = mapearTipo(nomeApi);
            tipos.add(tipo);
        }
        return tipos;
    }

    private List<Movimento> extrairMovimentos(JsonObject obj) throws Exception {
        List<Movimento> movimentos = new ArrayList<>();
        JsonArray array = obj.getAsJsonArray("moves");

        int limite = Math.min(MOVIMENTOS_MAXIMOS, array.size());

        for (int i = 0; i < limite; i++) {
            JsonObject moveRef = array.get(i).getAsJsonObject()
                    .getAsJsonObject("move");
            String url = moveRef.get("url").getAsString();
            try {
                String moveJson = api.getUrl(url);
                Movimento movimento = parsearMovimento(moveJson);
                movimentos.add(movimento);
            } catch (Exception e) {
                System.out.println("Aviso: movimento ignorado - " + e.getMessage());
            }
        }
        return movimentos;
    }

    public Movimento parsearMovimento(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        String nome = formatarNome(obj.get("name").getAsString());
        int dano = obj.get("power").isJsonNull() ? 0 : obj.get("power").getAsInt();
        int pp = obj.get("pp").isJsonNull() ? 10 : obj.get("pp").getAsInt();
        int precisao = obj.get("accuracy").isJsonNull() ? 100 : obj.get("accuracy").getAsInt();
        Tipo tipo = mapearTipo(obj.getAsJsonObject("type").get("name").getAsString());

        return new Movimento(nome, dano, tipo, pp, pp, precisao);
    }

    private Tipo mapearTipo(String tipoApi) {
        return switch (tipoApi) {
            case "fire" -> Tipo.FOGO;
            case "water" -> Tipo.AGUA;
            case "grass" -> Tipo.PLANTA;
            case "electric" -> Tipo.ELETRICO;
            case "flying" -> Tipo.VOADOR;
            case "rock" -> Tipo.PEDRA;
            case "psychic" -> Tipo.PSIQUICO;
            case "ghost" -> Tipo.FANTASMA;
            case "poison" -> Tipo.VENENOSO;
            case "bug" -> Tipo.INSETO;
            case "ice" -> Tipo.GELO;
            default -> Tipo.NORMAL;
        };
    }

    private String formatarNome(String nome) {
        String[] partes = nome.split("-");
        StringBuilder sb = new StringBuilder();
        for (String parte : partes) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(Character.toUpperCase(parte.charAt(0)));
            sb.append(parte.substring(1).toLowerCase());
        }
        return sb.toString();
    }
}