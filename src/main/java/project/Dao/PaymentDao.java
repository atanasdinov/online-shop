package project.Dao;

import project.model.entities.Payment;

import java.util.List;

public interface PaymentDao {
    void add(Payment payment);
    List<Payment> listPayments();
}

