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
public void dropDB() {
    String sql = "DROP DATABASE IF EXISTS saler007_jdbc";
    jdbc.execute(sql);
}
    public void initilizeDB() {

        String sql="CREATE DATABASE IF NOT EXISTS `saler007_jdbc` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;\n" +
               "USE saler007_jdbc;\n" +
               "CREATE TABLE IF NOT EXISTS `menu` (\n" +
               "  `id_menu` int NOT NULL AUTO_INCREMENT,\n" +
               "  `menu_name` varchar(150) NOT NULL,\n" +
               "  \n" +
               "  PRIMARY KEY (`id_menu`)\n" +
               ");\n" +
               "CREATE TABLE IF NOT EXISTS `prodotti` (\n" +
               "  `id_prodotto` int NOT NULL AUTO_INCREMENT,\n" +
               "  `id_menu` int NOT NULL,\n" +
               "  `ingredienti_pizza` varchar(500) DEFAULT NULL,\n" +
               "  `gradi_drink` double DEFAULT '0',\n" +
               "  `prodotto_name` varchar(150) NOT NULL,\n" +
               "  `tipo_prodotto` varchar(10) NOT NULL,\n" +
               "  `prezzo` double NOT NULL,\n" +
               "  PRIMARY KEY (`id_prodotto`),\n" +
               "  KEY `menu_prodotti` (`id_menu`),\n" +
               "  FOREIGN KEY (id_menu) REFERENCES menu(id_menu)\n" +
               ");\n" +
               "\n" +
               "\n" +
               "CREATE TABLE IF NOT EXISTS clienti (\n" +
               "    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
               "    nome_cliente VARCHAR(255) NOT NULL,\n" +
               "    cognome VARCHAR(255) NOT NULL,\n" +
               "    telefono_cliente VARCHAR(15) NOT NULL\n" +
               ");\n" +
               "CREATE TABLE IF NOT EXISTS `ordini` (\n" +
               "  `id_ordine` int NOT NULL AUTO_INCREMENT,\n" +
               "  \n" +
               " \n" +
               "  `id_cliente` BIGINT NOT NULL,\n" +
               "  \n" +
               "  PRIMARY KEY (`id_ordine`),\n" +
               "  dataOrdine TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
               "   totale DECIMAL(10, 2) NOT NULL,\n" +
               "  FOREIGN KEY (id_cliente) REFERENCES clienti(id_cliente)\n" +
               ") ;\n" +
               "CREATE TABLE IF NOT EXISTS prodotti_ordini (\n" +
               "    id_ordine int NOT NULL,\n" +
               "    id_prodotto int NOT NULL,\n" +
               "    PRIMARY KEY (id_ordine, id_prodotto),\n" +
               "    FOREIGN KEY (id_ordine) REFERENCES ordini(id_ordine),\n" +
               "    FOREIGN KEY (id_prodotto) REFERENCES prodotti(id_prodotto)\n" +
               ");\n";
        jdbc.update(sql);
    }
@Override
public void saveMenu(Menu menu) {
    String sql = "INSERT INTO menu (menu_name) values(?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    // Inserimento del menu e recupero del suo ID generato automaticamente
    jdbc.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
        ps.setString(1, menu.getNome());
        return ps;
    }, keyHolder);

    Long menuId = keyHolder.getKey().longValue(); // ID generato per il menu

    // Salvataggio delle pizze associate al menu
    menu.getPizzaList().forEach(pizza -> {
        String ingredienti = String.join(", ", pizza.listaIngredienti()); // Unione degli ingredienti in una stringa
        insertProduct(menuId, ingredienti, pizza.getNome(), "pizza", null, pizza.getPrezzo());
    });

    // Salvataggio delle bevande associate al menu
    menu.getDrinkList().forEach(drink -> {
        insertProduct(menuId, null, drink.getNome(), "drink", drink.getGradazione(), drink.getPrezzo());
    });

    System.out.println("Menu " + menu.getNome() + " salvato nel DB!");
}

    // Metodo privato per inserire un prodotto (pizza o bevanda) nella tabella 'prodotti'
    private void insertProduct(Long menuId, String ingredienti, String nome, String tipo, Double gradazione, Double prezzo) {
        String sql = "INSERT INTO prodotti (id_menu, ingredienti_pizza, prodotto_name, tipo_prodotto, gradi_drink, prezzo) values (?, ?, ?, ?, ?, ?)";
        jdbc.update(sql, menuId, ingredienti, nome, tipo, gradazione, prezzo);
    }

    // Ricerca di un menu tramite il suo ID
    @Override
    public Menu findMenuById(Long id) {
        String menuSql = "SELECT * FROM menu WHERE id_menu = ?";
        Menu menu = jdbc.queryForObject(menuSql, new MenuRowMapper(), id); // Recupero del menu

        String prodottiSql = "SELECT * FROM prodotti WHERE id_menu = ?";
        List<Prodotto> prodotti = jdbc.query(prodottiSql, new ProdottoRowMapper(), id); // Recupero dei prodotti associati

        // Divisione dei prodotti in pizze e bevande
        List<Pizza> pizzaList = new ArrayList<>();
        List<Drink> drinkList = new ArrayList<>();

        for (Prodotto prodotto : prodotti) {
            if (prodotto instanceof Pizza) {
                pizzaList.add((Pizza) prodotto);
            } else if (prodotto instanceof Drink) {
                drinkList.add((Drink) prodotto);
            }
        }

        // Associare le liste di pizze e bevande al menu
        assert menu != null;
        menu.setPizzaList(pizzaList);
        menu.setDrinkList(drinkList);

        return menu;
    }

    // Ricerca di un menu tramite il suo nome
    @Override
    public Menu findMenuByName(String name) {
        String sql = "SELECT * FROM menu WHERE menu_name LIKE ? LIMIT 1;";
        Menu menu= jdbc.queryForObject(sql, new MenuRowMapper(), "%" + name + "%");
        String prodottiSql = "SELECT * FROM prodotti WHERE id_menu = ?";
        List<Prodotto> prodotti = jdbc.query(prodottiSql, new ProdottoRowMapper(), menu.getId()); // Recupero dei prodotti associati

        // Divisione dei prodotti in pizze e bevande
        List<Pizza> pizzaList = new ArrayList<>();
        List<Drink> drinkList = new ArrayList<>();

        for (Prodotto prodotto : prodotti) {
            if (prodotto instanceof Pizza) {
                pizzaList.add((Pizza) prodotto);
            } else if (prodotto instanceof Drink) {
                drinkList.add((Drink) prodotto);
            }
        }

        // Associare le liste di pizze e bevande al menu
        assert menu != null;
        menu.setPizzaList(pizzaList);
        menu.setDrinkList(drinkList);
        return menu;
    }

    // Ricerca di un prodotto tramite il suo ID
    @Override
    public Prodotto findProdottoById(Long id) {
        String sql = "SELECT * FROM prodotti WHERE id_prodotto = ?";
        return jdbc.queryForObject(sql, new ProdottoRowMapper(), id);
    }

    // Eliminazione di un menu tramite il suo ID
    @Override
    public void deleteMenu(int id) {
        String sql = "DELETE from menu where id_menu = " + id;
        jdbc.update(sql);
        System.out.println("Menu eliminato nel DB!");
    }

    // Aggiornamento del nome di un menu
    @Override
    public void updateMenu(Menu menu) {
        String sql = "UPDATE menu SET menu_name = ? WHERE id_menu = " + menu.getId();
        jdbc.update(sql, menu.getNome());
        System.out.println("Menu " + menu.getNome() + " modificato nel DB!");
    }
}
