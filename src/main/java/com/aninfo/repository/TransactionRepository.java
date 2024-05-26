package com.aninfo.repository;

import com.aninfo.model.Transaction;
import com.aninfo.model.Account;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    Transaction findTransactionById(Long id);

    
    List<Transaction> findByFromAccount(Account from_account);

    @Override
    List<Transaction> findAll();

}
