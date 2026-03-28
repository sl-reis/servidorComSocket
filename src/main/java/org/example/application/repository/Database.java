package org.example.application.repository;

import org.example.application.domain.ItemCardapio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface Database {

    List<ItemCardapio> listaItensCardapio();

    Optional<ItemCardapio> itemCardapioPorId(Long id);

    boolean removeItemCardapio(Long id);

    boolean alteraPrecoItemCardapio(Long id, BigDecimal novoPreco);

    int totalItensCardapio();

    void adicionaItemCardapio(ItemCardapio item);
}
