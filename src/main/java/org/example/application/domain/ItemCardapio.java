package org.example.application.domain;

import org.example.application.domain.enumerator.CategoriaCardapio;

import java.math.BigDecimal;

public class ItemCardapio {

    private Long id;

    private String nome;

    private String descricao;

    private CategoriaCardapio categoria;

    private BigDecimal preco;

    private BigDecimal precoPromocional;

    public ItemCardapio(Long id, String nome, String descricao, CategoriaCardapio categoria, BigDecimal preco, BigDecimal precoPromocional) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.preco = preco;
        this.precoPromocional = precoPromocional;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public CategoriaCardapio getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaCardapio categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getPrecoPromocional() {
        return precoPromocional;
    }

    public void setPrecoPromocional(BigDecimal precoPromocional) {
        this.precoPromocional = precoPromocional;
    }
}
