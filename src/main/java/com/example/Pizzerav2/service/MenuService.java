package com.example.Pizzerav2.service;



import com.example.Pizzerav2.model.Drink;
import com.example.Pizzerav2.model.Menu;
import com.example.Pizzerav2.model.Pizza;
import com.example.Pizzerav2.repository.MenuDAO;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MenuService {
    @Autowired
    MenuDAO db;
    @Autowired @Qualifier ("crea_menu") ObjectProvider<Menu> menuObjectProvider;

    @Autowired @Qualifier("salsicciap")    ObjectProvider<Pizza> pizzasalsicciapObjectProvider;
    @Autowired @Qualifier("salsiccia") ObjectProvider<Pizza> pizzasalsicciaObjectProvider;
    @Autowired @Qualifier("margeritta")    ObjectProvider<Pizza> pizzaMargheritaObjectProvider;
    @Autowired @Qualifier("createpizzac")     ObjectProvider<Pizza> pizzaObjectProvider;

    @Autowired @Qualifier("coca")     ObjectProvider<Drink> drinkCocaColaObjectProvider;
    @Autowired @Qualifier("birra")     ObjectProvider<Drink> drinkBirraObjectProvider;
    @Autowired @Qualifier("drinkgener") ObjectProvider<Drink> drinkObjectProvider;



    public  Drink drinkgener(String name,double grad, double pr) {
Drink drink=drinkObjectProvider.getIfAvailable();
                drink.setGradazione(grad);
                drink.setNome(name);
                drink.setPrezzo(pr);
        return drink;
    }

    public Pizza createpizza(String nome, List<String> ingredienti, Double prezzo1) {
        Pizza pizza = pizzaObjectProvider.getObject();
        pizza.setNome(nome);
        pizza.setIngredienti(ingredienti);
        pizza.setPrezzo(prezzo1);
        //Pizza pizza = new Pizza(nome, ingredienti, prezzo1);


        return pizza;
    }
    public  Pizza getPizzaMargerita() {
        return pizzaMargheritaObjectProvider.getObject();
    }
    public  Pizza getPizzasalsicciap() {
        return pizzasalsicciapObjectProvider.getObject();
    }
    public  Pizza getPizzasalsiccia() {
        return pizzasalsicciaObjectProvider.getObject();
    }
    public  Pizza getPizza() {
        return pizzaObjectProvider.getObject();
    }


    public  Drink getDrinkCocaCola() {
        return drinkCocaColaObjectProvider.getObject();
    }
    public  Drink getDrinkbirra() {
        return drinkBirraObjectProvider.getObject();
    }
    public Drink getDrinkdrinkgener() {
        return drinkObjectProvider.getObject();
    }
    @Bean(name="crea_menu")
    @Scope("prototype")
    public Menu creamenu1() {
//        Menu menu = menuObjectProvider.getObject();
        Menu menu= new Menu();
        menu.setNome("DA Peppe");

        // Добавление пицц через провайдеры
        menu.getPizzaList().add(getPizzaMargerita());
        menu.getPizzaList().add(getPizzasalsiccia());
        menu.getPizzaList().add(getPizzasalsicciap());

        // Создание пиццы "test" через провайдер
        Pizza testPizza = pizzaObjectProvider.getObject();
        testPizza.setNome("test");
        testPizza.setIngredienti(List.of("base", "base3", "pepe"));
        testPizza.setPrezzo(12.00);
        menu.getPizzaList().add(testPizza);

        // Создание пиццы "Gorgonzolla"
        Pizza gorgonzollaPizza = pizzaObjectProvider.getObject();
        gorgonzollaPizza.setNome("Gorgonzolla");
        gorgonzollaPizza.setIngredienti(List.of("base", "gorgonzola", "salsa"));
        gorgonzollaPizza.setPrezzo(9.00);
        menu.getPizzaList().add(gorgonzollaPizza);

        // Создание пиццы "Quattro formaggi"
        Pizza quattroFormaggiPizza = pizzaObjectProvider.getObject();
        quattroFormaggiPizza.setNome("Quattro formaggi");
        quattroFormaggiPizza.setIngredienti(List.of("base", "gorgonzola", "mozarella", "elemental", "parmeggiano reggiano"));
        quattroFormaggiPizza.setPrezzo(9.00);
        menu.getPizzaList().add(quattroFormaggiPizza);

        // Добавление напитков через провайдеры
        menu.getDrinkList().add(getDrinkCocaCola());
        menu.getDrinkList().add(getDrinkbirra());

        // Создание напитка "Vino Bianco"
        Drink vinoBianco = drinkObjectProvider.getObject();
        vinoBianco.setNome("Vino Bianco");
        vinoBianco.setGradazione(12.00);
        vinoBianco.setPrezzo(10.00);
        menu.getDrinkList().add(vinoBianco);

        // Создание напитка "Vino rosso"
        Drink vinoRosso = drinkObjectProvider.getObject();
        vinoRosso.setNome("Vino rosso");
        vinoRosso.setGradazione(12.00);
        vinoRosso.setPrezzo(10.00);
        menu.getDrinkList().add(vinoRosso);

        // Создание напитка "Spritz"
        Drink spritz = drinkObjectProvider.getObject();
        spritz.setNome("Spritz");
        spritz.setGradazione(12.00);
        spritz.setPrezzo(5.00);
        menu.getDrinkList().add(spritz);

        // Создание напитка "Caffe espresso"
        Drink caffeEspresso = drinkObjectProvider.getObject();
        caffeEspresso.setNome("Caffe espresso");
        caffeEspresso.setGradazione(0.00);
        caffeEspresso.setPrezzo(1.20);
        menu.getDrinkList().add(caffeEspresso);

        // Создание напитка "Te verde"
        Drink teVerde = drinkObjectProvider.getObject();
        teVerde.setNome("Te verde");
        teVerde.setGradazione(0.00);
        teVerde.setPrezzo(1.20);
        menu.getDrinkList().add(teVerde);

        return menu;
    }
    public void salvaMenu(Menu menu) {
        db.saveMenu(menu);
    }
    public void updateMenu(Menu menu)
    {
        db.updateMenu(menu);
    }
    public void removeMenu(int id)
    {
        db.deleteMenu(id);
    }


    public Menu findMenuByName(String name)
    {
        Menu menu=new Menu();
        menu=db.findMenuByName(name);
        return  menu;
    }

    public Menu findMenuById(Long id)
    {
        Menu menu=new Menu();
        menu=db.findMenuById(id);
        return  menu;
    }
    public Menu findbyname(String name) {
        return db.findMenuByName(name);
    }


//    public Menu leggiMenu(Menu menu) {
//        db.findMenuById(menu.getId());
//        return menu;
//    }
//    public void menuStamp()
//    {
//        Menu menu = creamenu1();
//        System.out.println("************* Menu " + menu.getNome() +" **************");
//        System.out.println("----- Pizze -----");
//        menu.getPizzaList().forEach(System.out::println);
//        System.out.println("----- Drink -----");
//        menu.getDrinkList().forEach(System.out::println);
//
//    }
public void menuStamp(Menu menu) {

//    Menu menu = menuObjectProvider.getObject();

    System.out.println("************* Menu!" + menu.getNome() +" **************");
    System.out.println("----- Pizze -----");
    menu.getPizzaList().forEach(System.out::println);
    System.out.println("----- Drink -----");
    menu.getDrinkList().forEach(System.out::println);
}


}

