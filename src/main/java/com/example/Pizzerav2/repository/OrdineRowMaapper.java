package com.example.Pizzerav2.repository;

import com.example.Pizzerav2.model.Cliente;
import com.example.Pizzerav2.model.Menu;
import com.example.Pizzerav2.model.Ordine;
import com.example.Pizzerav2.service.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class OrdineRowMaapper implements RowMapper<Ordine> {
    @Override
    public Ordine mapRow(ResultSet rs, int rowNum) throws SQLException {
        Ordine ordine = new Ordine();
        ordine.setNumeroOrdine(rs.getInt("id_ordine"));
        ordine.setDataOrdine(rs.getTimestamp("dataOrdine").toLocalDateTime().toLocalDate());
        ordine.setTotale(rs.getDouble("totale"));

        // Заполняем объект Cliente
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id_cliente")); // Убедитесь, что "id_cliente" присутствует в запросе SELECT
        ordine.setCliente(cliente);

        return ordine;
    }
//    public Ordine mapRow(ResultSet rs, int rowNum) throws SQLException {
//        Ordine ordine = new Ordine();
//        ordine.setNumeroOrdine(rs.getInt("id_ordine"));
//        ordine.setDataOrdine(rs.getTimestamp("dataOrdine").toLocalDateTime().toLocalDate());
//        ordine.setTotale(rs.getDouble("totale"));
//        ordine.setCliente(new Cliente());
//        return ordine;
//    }
}

