package com.example.Pizzerav2.repository;

import com.example.Pizzerav2.model.Cliente;
import com.example.Pizzerav2.model.Ordine;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdineDaoRepository {
    //Crud Ordine
    public Ordine saveOrdine(Ordine ordine);

    Ordine findOrdineById(Long id);





    void deleteOrdine(Integer id);

    public void updateOrdine(Ordine ordine);

    void salvaCliente(Cliente cliente);

    void modificaCliente(Cliente cliente);

    Cliente getCliente(Long id);

    void editCliente(Cliente cliente);

    void removeCliente(Cliente cliente);
}
