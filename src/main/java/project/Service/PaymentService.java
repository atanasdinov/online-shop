package project.Service;

import project.model.entities.Payment;

import java.util.List;

public interface PaymentService {
    void add(Payment payment);
    List<Payment> listPayments();
}
