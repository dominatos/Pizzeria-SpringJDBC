package com.example.Pizzerav2.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBDAO {
    @Autowired
    JdbcTemplate jdbc;
    public void dropDB() {
        String sql = "DROP table IF EXISTS prodotti_ordini;";
        jdbc.execute(sql);
        sql = "DROP table IF EXISTS ordini;";
        jdbc.execute(sql);
        sql = "DROP table IF EXISTS clienti;";
        jdbc.execute(sql);


        sql = "DROP table IF EXISTS prodotti;";
        jdbc.execute(sql);
        sql = "DROP table IF EXISTS menu;";
        jdbc.execute(sql);
    }
    public void initilizeDB() {
//        String sql="CREATE DATABASE IF NOT EXISTS `saler007_jdbc` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;\n";
//        jdbc.update(sql);
        String sql=
                "CREATE TABLE IF NOT EXISTS `menu` (\n" +
                        "  `id_menu` int NOT NULL AUTO_INCREMENT,\n" +
                        "  `menu_name` varchar(150) NOT NULL,\n" +
                        "  \n" +
                        "  PRIMARY KEY (`id_menu`)\n" +
                        ");";
        jdbc.update(sql);
        sql="CREATE TABLE IF NOT EXISTS `prodotti` (\n" +
                "  `id_prodotto` int NOT NULL AUTO_INCREMENT,\n" +
                "  `id_menu` int NOT NULL,\n" +
                "  `ingredienti_pizza` varchar(500) DEFAULT NULL,\n" +
                "  `gradi_drink` double DEFAULT '0',\n" +
                "  `prodotto_name` varchar(150) NOT NULL,\n" +
                "  `tipo_prodotto` varchar(10) NOT NULL,\n" +
                "  `prezzo` double NOT NULL,\n" +
                "  PRIMARY KEY (`id_prodotto`),\n" +
                "  KEY `menu_prodotti` (`id_menu`),\n" +
                "  FOREIGN KEY (id_menu) REFERENCES menu(id_menu)\n" +
                ");";
        jdbc.update(sql);
        sql="CREATE TABLE IF NOT EXISTS clienti (\n" +
                "    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    nome_cliente VARCHAR(255) NOT NULL,\n" +
                "    cognome VARCHAR(255) NOT NULL,\n" +
                "    telefono_cliente VARCHAR(15) NOT NULL\n" +
                ");";
        jdbc.update(sql);
        sql="CREATE TABLE IF NOT EXISTS `ordini` (\n" +
                "  `id_ordine` int NOT NULL AUTO_INCREMENT,\n" +
                "  \n" +
                " \n" +
                "  `id_cliente` BIGINT NOT NULL,\n" +
                "  \n" +
                "  PRIMARY KEY (`id_ordine`),\n" +
                "  dataOrdine TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                "   totale DECIMAL(10, 2) NOT NULL,\n" +
                "  FOREIGN KEY (id_cliente) REFERENCES clienti(id_cliente)\n" +
                ") ;";
        jdbc.update(sql);
        sql="CREATE TABLE IF NOT EXISTS prodotti_ordini (\n" +
                "    id_ordine int NOT NULL,\n" +
                "    id_prodotto int NOT NULL,\n" +
                "    PRIMARY KEY (id_ordine, id_prodotto),\n" +
                "    FOREIGN KEY (id_ordine) REFERENCES ordini(id_ordine),\n" +
                "    FOREIGN KEY (id_prodotto) REFERENCES prodotti(id_prodotto)\n" +
                ");";
        jdbc.update(sql);
    }
}
