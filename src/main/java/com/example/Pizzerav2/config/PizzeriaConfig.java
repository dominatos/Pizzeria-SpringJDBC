package com.example.Pizzerav2.config;



import com.example.Pizzerav2.model.Cliente;
import com.example.Pizzerav2.model.Drink;
import com.example.Pizzerav2.model.Ordine;
import com.example.Pizzerav2.model.Pizza;
import com.github.javafaker.Faker;
import org.springframework.context.annotation.*;

import java.util.Locale;

@Configuration

public class PizzeriaConfig {

    @Bean
    @Scope("prototype")
    public Pizza createpizzac(){
        return new Pizza();
    }
    @Bean(name = "drinkgener")
    @Scope("prototype")
    public Drink createdrink(){
        return new Drink();
    }

    @Bean(name = "salsiccia")
    @Scope("singleton")
    public static Pizza salsiccia() {

        Pizza pizza = new Pizza();
        pizza.setNome("Salsiccia");
        pizza.setPrezzo(5.99);
        pizza.addIngred("cheese");
        pizza.addIngred("salsa");
        pizza.addIngred("salsiccia");
        return pizza;
    }
    @Bean(name = "margeritta")
    @Scope("singleton")
    public static Pizza margeritta() {

        Pizza pizza = new Pizza();

    pizza.setNome("Margeritta");
    pizza.setPrezzo(4.99);
        pizza.addIngred("cheese");
        pizza.addIngred("salsa");
        return pizza;
    }

    @Bean(name = "salsicciap")
    @Scope("singleton")
    public static Pizza salsicciap() {

        Pizza pizza = new Pizza();
        pizza.setNome("Salsiccia piccante");
        pizza.setPrezzo(6.49);
        pizza.addIngred("cheese");
        pizza.addIngred("salsa");
        pizza.addIngred("salsiccia");
        pizza.addIngred("peperoni piccante");
        return pizza;
    }
    @Bean(name = "coca")
    @Scope("singleton")
    public static Drink coca() {
        Drink drink = new Drink();
        drink.setNome("Coca-cola");
        drink.setPrezzo(2.00);
        drink.setGradazione(0);

        return drink;
    }
    @Bean(name = "birra")
    @Scope("singleton")
    public static Drink birra() {
        Drink drink=new Drink();
        drink.setNome("Peroni");
        drink.setPrezzo(2.99);
        drink.setGradazione(5);
        return drink;

    }
    @Bean
    @Scope("prototype")
    public Cliente creaFakeCliente() {
        Faker fake = new Faker(Locale.forLanguageTag("it-IT"));

        return new Cliente(fake.name().firstName(),fake.name().fullName(), fake.phoneNumber().cellPhone());
    }

    @Bean
    @Scope("prototype")
    public Cliente createCustomCliente() {


        return new Cliente();
    }

    @Bean
    @Scope("prototype")
    public Ordine creaOrdine1() {
        return new Ordine();
    }


















}
