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
    public void addProduct(int cartId, Product product) {
        em.createNativeQuery("update carts c set c.totalPrice=c.totalPrice+:productPrice where c.id=:cartId", Cart.class)
                .setParameter("productPrice", product.getPrice())
                .setParameter("cartId", cartId)
                .executeUpdate();

        em.createNativeQuery("update products p set p.cart_id=:cartId, p.quantity=p.quantity-1 where p.id=:productId", Product.class)
                .setParameter("productId", product.getId())
                .setParameter("cartId", cartId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void removeProduct(int cartId, Product product) {
        em.createNativeQuery("update carts c set c.totalPrice=c.totalPrice-:productPrice where c.id=:cartId", Cart.class)
                .setParameter("productPrice", product.getPrice())
                .setParameter("cartId", cartId)
                .executeUpdate();

        em.createNativeQuery("update products p set p.cart_id=null, p.quantity=p.quantity+1 where p.id=:productId", Product.class)
                .setParameter("productId", product.getId())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void removeAllProducts(int cartId) {
        em.createNativeQuery("update products p set p.cart_id=null", Product.class)
                .executeUpdate();
    }

    @Override
    @Transactional
    public List<Product> getProducts(int cartId) {
        return (List<Product>) em.createNativeQuery("select * from products p where p.cart_id=:cartId", Product.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

    @Override
    @Transactional
    public boolean doesExist(int cartId) {
//        return em.createNativeQuery("select * from carts c where c.id=:cartId", Cart.class)
//                .setParameter("cartId", cartId)
//                .getResultList().size() != 0;
        return !(em.createNativeQuery("select * from carts c where c.id=:cartId", Cart.class)
                .setParameter("cartId", cartId)
                .getResultList()
                .isEmpty());
    }

}
