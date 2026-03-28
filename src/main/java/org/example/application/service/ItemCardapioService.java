package org.example.application.service;

import org.example.application.domain.ItemCardapio;
import org.example.framework.annotation.Inject;
import org.example.application.repository.ItemCardapioDAO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ItemCardapioService {

    @Inject
    private ItemCardapioDAO itemCardapioDAO;

    public List<ItemCardapio> listaItensCardapio() {
        return itemCardapioDAO.listaItensCardapio();
    }

    public int totalItensCardapio() {
        return itemCardapioDAO.totalItensCardapio();
    }

    public void adicionaItemCardapio(ItemCardapio item) {
        itemCardapioDAO.adicionaItemCardapio(item);
    }

    public Optional<ItemCardapio> itemCardapioPorId(Long id) {
        return itemCardapioDAO.itemCardapioPorId(id);
    }

    public boolean removeItemCardapio(Long id) {
        return itemCardapioDAO.removeItemCardapio(id);
    }

    public boolean alteraPrecoItemCardapio(Long id, BigDecimal novoPreco) {
        return itemCardapioDAO.alteraPrecoItemCardapio(id, novoPreco);
    }
}
