package org.example.application.controller;

import com.google.gson.Gson;
import org.example.application.domain.ItemCardapio;
import org.example.framework.annotation.*;
import org.example.application.service.ItemCardapioService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MyController(path = "/cardapio")
public class ControllerCardapio {

    @Inject
    private ItemCardapioService itemCardapioService;

    @GetMapping
    public String recuperarCardapio() {
        List<ItemCardapio> itensCardapio = itemCardapioService.listaItensCardapio();
        Gson gson = new Gson();
        return gson.toJson(itensCardapio);
    }

    @PostMapping
    public String inserirItem(ItemCardapio itemCardapio) {
        itemCardapioService.adicionaItemCardapio(itemCardapio);
        List<ItemCardapio> lista = itemCardapioService.listaItensCardapio();
        Gson gson = new Gson();
        return gson.toJson(lista);
    }

    @GetMapping(path = "/total-itens")
    public String recuperarTotalItensCardapio() {
        Integer quantidadeItens = itemCardapioService.totalItensCardapio();
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("totalItens", quantidadeItens);
        Gson gson = new Gson();
        return gson.toJson(resposta);
    }

    @GetMapping(path = "/{id}")
    public String recuperarItemCardapioPorId(Long id) {
        ItemCardapio itemCardapio = itemCardapioService.itemCardapioPorId(id).orElseThrow(() -> new RuntimeException("Item cardápio não encontrado"));
        Gson gson = new Gson();
        return gson.toJson(itemCardapio);
    }

    @DeleteMapping(path = "/{id}")
    public void removerItemCardapio(Long id) {
        boolean removido = itemCardapioService.removeItemCardapio(id);
        if (!removido) {
            throw new RuntimeException("Item cardápio não encontrado para remoção");
        }
    }

    @PatchMapping(path = "/{id}")
    public void atualizarItemCardapio(Long id, BigDecimal novoValor) {
        boolean atualizado = itemCardapioService.alteraPrecoItemCardapio(id, novoValor);
        if  (!atualizado) {
            throw new RuntimeException("Item cardápio não encontrado para atualização");
        }
    }
}