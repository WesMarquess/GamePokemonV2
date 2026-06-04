package model;

import enums.Tipo;

public class Movimento {
    private String nome;
    private Integer dano;
    private Tipo tipo;
    private Integer ppAtual;
    private Integer ppMaximo;
    private Integer precisao;

    public Movimento(String nome, Integer dano, Tipo tipo, Integer ppAtual, Integer ppMaximo, Integer precisao) {
        // Validações de nulidade e valores inconsistentes no construtor
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do movimento não pode ser nulo ou vazio.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo do movimento não pode ser nulo.");
        }
        if (dano != null && dano < 0) {
            throw new IllegalArgumentException("O dano do movimento não pode ser negativo.");
        }
        if (ppMaximo == null || ppMaximo <= 0) {
            throw new IllegalArgumentException("O PP máximo deve ser maior que zero.");
        }
        if (precisao != null && (precisao < 0 || precisao > 100)) {
            throw new IllegalArgumentException("A precisão deve estar entre 0 e 100.");
        }

        this.nome = nome;
        this.dano = (dano == null) ? 0 : dano; // Se a API mandar nulo, assume 0 (movimento de status)
        this.tipo = tipo;
        this.ppMaximo = ppMaximo;
        this.precisao = (precisao == null) ? 100 : precisao; // Se não tiver precisão definida, assume 100

        // Garante que o PP atual respeite os limites na criação
        setPpAtual(ppAtual);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome inválido.");
        }
        this.nome = nome;
    }

    public Integer getDano() {
        return dano;
    }

    public void setDano(Integer dano) {
        if (dano == null || dano < 0) {
            throw new IllegalArgumentException("O dano não pode ser negativo ou nulo.");
        }
        this.dano = dano;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo não pode ser nulo.");
        }
        this.tipo = tipo;
    }

    public Integer getPpAtual() {
        return ppAtual;
    }

    public void setPpAtual(Integer ppAtual) {
        // Validação defensiva para manter os limites do PP do ataque
        if (ppAtual == null || ppAtual < 0) {
            this.ppAtual = 0;
        } else if (ppAtual > this.ppMaximo) {
            this.ppAtual = this.ppMaximo;
        } else {
            this.ppAtual = ppAtual;
        }
    }

    public Integer getPpMaximo() {
        return ppMaximo;
    }

    public void setPpMaximo(Integer ppMaximo) {
        if (ppMaximo == null || ppMaximo <= 0) {
            throw new IllegalArgumentException("PP Máximo inválido.");
        }
        this.ppMaximo = ppMaximo;
        // Ajusta o PP atual se o novo máximo for menor que ele
        if (this.ppAtual > this.ppMaximo) {
            this.ppAtual = this.ppMaximo;
        }
    }

    public Integer getPrecisao() {
        return precisao;
    }

    public void setPrecisao(Integer precisao) {
        if (precisao == null || precisao < 0 || precisao > 100) {
            throw new IllegalArgumentException("A precisão deve ser um valor entre 0 e 100.");
        }
        this.precisao = precisao;
    }
}