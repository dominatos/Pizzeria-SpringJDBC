package com.example.Pizzerav2.model;

import lombok.*;

import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Builder
public class Cliente {
    private Long id;
    private String nome;
    private String cognome;
    private String telefono;

    public Cliente(String nome, String cognome, String telefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;

    }
    @Override
    public String toString() {
        return String.format(
                "Client ID: %d | Nome: %s | Cognome: %s | TEL.: %s",
                id,
                nome,
                cognome,
                telefono
        );
    }
}
