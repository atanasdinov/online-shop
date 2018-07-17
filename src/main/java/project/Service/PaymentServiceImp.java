package project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import project.Dao.PaymentDao;
import project.model.entities.Payment;

import java.util.List;

public class PaymentServiceImp implements PaymentService{

    @Autowired
    private PaymentDao personDao;

    @Transactional
    @Override
    public void add(Payment payment) {
        personDao.add(payment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Payment> listPayments() {
        return personDao.listPayments();
    }
}
