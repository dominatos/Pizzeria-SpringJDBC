package com.example.Pizzerav2.repository;

import com.example.Pizzerav2.model.Drink;
import com.example.Pizzerav2.model.Menu;
import com.example.Pizzerav2.model.Pizza;
import com.example.Pizzerav2.model.Prodotto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuDAO implements MenuDaoRepository{
    @Autowired JdbcTemplate jdbc;
//
//    @Override
//    public void saveMenu(Menu menu) {
//String sql="Insert into menu (menu_name) values(?)";
//jdbc.update(sql, menu.getNome() );
//        menu.getPizzaList().forEach(pizza->{
//            String sql2="INSERT INTO prodotti (id_menu,ingredienti_pizza,  prodotto_name,  tipo_prodotto,gradi_drink) values (?,?,?,?,?);";
//            jdbc.update(sql2, 1L, pizza.listaIngredienti().toString() , pizza.getNome(),"pizza",null );
//        });
//        menu.getDrinkList().forEach(drink->{
//            String sql2="INSERT INTO prodotti (id_menu,ingredienti_pizza,  prodotto_name,  tipo_prodotto,gradi_drink) values (?,?,?,?,?);";
//            jdbc.update(sql2, 1L, null , drink.getNome(),"drink",drink.getGradazione() );
//        });
//
//
//        System.out.println("Menu " + menu.getNome() + " salvato nel DB!");
//    }
@Override
public void saveMenu(Menu menu) {
    String sql = "INSERT INTO menu (menu_name) values(?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbc.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
        ps.setString(1, menu.getNome());
        return ps;
    }, keyHolder);

    Long menuId = keyHolder.getKey().longValue();

    menu.getPizzaList().forEach(pizza -> {
        String ingredienti = String.join(", ", pizza.listaIngredienti());
        insertProduct(menuId, ingredienti, pizza.getNome(), "pizza", null,pizza.getPrezzo());
    });

    menu.getDrinkList().forEach(drink -> {
        insertProduct(menuId, null, drink.getNome(), "drink", drink.getGradazione(),drink.getPrezzo());
    });

    System.out.println("Menu " + menu.getNome() + " salvato nel DB!");
}

    private void insertProduct(Long menuId, String ingredienti, String nome, String tipo, Double gradazione,Double prezzo) {
        String sql = "INSERT INTO prodotti (id_menu, ingredienti_pizza, prodotto_name, tipo_prodotto, gradi_drink,prezzo) values (?, ?, ?, ?, ?, ?)";
        jdbc.update(sql, menuId, ingredienti, nome, tipo, gradazione, prezzo);
    }

    @Override
    public Menu findMenuById(Long id) {
        String menuSql = "SELECT * FROM menu WHERE id_menu = ?";
        Menu menu = jdbc.queryForObject(menuSql, new MenuRowMapper(), id);

        String prodottiSql = "SELECT * FROM prodotti WHERE id_menu = ?";
        List<Prodotto> prodotti = jdbc.query(prodottiSql, new ProdottoRowMapper(), id);

        // Разделение продуктов на пиццы и напитки с использованием instanceof
        List<Pizza> pizzaList = new ArrayList<>();
        List<Drink> drinkList = new ArrayList<>();

        for (Prodotto prodotto : prodotti) {
            if (prodotto instanceof Pizza) {
                pizzaList.add((Pizza) prodotto);
            } else if (prodotto instanceof Drink) {
                drinkList.add((Drink) prodotto);
            }
        }

        assert menu != null;
        menu.setPizzaList(pizzaList);
        menu.setDrinkList(drinkList);

        return menu;
    }

    @Override
    public Menu findMenuByName(String name) {
        String sql = "SELECT * FROM menu WHERE menu_name LIKE ? LIMIT 1;";
        return jdbc.queryForObject(sql, new MenuRowMapper(), "%" + name + "%");
    }





    @Override
    public void deleteMenu(int id) {
        String sql="DELETE from menu where id_menu ="+ id;
        jdbc.update(sql);
        System.out.println("Menu  deleted nel DB!");

    }



    @Override
    public void updateMenu(Menu menu) {
        String sql="Update menu Set menu_name= ? where id_menu ="+ menu.getId();
        jdbc.update(sql, menu.getNome() );
        System.out.println("Menu " + menu.getNome() + " modifito nel DB!");
    }

}
