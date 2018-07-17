package project.Dao;

import org.springframework.stereotype.Repository;
import project.model.entities.Payment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PaymentDaoImp implements PaymentDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void add(Payment payment) {
        em.persist(payment);
    }

    @Override
    public List<Payment> listPayments() {
        javax.persistence.criteria.CriteriaQuery<Payment> criteriaQuery = em.getCriteriaBuilder().createQuery(Payment.class);
        @SuppressWarnings("unused")
        Root<Payment> root = criteriaQuery.from(Payment.class);
        return em.createQuery(criteriaQuery).getResultList();
    }
}
