package com.example.Pizzerav2.repository;

import com.example.Pizzerav2.model.Menu;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuRowMapper implements RowMapper<Menu> {
    @Override
    public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
        Menu menu = new Menu();
        menu.setId(rs.getLong("id_menu"));
        menu.setNome(rs.getString("menu_name"));
        return menu;
    }
}
