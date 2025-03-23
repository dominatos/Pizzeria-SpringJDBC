package com.example.Pizzerav2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ordine {

    private Integer numeroOrdine;
    private Cliente cliente;
    private List<Prodotto> listaProdotti;
    private LocalDate dataOrdine;
    private Double totale;

    @Override
    public String toString() {
        return String.format(
                "OrdineID: %d | Cliente: %s | Prodotti: %s | Totale: %.2f€",
                numeroOrdine,
                cliente.getNome()+" "+ cliente.getCognome(),
                listaProdotti.stream()
                        .map(p -> String.format("%s (%.2f€)", p.getNome(), p.getPrezzo()))
                        .collect(Collectors.joining(", ")),
                totale
        );
    }
}
