package model;

import enums.TipoItem;

public class Item {

    private TipoItem tipo;
    private Integer quantidade;

    public Item(TipoItem tipo, Integer quantidade) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo do item não pode ser nulo.");
        }
        if (quantidade == null || quantidade < 0) {
            throw new IllegalArgumentException("A quantidade inicial do item não pode ser nula ou negativa.");
        }

        this.tipo = tipo;
        this.quantidade = quantidade;
    }

    public TipoItem getTipo() {
        return tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        if (quantidade == null || quantidade < 0) {
            throw new IllegalArgumentException("A quantidade do item não pode ser nula ou negativa.");
        }
        this.quantidade = quantidade;
    }

    public boolean possuiQuantidade() {
        return quantidade > 0;
    }

    public void diminuirQuantidade() {
        if (quantidade > 0) {
            quantidade--;
        }
    }
    @Override
    public String toString() {
        return tipo + " x" + quantidade;
    }
}