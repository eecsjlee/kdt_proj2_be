package com.kdt_proj2_be.service;

import com.kdt_proj2_be.persistence.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;


}
