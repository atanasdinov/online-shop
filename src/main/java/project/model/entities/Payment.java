package project.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "PAYMENTS")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CARD_IBAN")
    private String cardIBAN;

    @Column(name = "CASH")
    private String cash;

    public Payment() {}

    public Payment(String cardIBAN, String cash) {
        this.cardIBAN = cardIBAN;
        this.cash = cash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardIBAN() {
        return cardIBAN;
    }

    public void setCardIBAN(String cardIBAN) {
        this.cardIBAN = cardIBAN;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }
}
