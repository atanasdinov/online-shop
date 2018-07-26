package project.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.model.entities.Cart;
import project.model.entities.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Repository
public class CartRepositoryImpl implements CartRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public CartRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void persist() {
        Cart cart = new Cart();
        cart.setShipmentPrice(0.0);
        cart.setTotalPrice(0.0);
        em.persist(cart);
    }

    @Override
    @Transactional
    public void addProduct(long cartId, Product product) {
        em.createNativeQuery("update carts c set c.totalPrice=c.totalPrice+:productPrice where c.id=:cartId", Cart.class)
                .setParameter("productPrice", product.getPrice())
                .setParameter("cartId", cartId)
                .executeUpdate();

        em.createNativeQuery("insert into products_carts(products_Id, cart_Id) values (:productId, :cartId)", Cart.class)
                .setParameter("productId", product.getId())
                .setParameter("cartId", cartId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void removeProduct(long cartId, Product product) {
        em.createNativeQuery("update carts c set c.totalPrice=c.totalPrice-:productPrice where c.id=:cartId", Cart.class)
                .setParameter("productPrice", product.getPrice())
                .setParameter("cartId", cartId)
                .executeUpdate();

        em.createNativeQuery("delete from products_carts where cart_id=:cartId and products_id=:productId", Cart.class)
                .setParameter("productId", product.getId())
                .setParameter("cartId", cartId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void removeAllProducts(long cartId) {
        em.createNativeQuery("delete from products_carts where cart_id=:cartId", Cart.class)
                .setParameter("cartId", cartId)
                .executeUpdate();
        em.createNativeQuery("update carts c set c.totalPrice=:productPrice where c.id=:cartId", Cart.class)
                .setParameter("productPrice", 0)
                .setParameter("cartId", cartId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public List<Product> getProducts(long cartId) {
        return (List<Product>) em.createNativeQuery("select * from products p left join products_carts pc on p.id=pc.products_id where pc.cart_id=:cartId", Product.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

    @Override
    @Transactional
    public boolean doesExist(long cartId) {
//        return em.createNativeQuery("select * from carts c where c.id=:cartId", Cart.class)
//                .setParameter("cartId", cartId)
//                .getResultList().size() != 0;
        return !(em.createNativeQuery("select * from carts c where c.id=:cartId", Cart.class)
                .setParameter("cartId", cartId)
                .getResultList()
                .isEmpty());
    }

    @Override
    public Cart getCart(long cartId) {
        return (Cart) em.createNativeQuery("select * from carts c where c.id=:cartId", Cart.class)
                .setParameter("cartId",cartId).getSingleResult();
    }

    @Override
    public List<Cart> all() {
        return (List<Cart>) em.createNativeQuery("select * from carts c", Cart.class)
                .getResultList();
    }

    @Override
    public double getTotalPrice(long cartId) {
        return (Double) em.createNativeQuery("select totalPrice from carts c where c.id=:cartId")
                .setParameter("cartId",cartId)
                .getSingleResult();
    }

    @Override
    public boolean doesProductExist(long cartId, Product product) {
        return !(em.createNativeQuery("select * from products_carts where products_id=:productId and cart_id=:cartId")
                .setParameter("cartId", cartId)
                .setParameter("productId", product.getId())
                .getResultList()
                .isEmpty());
    }



}