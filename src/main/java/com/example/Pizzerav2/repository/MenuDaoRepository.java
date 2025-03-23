package com.example.Pizzerav2.repository;

import com.example.Pizzerav2.model.Menu;
import com.example.Pizzerav2.model.Prodotto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuDaoRepository {
    //Crud menu
    public void saveMenu(Menu menu);

    Menu findMenuById(Long id);

    public Menu findMenuByName(String name);


    Prodotto findProdottoById(Long id);

    void deleteMenu(int id);

    public void updateMenu(Menu menu);
}
