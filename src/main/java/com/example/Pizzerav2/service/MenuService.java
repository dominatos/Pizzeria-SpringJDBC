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

    // DAO per l'interazione con il database dei menu
    @Autowired
    MenuDAO db;

    // Provider per ottenere menu configurati dinamicamente tramite Spring
    @Autowired
    @Qualifier("crea_menu")
    ObjectProvider<Menu> menuObjectProvider;

    // Provider per diverse pizze predefinite
    @Autowired @Qualifier("salsicciap")
    ObjectProvider<Pizza> pizzasalsicciapObjectProvider;
    @Autowired @Qualifier("salsiccia")
    ObjectProvider<Pizza> pizzasalsicciaObjectProvider;
    @Autowired @Qualifier("margeritta")
    ObjectProvider<Pizza> pizzaMargheritaObjectProvider;
    @Autowired @Qualifier("createpizzac")
    ObjectProvider<Pizza> pizzaObjectProvider;

    // Provider per le bevande predefinite
    @Autowired @Qualifier("coca")
    ObjectProvider<Drink> drinkCocaColaObjectProvider;
    @Autowired @Qualifier("birra")
    ObjectProvider<Drink> drinkBirraObjectProvider;
    @Autowired @Qualifier("drinkgener")
    ObjectProvider<Drink> drinkObjectProvider;

    // Metodo per creare una nuova bevanda personalizzata
    public Drink drinkgener(String name, double grad, double pr) {
        Drink drink = drinkObjectProvider.getIfAvailable(); // Ottiene un'istanza di Drink
        drink.setGradazione(grad); // Imposta la gradazione
        drink.setNome(name);       // Imposta il nome
        drink.setPrezzo(pr);       // Imposta il prezzo
        return drink;
    }

    // Metodo per creare una nuova pizza personalizzata
    public Pizza createpizza(String nome, List<String> ingredienti, Double prezzo1) {
        Pizza pizza = pizzaObjectProvider.getObject(); // Ottiene un'istanza di Pizza
        pizza.setNome(nome); // Imposta il nome
        pizza.setIngredienti(ingredienti); // Imposta gli ingredienti
        pizza.setPrezzo(prezzo1); // Imposta il prezzo
        return pizza;
    }

    // Metodi per ottenere pizze predefinite tramite i rispettivi provider
    public Pizza getPizzaMargerita() {
        return pizzaMargheritaObjectProvider.getObject();
    }
    public Pizza getPizzasalsicciap() {
        return pizzasalsicciapObjectProvider.getObject();
    }
    public Pizza getPizzasalsiccia() {
        return pizzasalsicciaObjectProvider.getObject();
    }
    public Pizza getPizza() {
        return pizzaObjectProvider.getObject();
    }

    // Metodi per ottenere bevande predefinite tramite i rispettivi provider
    public Drink getDrinkCocaCola() {
        return drinkCocaColaObjectProvider.getObject();
    }
    public Drink getDrinkbirra() {
        return drinkBirraObjectProvider.getObject();
    }
    public Drink getDrinkdrinkgener() {
        return drinkObjectProvider.getObject();
    }

    // Bean Spring per creare un nuovo menu (ogni volta che viene richiesto)
    @Bean(name="crea_menu")
    @Scope("prototype") // Il menu è un prototipo: viene creato un nuovo oggetto ad ogni richiesta
    public Menu creamenu() {
        Menu menu = new Menu(); // Creazione di un nuovo menu
        menu.setNome("DA Peppe");

        // Aggiunta di pizze tramite i provider predefiniti
        menu.getPizzaList().add(getPizzaMargerita());
        menu.getPizzaList().add(getPizzasalsiccia());
        menu.getPizzaList().add(getPizzasalsicciap());

        // Creazione di pizze personalizzate e aggiunta al menu
        Pizza testPizza = pizzaObjectProvider.getObject();
        testPizza.setNome("Capricciosa");
        testPizza.setIngredienti(List.of("base", "pomodoro", "mozzarella", "prosciutto cotto o crudo",
                "funghi champignon", "olive verdi e nere", "carciofini"));
        testPizza.setPrezzo(12.00);
        menu.getPizzaList().add(testPizza);

        // Creazione della pizza "Gorgonzolla"
        Pizza gorgonzollaPizza = pizzaObjectProvider.getObject();
        gorgonzollaPizza.setNome("Gorgonzolla");
        gorgonzollaPizza.setIngredienti(List.of("base", "gorgonzola", "salsa"));
        gorgonzollaPizza.setPrezzo(9.00);
        menu.getPizzaList().add(gorgonzollaPizza);

        // Creazione della pizza "Quattro formaggi"
        Pizza quattroFormaggiPizza = pizzaObjectProvider.getObject();
        quattroFormaggiPizza.setNome("Quattro formaggi");
        quattroFormaggiPizza.setIngredienti(List.of("base", "gorgonzola", "mozzarella", "emmental", "parmigiano reggiano"));
        quattroFormaggiPizza.setPrezzo(9.00);
        menu.getPizzaList().add(quattroFormaggiPizza);

        // Aggiunta di bevande tramite i provider predefiniti
        menu.getDrinkList().add(getDrinkCocaCola());
        menu.getDrinkList().add(getDrinkbirra());

        // Creazione di bevande personalizzate
        Drink vinoBianco = drinkObjectProvider.getObject();
        vinoBianco.setNome("Vino Bianco");
        vinoBianco.setGradazione(12.00);
        vinoBianco.setPrezzo(10.00);
        menu.getDrinkList().add(vinoBianco);

        Drink vinoRosso = drinkObjectProvider.getObject();
        vinoRosso.setNome("Vino rosso");
        vinoRosso.setGradazione(12.00);
        vinoRosso.setPrezzo(10.00);
        menu.getDrinkList().add(vinoRosso);

        Drink spritz = drinkObjectProvider.getObject();
        spritz.setNome("Spritz");
        spritz.setGradazione(12.00);
        spritz.setPrezzo(5.00);
        menu.getDrinkList().add(spritz);

        Drink caffeEspresso = drinkObjectProvider.getObject();
        caffeEspresso.setNome("Caffe espresso");
        caffeEspresso.setGradazione(0.00);
        caffeEspresso.setPrezzo(1.20);
        menu.getDrinkList().add(caffeEspresso);

        Drink teVerde = drinkObjectProvider.getObject();
        teVerde.setNome("Te verde");
        teVerde.setGradazione(0.00);
        teVerde.setPrezzo(1.20);
        menu.getDrinkList().add(teVerde);

        return menu;
    }

    // Metodi per salvare, aggiornare ed eliminare un menu dal database
    public void salvaMenu(Menu menu) {
        db.saveMenu(menu);
    }
    public void updateMenu(Menu menu) {
        db.updateMenu(menu);
    }
    public void removeMenu(int id) {
        db.deleteMenu(id);
    }

    // Metodi per trovare un menu per nome o ID
    public Menu findMenuByName(String name) {
        return db.findMenuByName(name);
    }
    public Menu findMenuById(Long id) {
        return db.findMenuById(id);
    }

    // Metodo per stampare un menu
    public void menuStamp(Menu menu) {
        System.out.println("************* Menu " + menu.getNome() + " **************");
        System.out.println("----- Pizze -----");
        menu.getPizzaList().forEach(System.out::println);
        System.out.println("----- Drink -----");
        menu.getDrinkList().forEach(System.out::println);
    }
}
