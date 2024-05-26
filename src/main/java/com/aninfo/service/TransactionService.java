package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.exceptions.AccountNotFoundException;
import com.aninfo.exceptions.TransactionNotFoundException;
import com.aninfo.exceptions.InvalidTransactionTypeException;
import com.aninfo.model.Account;
import com.aninfo.repository.AccountRepository;

import com.aninfo.model.Transaction;
import com.aninfo.model.TransactionType;
import com.aninfo.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;
 

    public Collection<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }



    public Collection<Transaction> getTransactionsByAccount(Long cbu) {
        Account fromAccount = accountRepository.findAccountByCbu(cbu);
        return transactionRepository.findByFromAccount(fromAccount);
    }
    
    @Transactional
    public Transaction createTransaction(Long from_account, Long to_account, Double amount, TransactionType type) {
        
        Account fromAccount = accountRepository.findAccountByCbu(from_account);
        Account toAccount = accountRepository.findAccountByCbu(to_account);
        
        if (type == TransactionType.WITHDRAWAL) {
            // if (to_account.getBalance() < transaction.getAmount()) {
            //     throw new InsufficientFundsException("ToAccount has insufficient funds to make a withdrawal");
            // }
            System.out.println("es withdraal");
            accountService.withdraw(to_account, amount); // todo falla si no tiene suficiente plata
            accountService.deposit(from_account, amount);
        } else if (type  == TransactionType.DEPOSIT) {
            // if (from_account.getBalance() < transaction.getAmount()){
            //     throw new InsufficientFundsException("FromAccount has insufficient funds to make a deposit");
            // }

          
            accountService.withdraw(from_account, amount);
            System.out.println("withdraw done");
            accountService.deposit(to_account, amount);
            System.out.println("deposite done");
        }
        else {
            throw new InvalidTransactionTypeException("Type is not valid");
        }

        Transaction transaction = new Transaction(toAccount, fromAccount, amount, type);
        //Transaction(Account to_account, Account from_account, Double amount, TransactionType type) 
        return transactionRepository.save(transaction); 
    }


    @Transactional
    public void deleteById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException("Could not find transaction"));
        if (transaction.getType() == TransactionType.WITHDRAWAL){
            accountService.withdraw(transaction.getFromAccountCbu(), transaction.getAmount());
            accountService.deposit(transaction.getToAccountCbu(), transaction.getAmount());  
        }
        else {
            accountService.withdraw(transaction.getToAccountCbu(), transaction.getAmount());
            accountService.deposit(transaction.getFromAccountCbu(), transaction.getAmount());
        }

        transactionRepository.deleteById(id);
    }


// Se debe agregar la capacidad de crear transacciones de ambos tipos (extracción y depósito), 
// leer todas las transacciones dada una cuenta existente, leer una transacción 
// en particular y, por último, poder eliminar una transacción con su correspondiente rollback.!
   

}
