package com.example.Pizzerav2.service;

import com.example.Pizzerav2.model.Cliente;
import com.example.Pizzerav2.model.Menu;
import com.example.Pizzerav2.model.Ordine;
import com.example.Pizzerav2.model.Prodotto;
import com.example.Pizzerav2.repository.MenuDAO;
import com.example.Pizzerav2.repository.OrdineDAO;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrdineService {

    // Utilizzato per generare dati casuali, come nomi di clienti o numeri di telefono
    private static Faker faker = new Faker();

    // Lista per memorizzare temporaneamente gli ordini (utile per test o operazioni locali)
    private List<Ordine> listaOrdini = new ArrayList<>();

    @Autowired
    OrdineDAO ordineDAO; // DAO per la gestione degli ordini nel database
    @Autowired
    MenuDAO menuDAO; // DAO per la gestione dei menu nel database
    @Autowired
    @Qualifier("creaOrdine1") // Provider per ottenere un oggetto Ordine configurato tramite Spring
    ObjectProvider<Ordine> ordineObjectProvider;

    @Autowired
    @Qualifier("creaFakeCliente") // Provider per generare clienti fittizi tramite Spring
    ObjectProvider<Cliente> clienteFakeObjectProvider;

    @Autowired
    @Qualifier("createCustomCliente") // Provider per generare clienti fittizi tramite Spring
    ObjectProvider<Cliente> CustomClienteObjectProvider;
    @Autowired
    private MenuService menuService; // Servizio per gestire i menu

    @Autowired
    private ObjectProvider<Menu> menuProvider; // Provider per ottenere oggetti Menu

    // Metodo per creare un nuovo ordine
    public Ordine creaOrdine(Cliente cliente, List<Prodotto> prodotti) {
        // Recupero un oggetto Ordine fornito da Spring (se disponibile)
        Ordine ord = ordineObjectProvider.getIfAvailable();

        // Calcolo del totale dell'ordine sommando i prezzi dei prodotti
        Double total = prodotti.stream().mapToDouble(Prodotto::getPrezzo).sum();

        // Imposto i dati del cliente, dei prodotti e del totale nell'oggetto ordine
        ord.setCliente(cliente);
        ord.setListaProdotti(prodotti);
        ord.setTotale(total);
        ord.setDataOrdine(LocalDate.now()); // Imposto la data corrente dell'ordine

        // Restituisco l'oggetto Ordine appena creato
        return ord;
    }

    // Metodo per recuperare un ordine dal database tramite il suo ID
    public Ordine getOrdineById(Long id) {
        return ordineDAO.findOrdineById(id);
    }
    // Metodo per creare n ordini casuali da un menu specifico
    public void creaNordini(int n, Long menuNum) {
        for (int i = 0; i < n; i++) {
            // Genero un numero casuale di prodotti (da 2 a 5)
            int numProd = faker.number().numberBetween(2, 5);

            // Creo un nuovo ordine con cliente casuale e prodotti casuali
            Ordine o = creaOrdine(creaFakeCliente(), getRandomProdotto(numProd, menuNum));

            // Salvo l'ordine nel database
            salvaOrdine(o);
        }
    }

    // Metodo per salvare un ordine nel database
    public void salvaOrdine(Ordine ordine) {
        ordineDAO.saveOrdine(ordine);
    }
    public void removeOrdine(Integer id) {
        ordineDAO.deleteOrdine(id);
    }
    // Metodo per stampare tutti gli ordini
    public void stampaordini() {
        // Recupero gli ordini dal DAO e li stampo uno ad uno
        // Stampa dell'oggetto Ordine
        System.out.println("********************************" +
                "Lista ordini:********************************");

        ordineDAO.getListaOrdini().forEach(System.out::println);
        // listaOrdini.forEach(ordine -> { // System.out.printf("Ordine ID: %d | Cliente: %s | Prodotti: %s | Totale: %.2f€%n", // ordine.getNumeroOrdine(), // ordine.getCliente().getNome(), // ordine.getListaProdotti().stream() // .map(p -> p.getNome() + "(" + p.getPrezzo() + "€)") // .collect(Collectors.joining(", ")), // ordine.getTotale() // ); // });
        System.out.println("******************************************************************************"
                );
    }



    public void modificaOrdine(Ordine ordine) {
        ordineDAO.updateOrdine(ordine);
        System.out.println("Ordine"+ordine.getNumeroOrdine()+" modificato");
    }
    public void saveCliente(Cliente cliente) {

        ordineDAO.salvaCliente(cliente);

    }
    // Metodo per generare un cliente
    public Cliente getCustomCliente(String nome,String cognome,String telefono) {
        // Ottengo un cliente casuale tramite Spring e lo salvo nel database
        Cliente client = CustomClienteObjectProvider.getIfAvailable();
        client.setNome(nome);
        client.setCognome(cognome);
        client.setTelefono(telefono);
        ordineDAO.salvaCliente(client);
        return client;
    }
    // Metodo per generare un cliente fittizio
    public Cliente creaFakeCliente() {
        // Ottengo un cliente casuale tramite Spring e lo salvo nel database
        Cliente client = clienteFakeObjectProvider.getIfAvailable();
        ordineDAO.salvaCliente(client);
        return client;
    }
    public void removeClient(Long id) {
        ordineDAO.removeCliente(ordineDAO.getCliente(id));
    }
    public Cliente getClienteById(Long id) {
        return ordineDAO.getCliente(id);
    }
    public void modificaCliente(Cliente cliente) {
        ordineDAO.modificaCliente(cliente);
    }
    public List<Cliente> getListaCliente() {
        List<Cliente> allClients = new ArrayList<>();
        allClients=ordineDAO.getListaClienti();
        return allClients;
    }
    public void stampaListaCliente() {
        System.out.println("********************************" +
                "Lista Clienti:********************************");
        this.getListaCliente().forEach(System.out::println);
        System.out.println("******************************************************************************"
        );
    }

    // Metodo per ottenere una lista di prodotti casuali da un menu specifico
    public List<Prodotto> getRandomProdotto(int n, Long menuNum) {
        List<Prodotto> allProducts = new ArrayList<>();

        // Aggiungo la lista delle bevande del menu specificato
        allProducts.addAll(menuDAO.findMenuById(menuNum).getDrinkList());

        // Aggiungo la lista delle pizze del menu specificato
        allProducts.addAll(menuDAO.findMenuById(menuNum).getPizzaList());

        // Mischio casualmente la lista di prodotti
        Collections.shuffle(allProducts);

        // Restituisco solo i primi n prodotti dalla lista mescolata
        return allProducts.subList(0, Math.min(n, allProducts.size()));
    }


}

