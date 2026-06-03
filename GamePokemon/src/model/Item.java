package model;

import enums.TipoItem;

public class Item {

    private TipoItem tipo;
    private Integer quantidade;

    public Item(TipoItem tipo, Integer quantidade) {
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