package com.example.Pizzerav2.repository;

import com.example.Pizzerav2.model.Cliente;
import com.example.Pizzerav2.model.Ordine;
import com.example.Pizzerav2.model.Prodotto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class ProdottiInOrdiniRowMapper implements RowMapper<Long> {
    @Override
    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("id_prodotto");
    }
}