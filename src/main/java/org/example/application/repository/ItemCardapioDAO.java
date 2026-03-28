package org.example.application.repository;

import org.example.application.domain.ItemCardapio;
import org.example.application.domain.enumerator.CategoriaCardapio;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemCardapioDAO implements Database {

    @Override
    public List<ItemCardapio> listaItensCardapio() {
        String sql = "select id, nome, descricao, categoria, preco, preco_promocional from item_cardapio";
        List<ItemCardapio> itensCardapio = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nome = resultSet.getString("nome");
                String descricao = resultSet.getString("descricao");
                String categoria = resultSet.getString("categoria");
                BigDecimal preco = resultSet.getBigDecimal("preco");
                BigDecimal precoPromocional = resultSet.getBigDecimal("preco_promocional");
                ItemCardapio itemCardapio = new ItemCardapio(id, nome, descricao, CategoriaCardapio.valueOf(categoria), preco, precoPromocional);
                itensCardapio.add(itemCardapio);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itensCardapio;
    }

    @Override
    public int totalItensCardapio() {
        String sql = "select count(*) from item_cardapio";
        try (Connection connection = ConnectionFactory.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public void adicionaItemCardapio(ItemCardapio item) {
        String sql = "insert into item_cardapio (nome, descricao, categoria, preco, preco_promocional) values (?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionFactory.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            //statement.setLong(1, item.getId());
            statement.setString(1, item.getNome());
            statement.setString(2, item.getDescricao());
            statement.setString(3, item.getCategoria().name());
            statement.setBigDecimal(4, item.getPreco());
            statement.setBigDecimal(5, item.getPrecoPromocional());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ItemCardapio> itemCardapioPorId(Long id) {
        String sql = "select id, nome, descricao, categoria, preco, preco_promocional from item_cardapio where id = ?";
        try (Connection connection = ConnectionFactory.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String nome = resultSet.getString("nome");
                        String descricao = resultSet.getString("descricao");
                        String categoria = resultSet.getString("categoria");
                        BigDecimal preco = resultSet.getBigDecimal("preco");
                        BigDecimal precoPromocional = resultSet.getBigDecimal("preco_promocional");
                        ItemCardapio itemCardapio = new ItemCardapio(id, nome, descricao, CategoriaCardapio.valueOf(categoria), preco, precoPromocional);
                        return Optional.of(itemCardapio);
                    }
                }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeItemCardapio(Long id) {
        String sql = "delete from item_cardapio where id = ?";
        try (Connection connection = ConnectionFactory.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            int itemAfetados = statement.executeUpdate();
            return itemAfetados > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean alteraPrecoItemCardapio(Long id, BigDecimal novoPreco) {
        String sql = "update item_cardapio set preco = ? where id = ?";
        try (Connection connection = ConnectionFactory.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, novoPreco);
            statement.setLong(2, id);
            int itemAfetados = statement.executeUpdate();
            return itemAfetados > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
