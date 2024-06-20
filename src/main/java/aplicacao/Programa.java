package aplicacao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


import dominio.Pessoa;

public class Programa {

    public static void main(String[] args) {

        //como o proprio banco de dados vai incrementas as Id's, instanciamos como null
        Pessoa p1 = new Pessoa(null, "Carlos da Silva", "carlos@gmail.com");
        Pessoa p2 = new Pessoa(null, "Joaquim Torres", "joaquim@gmail.com");
        Pessoa p3 = new Pessoa(null, "Ana Maria", "ana@gmail.com");

        //passa a persistence name do xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");

        //aqui, automaticamente teremos uma conexão com banco de dados
        EntityManager em = emf.createEntityManager();

        //iniciando transacao com banco de dados
        em.getTransaction().begin();

        //codigo utilizado para salvar os objetos no banco de dados
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);

        //buscando objeto pelo ID
        Pessoa pFind = em.find(Pessoa.class, 2);
        System.out.println(pFind);

        //confirmando alterações do banco de dados
        em.getTransaction().commit();

        System.out.println("Pronto!");

        //fechando o entitymanager e factory
        em.close();
        emf.close();
    }
}