package com.aninfo.model;

import javax.persistence.*;
@Entity
@SequenceGenerator(name="transaction_seq", sequenceName="transaction_seq", allocationSize=1)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    private Long id;


    private Double amount;


    @ManyToOne
    @JoinColumn(name = "from_account", nullable = false)
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account", nullable = false)
    private Account toAccount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public Transaction(){
    }

    // Constructor personalizado
    public Transaction(Account to_account, Account from_account, Double amount, TransactionType type) {
        this.amount = amount;
        this.type = type;
        this.toAccount = to_account;
        this.fromAccount = from_account;
       
    }
    public Long getFromAccountCbu() {
        return fromAccount.getCbu();
    }

    public Long getToAccountCbu() {
        return toAccount.getCbu();
    }

    public Double getAmount() {
        return amount;
    }

    // Getter para el tipo de transacción
    public TransactionType getType() {
        return type;
    }
    public Long getId() {
        return id;
    }
}

