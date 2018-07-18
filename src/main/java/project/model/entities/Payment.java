package project.model.entities;

public class Payment {

    private String cardIBAN;
    private String cash;

    public Payment() {
    }

    public Payment(String cardIBAN, String cash) {
        this.cardIBAN = cardIBAN;
        this.cash = cash;
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
