package com.example.Pizzerav2.service;

import com.example.Pizzerav2.model.Cliente;
import com.example.Pizzerav2.model.Menu;
import com.example.Pizzerav2.model.Ordine;
import com.example.Pizzerav2.model.Prodotto;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdineService {

    private static Faker faker = new Faker();
    private List<Ordine> listaOrdini = new ArrayList<Ordine>();
    private static int numOrdine = 1;
    @Autowired @Qualifier("creaOrdine1")    ObjectProvider <Ordine> ordineObjectProvider;
    @Autowired @Qualifier("creaFakeCliente") ObjectProvider <Cliente> clienteFakeObjectProvider;
    @Autowired    private MenuService menuService;
    @Autowired    private ObjectProvider<Menu> menuProvider;
    public Ordine creaOrdine(Cliente cliente,List<Prodotto> prodotti){
        Ordine ord = ordineObjectProvider.getIfAvailable();
        //Cliente fakeCliente = clienteFakeObjectProvider.getIfAvailable();
        Double total=0.0;
        total=prodotti.stream().mapToDouble(Prodotto::getPrezzo).sum();
        //Ordine ord=new Ordine();
        assert ord != null;
        ord.setCliente(cliente);
                ord.setListaProdotti(prodotti);
                ord.setTotale(total);
                ord.setDataOrdine(LocalDate.now());
                ord.setNumeroOrdine(numOrdine);

                listaOrdini.add(ord);
                numOrdine++;


                return ord;
    }
//    public void stampaordini(){
//        listaOrdini.forEach(System.out::println);
//    }
    public void stampaordini() {
        listaOrdini.forEach(ordine -> {
            System.out.printf("Ordine ID: %d | Cliente: %s | Prodotti: %s | Totale: %.2f€%n",
                    ordine.getNumeroOrdine(),
                    ordine.getCliente().getNome(),
                    ordine.getListaProdotti().stream()
                            .map(p -> p.getNome() + "(" + p.getPrezzo() + "€)")
                            .collect(Collectors.joining(", ")),
                    ordine.getTotale()
            );
        });
    }

    public Ordine findOrdine(Integer idOrdine) {
        return listaOrdini.stream()
                .filter(p -> p.getNumeroOrdine() == idOrdine)
                .findFirst()
                .orElse(null);
    }
    public Cliente getFakeCliente() {
        return clienteFakeObjectProvider.getObject();
    }



    public List<Prodotto> getRandomProdotto(int n) {
        // Получаем меню через провайдер
        Menu menu = menuProvider.getObject();

        // Создаем список всех продуктов
        List<Prodotto> allProducts = new ArrayList<>();

        // Добавляем все напитки
        allProducts.addAll(menu.getDrinkList());

        // Добавляем все пиццы
        allProducts.addAll(menu.getPizzaList());

        // Перемешиваем список
        Collections.shuffle(allProducts);

        // Возвращаем только n продуктов из перемешанного списка
        return allProducts.subList(0, Math.min(n, allProducts.size()));
    }
//    public List<Prodotto> getRandomProdotto(int n) {
//        List<Prodotto> allProducts = new ArrayList<>(
//                Arrays.asList(
//
//                        menuService.getDrinkbirra(),
//                        menuService.getDrinkCocaCola(),
//                        menuService.getPizzaMargerita(),
//                        menuService.getPizzasalsiccia(),
//                        menuService.getPizzasalsicciap(),
//                        menuService.createpizza("Gorgonzolla",List.of("base","gorgonzola","salsa"),9.00),
//                        menuService.createpizza("Quattro formaggi",List.of("base","gorgonzola","mozarella","elemental","parmeggiano reggiano"),9.00),
//                        menuService.drinkgener("VIno Bianco",12.00,10.00),
//                        menuService.drinkgener("Vino rosso",12.00,10.00),
//                        menuService.drinkgener("Caffe espresso",0.00,1.20),
//                        menuService.drinkgener("Te verde",0.00,1.20)
//                )
//        );
//
//        Collections.shuffle(allProducts);
//
//        // Возвращаем только n продуктов из перемешанного списка
//        return allProducts.subList(0, Math.min(n, allProducts.size()));
//    }
    public void creaNordini(int n)
    {
        for(int i=0;i<n;i++)
        {int numProd=faker.number().numberBetween(2,5);
            creaOrdine(getFakeCliente(),getRandomProdotto(numProd));


       }
    }
}
