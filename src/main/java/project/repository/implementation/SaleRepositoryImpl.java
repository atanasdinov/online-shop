package project.repository.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.model.entities.Sale;
import project.repository.specification.SaleRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class SaleRepositoryImpl implements SaleRepository {

    @PersistenceContext
    @Autowired
    private EntityManager em;

    @Override
    public void persist(String username, String productName, Double price, Integer productQuantity) {
        em.createNativeQuery("insert into sales (username, productName, price, quantity) values (:username, :productName, :price, :productQuantity)")
                .setParameter("username", username)
                .setParameter("productName", productName)
                .setParameter("price", price)
                .setParameter("productQuantity", productQuantity)
                .executeUpdate();

    }

    @Override
    public List<Sale> getAllSales() {
        return (List<Sale>) em.createNativeQuery("select * from sales", Sale.class).getResultList();
    }

}
