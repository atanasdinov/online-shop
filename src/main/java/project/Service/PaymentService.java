package project.Service;

import project.model.Payment;

import java.util.List;

public interface PaymentService {
    void add(Payment payment);
    List<Payment> listPayments();
}
