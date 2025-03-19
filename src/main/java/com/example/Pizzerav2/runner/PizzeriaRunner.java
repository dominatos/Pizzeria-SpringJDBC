package com.example.Pizzerav2.runner;


import com.example.Pizzerav2.model.Menu;
import com.example.Pizzerav2.service.MenuService;
import com.example.Pizzerav2.service.OrdineService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;


@Component
public class PizzeriaRunner implements CommandLineRunner {
    @Autowired
    MenuService menuService; // new MenuService();
    @Autowired
    OrdineService ordineService;
    @Autowired @Qualifier("crea_menu")
    ObjectProvider<Menu> menuObjectProvider;

    @Override
    public void run(String... args) throws Exception {
        //menuService.menuStamp();
//        Menu menuProdotti = menuService.creamenu1();
//        menuService.salvaMenu(menuProdotti);
        //System.out.println(menuService.findMenuById(3L));
        //System.out.println(menuService.findbyname("Peppe"));
        //menuService.removeMenu(2);
        Menu menu = menuService.creamenu1();
        //menu.setNome("WTF");
        //menuService.menuStamp(menu);
        menuService.salvaMenu(menu);
        Menu menu2 = menuService.findMenuById(1l);
        menuService.menuStamp(menu2);
        //menuService.salvaMenu(menu2);
        //System.out.println(menu2);
        //menu.getPizzaList().forEach(System.out::println);

        //menu.setNome("Peppe wtf");
        //menuService.updateMenu(menu);
        //System.out.println(menu);


        ordineService.creaNordini(20);
        ordineService.stampaordini();

    }
}
