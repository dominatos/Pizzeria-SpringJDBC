package com.example.Pizzerav2.repository;

import com.example.Pizzerav2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrdineDAO implements OrdineDaoRepository {
    @Autowired
    JdbcTemplate jdbc; // Oggetto JdbcTemplate per la connessione e operazioni sul database
    @Autowired
    MenuDAO menuDAO; // DAO per la gestione dei menu e dei prodotti

    // Salvataggio di un nuovo ordine nel database
    @Override
    public Ordine saveOrdine(Ordine ordine) {
        String sql = "INSERT INTO ordini (id_cliente, dataOrdine, totale) values(?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Inserimento dell'ordine nel database e recupero dell'ID generato
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id_ordine" });
            ps.setLong(1, ordine.getCliente().getId()); // ID del cliente associato
            ps.setString(2, ordine.getDataOrdine().toString()); // Data dell'ordine
            ps.setDouble(3, ordine.getTotale()); // Totale dell'ordine
            return ps;
        }, keyHolder);

        Long ordineId = keyHolder.getKey().longValue(); // Recupero dell'ID dell'ordine

        // Salvataggio dei prodotti associati all'ordine
        String sql_p = "INSERT INTO prodotti_ordini (id_ordine, id_prodotto) VALUES (?, ?)";
        ordine.getListaProdotti().forEach(p -> {
            jdbc.update(sql_p, ordineId, p.getId()); // Associazione prodotto-ordine
        });

        //System.out.println("Ordine " + ordine.getCliente().getNome() + " salvato nel DB! ID: " + ordineId);
        return ordine; // Restituzione dell'ordine salvato
    }

    // Recupero di un ordine dal database tramite il suo ID
    @Override
    public Ordine findOrdineById(Long id) {
        String ordineSql = "SELECT * FROM ordini WHERE id_ordine = ?";
        Ordine ordine = jdbc.queryForObject(ordineSql, new OrdineRowMaapper(), id); // Recupero dell'ordine

        // Recupero del cliente associato all'ordine
        Cliente clientedaordine = getCliente(getidCliente(id));
        ordine.setCliente(clientedaordine);

        // Recupero degli ID dei prodotti associati all'ordine
        String sql = "SELECT id_prodotto FROM prodotti_ordini WHERE id_ordine = ?";
        List<Long> prodottoIds = jdbc.query(sql, new ProdottiInOrdiniRowMapper(), id);

        // Creazione della lista di prodotti
        List<Prodotto> prodottoList = new ArrayList<>();
        prodottoIds.forEach(l -> {
            prodottoList.add(menuDAO.findProdottoById(l)); // Recupero del prodotto tramite il suo ID
            ordine.setListaProdotti(prodottoList); // Aggiunta alla lista dei prodotti
        });

        return ordine; // Restituzione dell'ordine completo
    }
    // Metodo per eliminare un ordine
    @Override
    public void deleteOrdine(Integer id) {
        String checkSql = "SELECT COUNT(*) FROM ordini WHERE id_ordine = ?";
        Integer count = jdbc.queryForObject(checkSql, Integer.class, id);

        if (count == 0) {
            System.out.println("Ordine ID: " + id +" non esiste!.");
            return;
        } else {
            System.out.println("Ordine " + id + " e trovato e viene eliminato nel DB!");
        }
        String sql = "DELETE FROM prodotti_ordini WHERE id_ordine = ?";
        jdbc.update(sql, id);
        sql = "DELETE FROM ordini WHERE id_ordine = ?";
        jdbc.update(sql, id);
        checkSql = "SELECT COUNT(*) FROM ordini WHERE id_ordine = ?";
        count = jdbc.queryForObject(checkSql, Integer.class, id);
        if (count == 0) {
            System.out.println("Ordine ID: " + id +" eliminato nel DB!");

        }


    }
    // Metodo per aggiornare un ordine
    @Override
    public void updateOrdine(Ordine ordine) {
        String sql = "UPDATE ordini SET id_cliente = ?, dataOrdine = ?, totale = ? WHERE id_ordine = ?";
        jdbc.update(sql, ordine.getCliente().getId(), ordine.getDataOrdine().toString(), ordine.getTotale(), ordine.getNumeroOrdine());
        sql ="Delete from prodotti_ordini where id_ordine = ?";
        jdbc.update(sql, ordine.getNumeroOrdine());
        String sql_p = "INSERT INTO prodotti_ordini (id_ordine, id_prodotto) VALUES (?, ?)";
        ordine.getListaProdotti().forEach(p -> {
            jdbc.update(sql_p,  ordine.getNumeroOrdine(),p.getId()); // Associazione prodotto-ordine
        });

        // System.out.println("Ordine " + ordine.getCliente().getNome() + " salvato nel DB! ID: " + ordine.getNumeroOrdine());
    }
    // Recupero della lista di tutti gli ordini dal database
    public List<Ordine> getListaOrdini() {
        String sql = "SELECT o.id_ordine, o.dataOrdine, o.totale, c.id_cliente, c.nome_cliente,c.cognome, c.telefono_cliente, po.id_prodotto " +
                "FROM ordini o " +
                "JOIN clienti c ON o.id_cliente = c.id_cliente " +
                "JOIN prodotti_ordini po ON o.id_ordine = po.id_ordine " +
                "ORDER BY o.id_ordine";

        List<Ordine> ordini = jdbc.query(sql, rs -> {
            Map<Long, Ordine> ordineMap = new HashMap<>();

            while (rs.next()) {
                Long idOrdine = rs.getLong("id_ordine");
                Ordine ordine = ordineMap.get(idOrdine);

                if (ordine == null) {
                    ordine = new Ordine();
                    ordine.setNumeroOrdine(rs.getInt("id_ordine"));
                    ordine.setDataOrdine(rs.getTimestamp("dataOrdine").toLocalDateTime().toLocalDate());
                    ordine.setTotale(rs.getDouble("totale"));

                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getLong("id_cliente"));
                    cliente.setNome(rs.getString("nome_cliente"));
                    cliente.setCognome(rs.getString("cognome"));
                    cliente.setTelefono(rs.getString("telefono_cliente"));
                    ordine.setCliente(cliente);

                    ordine.setListaProdotti(new ArrayList<>());
                    ordineMap.put(idOrdine, ordine);
                }

                Long idProdotto = rs.getLong("id_prodotto");
                if (idProdotto != null) {
                    Prodotto prodotto = menuDAO.findProdottoById(idProdotto);
                    if (prodotto != null) {
                        ordine.getListaProdotti().add(prodotto);
                    }
                }
            }

            return new ArrayList<>(ordineMap.values());
        });

        return ordini;
    }






    // Salvataggio di un cliente nel database
    @Override
    public void salvaCliente(Cliente cliente) {
        String sql = "INSERT INTO clienti (nome_cliente, cognome, telefono_cliente) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Inserimento del cliente nel database e recupero dell'ID generato
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id_cliente" });
            ps.setString(1, cliente.getNome()); // Nome del cliente
            ps.setString(2, cliente.getCognome()); // Cognome del cliente
            ps.setString(3, cliente.getTelefono()); // Numero di telefono
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue(); // Recupero dell'ID del cliente
        cliente.setId(generatedId); // Impostazione dell'ID nel cliente
    }
    @Override
    public void modificaCliente(Cliente cliente)
    {
        String sql = "Update clienti Set nome_cliente=?, cognome=?, telefono_cliente=? where id_cliente=?";
        jdbc.update(sql, cliente.getNome(),cliente.getCognome(), cliente.getTelefono(), cliente.getId());
        System.out.println("Cliente " + cliente.getNome() + " modificato nel DB!");

    }

    // Recupero di un cliente tramite il suo ID
    @Override
    public Cliente getCliente(Long id) {
        String sql = "SELECT * FROM clienti WHERE id_cliente = ?";
        return jdbc.queryForObject(sql, new ClienteRowMapper(), id); // Restituzione del cliente
    }

    // Recupero dell'ID del cliente associato all'ordine
    public Long getidCliente(Long id) {
        String sql = "SELECT id_cliente FROM ordini WHERE id_ordine = ?";
        return jdbc.queryForObject(sql, Long.class, id);
    }

    // Aggiornamento dei dati di un cliente
    @Override
    public void editCliente(Cliente cliente) {
        String sql = "UPDATE clienti SET nome_cliente = ?, cognome = ?, telefono_cliente = ? WHERE id_cliente = ?";
        jdbc.update(sql, cliente.getNome(), cliente.getCognome(), cliente.getTelefono(), cliente.getId());
    }

    // Eliminazione di un cliente tramite il suo ID
    @Override
    public void removeCliente(Cliente cliente) {
        String sql = "DELETE FROM clienti WHERE id_cliente = ?";
        jdbc.update(sql, cliente.getId());
    }

    // Recupero della lista di tutti i clienti dal database
    public List<Cliente> getListaClienti() {
        String sql = "SELECT * FROM clienti";
        return jdbc.query(sql, new ClienteRowMapper());
    }


    //    public Long getidCliente(Long id){
//        String sql = "SELECT * FROM ordini WHERE id_ordine = ?";
//        Ordine ordine = jdbc.queryForObject(sql, new OrdineRowMaapper(), id);
//        id=ordine.getCliente().getId();
//
//        return id;
//
//    }




}
