package project.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.model.entities.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public ProductRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void persist(Product product) {
        em.createNativeQuery("insert into products (name,price,quantity,rating,category_id) values (:name,:price,:quantity,:rating,:categoryId)")
                .setParameter("name", product.getName())
                .setParameter("price", product.getPrice())
                .setParameter("quantity", product.getQuantity())
                .setParameter("rating", product.getRating())
                .setParameter("categoryId", product.getCategory().getId());
    }

    @Override
    @Transactional
    public void delete(long id) {
        em.createNativeQuery("delete from products where id=:id", Product.class)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    @Transactional
    public Product get(long id) {
        return (Product) em
                .createNativeQuery("select * from products where id=:id", Product.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    @Transactional
    public List<Product> all() {
        return (List<Product>) em.createNativeQuery("select * from products", Product.class).getResultList();
    }

    @Override
    @Transactional
    public List<Product> allByCategory(String categoryName) {
        return (List<Product>) em
                .createNativeQuery("SELECT * FROM products as p left join categories as c on p.category_id=c.id where c.name=:name", Product.class)
                .setParameter("name", categoryName).getResultList();
    }

}