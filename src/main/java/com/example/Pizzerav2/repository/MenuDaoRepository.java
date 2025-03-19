package com.example.Pizzerav2.repository;

import com.example.Pizzerav2.model.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuDaoRepository {
    //Crud menu
    public void saveMenu(Menu menu);

    Menu findMenuById(Long id);

    public Menu findMenuByName(String name);



    void deleteMenu(int id);

    public void updateMenu(Menu menu);
}
