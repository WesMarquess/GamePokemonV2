package battle;

import enums.Tipo;

import java.util.HashMap;
import java.util.Map;

public class BattleMultiplier {

    private static final Map<Tipo, Map<Tipo, Double>> chart =
            new HashMap<>();

    static {

        for (Tipo attacker : Tipo.values()) {
            chart.put(attacker, new HashMap<>());
        }

        // FOGO
        chart.get(Tipo.FOGO).put(Tipo.PLANTA, 2.0);
        chart.get(Tipo.FOGO).put(Tipo.INSETO, 2.0);
        chart.get(Tipo.FOGO).put(Tipo.GELO, 2.0);
        chart.get(Tipo.FOGO).put(Tipo.PEDRA, 0.5);
        chart.get(Tipo.FOGO).put(Tipo.AGUA, 0.5);
        chart.get(Tipo.FOGO).put(Tipo.FOGO, 1.0);

        // AGUA
        chart.get(Tipo.AGUA).put(Tipo.FOGO, 2.0);
        chart.get(Tipo.AGUA).put(Tipo.PEDRA, 2.0);
        chart.get(Tipo.AGUA).put(Tipo.TERRA, 2.0);
        chart.get(Tipo.AGUA).put(Tipo.PLANTA, 0.5);
        chart.get(Tipo.AGUA).put(Tipo.ELETRICO, 0.5);
        chart.get(Tipo.AGUA).put(Tipo.AGUA, 1.0);

        // PLANTA
        chart.get(Tipo.PLANTA).put(Tipo.AGUA, 2.0);
        chart.get(Tipo.PLANTA).put(Tipo.TERRA, 2.0);
        chart.get(Tipo.PLANTA).put(Tipo.PEDRA, 2.0);
        chart.get(Tipo.PLANTA).put(Tipo.FOGO, 0.5);
        chart.get(Tipo.PLANTA).put(Tipo.INSETO, 0.5);
        chart.get(Tipo.PLANTA).put(Tipo.VOADOR, 0.5);
        chart.get(Tipo.PLANTA).put(Tipo.VENENO, 0.5);
        chart.get(Tipo.PLANTA).put(Tipo.PLANTA, 1.0);

        // ELETRICO
        chart.get(Tipo.ELETRICO).put(Tipo.AGUA, 2.0);
        chart.get(Tipo.ELETRICO).put(Tipo.VOADOR, 2.0);
        chart.get(Tipo.ELETRICO).put(Tipo.TERRA, 0.0);
        chart.get(Tipo.ELETRICO).put(Tipo.ELETRICO, 1.0);

        // PEDRA
        chart.get(Tipo.PEDRA).put(Tipo.FOGO, 2.0);
        chart.get(Tipo.PEDRA).put(Tipo.VOADOR, 2.0);
        chart.get(Tipo.PEDRA).put(Tipo.INSETO, 2.0);
        chart.get(Tipo.PEDRA).put(Tipo.GELO, 2.0);
        chart.get(Tipo.PEDRA).put(Tipo.LUTADOR, 0.5);
        chart.get(Tipo.PEDRA).put(Tipo.TERRA, 0.5);
        chart.get(Tipo.PEDRA).put(Tipo.AGUA, 0.5);
        chart.get(Tipo.PEDRA).put(Tipo.PEDRA, 1.0);

        // FANTASMA
        chart.get(Tipo.FANTASMA).put(Tipo.PSIQUICO, 2.0);
        chart.get(Tipo.FANTASMA).put(Tipo.NORMAL, 0.0);
        chart.get(Tipo.FANTASMA).put(Tipo.LUTADOR, 0.0);
        chart.get(Tipo.FANTASMA).put(Tipo.FANTASMA, 2.0);

        // LUTADOR
        chart.get(Tipo.LUTADOR).put(Tipo.NORMAL, 2.0);
        chart.get(Tipo.LUTADOR).put(Tipo.PEDRA, 2.0);
        chart.get(Tipo.LUTADOR).put(Tipo.GELO, 2.0);
        chart.get(Tipo.LUTADOR).put(Tipo.PSIQUICO, 0.5);
        chart.get(Tipo.LUTADOR).put(Tipo.VOADOR, 0.5);
        chart.get(Tipo.LUTADOR).put(Tipo.INSETO, 0.5);
        chart.get(Tipo.LUTADOR).put(Tipo.LUTADOR, 1.0);

        // TERRA
        chart.get(Tipo.TERRA).put(Tipo.FOGO, 2.0);
        chart.get(Tipo.TERRA).put(Tipo.ELETRICO, 2.0);
        chart.get(Tipo.TERRA).put(Tipo.VENENO, 2.0);
        chart.get(Tipo.TERRA).put(Tipo.PEDRA, 2.0);
        chart.get(Tipo.TERRA).put(Tipo.PLANTA, 0.5);
        chart.get(Tipo.TERRA).put(Tipo.GELO, 0.5);
        chart.get(Tipo.TERRA).put(Tipo.AGUA, 0.5);
        chart.get(Tipo.TERRA).put(Tipo.TERRA, 1.0);

        // DRAGAO
        chart.get(Tipo.DRAGAO).put(Tipo.DRAGAO, 2.0);
        chart.get(Tipo.DRAGAO).put(Tipo.GELO, 0.5);
        chart.get(Tipo.DRAGAO).put(Tipo.VOADOR, 1.0);
    }

    public static double getMultiplier(Tipo attacker, Tipo defender) {

        Map<Tipo, Double> inner = chart.get(attacker);

        if (inner == null) {
            return 1.0;
        }

        return inner.getOrDefault(defender, 1.0);
    }
}