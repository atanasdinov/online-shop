package com.scalefocus.shop.repository.implementation;

import com.scalefocus.shop.model.entity.Product;
import com.scalefocus.shop.model.entity.Sale;
import com.scalefocus.shop.repository.specification.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class SaleRepositoryImpl implements SaleRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public SaleRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void addSale(String username, Product product) {
        em.createNativeQuery("insert into sales (username, product_name, quantity, price, total_price) values (:username, :productName, :quantity, :price, :totalPrice)")
                .setParameter("username", username)
                .setParameter("productName", product.getName())
                .setParameter("quantity", product.getRequestedQuantity())
                .setParameter("price", product.getPrice())
                .setParameter("totalPrice", product.getPrice() * product.getRequestedQuantity())
                .executeUpdate();
    }

    @Override
    public List<Sale> getAllSales() {
        return (List<Sale>) em.createNativeQuery("select * from sales", Sale.class)
                .getResultList();
    }
}
