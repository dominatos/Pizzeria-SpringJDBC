package com.example.Pizzerav2.repository;

import com.example.Pizzerav2.model.Cliente;
import com.example.Pizzerav2.model.Ordine;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ClienteRowMapper implements RowMapper<Cliente> {
    @Override
    public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id_cliente"));
        cliente.setNome(rs.getString("nome_cliente"));
        cliente.setCognome(rs.getString("cognome"));
        cliente.setTelefono(rs.getString("telefono_cliente"));

        return cliente;
    }
}
