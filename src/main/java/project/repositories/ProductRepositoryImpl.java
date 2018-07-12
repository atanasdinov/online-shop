package project.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.model.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    @PersistenceContext
    @Autowired
    private EntityManager em;

//    @Autowired
//    public ProductRepositoryImpl(EntityManager em) {
//        this.em = em;
//    }

    @Override
    @Transactional
    public void add(Product product) {
        em.persist(product);
    }

    @Override
    @Transactional
    public Product get(String name) {

        TypedQuery<Product> typedQuery = em
                .createQuery("select p from Product p where p.name=:name", Product.class)
                .setParameter("name",name);
        return typedQuery.getSingleResult();
    }

    @Override
    @Transactional
    public List<Product> all() {
        return (List<Product>) em.createQuery("select p from Product p");
    }
}
