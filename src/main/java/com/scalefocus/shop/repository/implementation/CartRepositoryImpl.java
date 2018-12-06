package com.scalefocus.shop.repository.implementation;

import com.scalefocus.shop.model.entity.Cart;
import com.scalefocus.shop.model.entity.Product;
import com.scalefocus.shop.repository.specification.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CartRepositoryImpl implements CartRepository {

    @PersistenceContext
    private EntityManager em;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CartRepositoryImpl(EntityManager em, JdbcTemplate jdbcTemplate) {
        this.em = em;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Cart createCart() {
        em.createNativeQuery("insert into carts(total_price) values (:totalPrice)", Cart.class)
                .setParameter("totalPrice", 0)
                .executeUpdate();

        return jdbcTemplate.queryForObject("select * from carts order by id desc limit 1", (resultSet, i) -> {
            Cart cart = new Cart();
            cart.setId(resultSet.getLong("id"));
            cart.setTotalPrice(resultSet.getDouble("total_price"));
            cart.setProducts(getAllProducts(cart.getId()));
            return cart;
        });
    }

    @Override
    public void addProduct(long cartId, Product product) {
        em.createNativeQuery("update carts set total_price=total_price+:productPrice where id=:cartId", Cart.class)
                .setParameter("productPrice", product.getPrice())
                .setParameter("cartId", cartId)
                .executeUpdate();

        em.createNativeQuery("insert into carts_products(product_id, cart_id) values (:productId, :cartId)", Cart.class)
                .setParameter("productId", product.getId())
                .setParameter("cartId", cartId)
                .executeUpdate();
    }

    @Override
    public void removeProduct(long cartId, Product product) {
        em.createNativeQuery("update carts set total_price=total_price-:productPrice where id=:cartId", Cart.class)
                .setParameter("productPrice", product.getPrice())
                .setParameter("cartId", cartId)
                .executeUpdate();

        em.createNativeQuery("delete from carts_products where cart_id=:cartId and product_id=:productId", Cart.class)
                .setParameter("productId", product.getId())
                .setParameter("cartId", cartId)
                .executeUpdate();
    }

    @Override
    public void removeAllProducts(long cartId) {
        em.createNativeQuery("delete from carts_products where cart_id=:cartId")
                .setParameter("cartId", cartId)
                .executeUpdate();

        em.createNativeQuery("update carts set total_price=:productPrice where id=:cartId", Cart.class)
                .setParameter("productPrice", 0)
                .setParameter("cartId", cartId)
                .executeUpdate();
    }

    @Override
    public List<Product> getAllProducts(long cartId) {
        return (List<Product>) em.createNativeQuery("select * from products left join carts_products on id=product_id where cart_id=:cartId", Product.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

    @Override
    public Cart getCart(long cartId) {
        return jdbcTemplate.queryForObject("select * from carts where id=?", new Object[]{cartId}, (resultSet, i) -> {
            Cart cart = new Cart();
            cart.setId(cartId);
            cart.setTotalPrice(resultSet.getDouble("total_price"));
            cart.setProducts(getAllProducts(cartId));
            return cart;
        });
    }

    @Override
    public boolean productExists(long cartId, long productId) {
        return !(em.createNativeQuery("select * from carts_products where product_id=:productId and cart_id=:cartId")
                .setParameter("cartId", cartId)
                .setParameter("productId", productId)
                .getResultList()
                .isEmpty());
    }
}