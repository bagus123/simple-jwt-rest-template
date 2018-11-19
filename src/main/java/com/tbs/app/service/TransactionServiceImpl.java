package com.tbs.app.service;

import com.tbs.app.constants.HttpStatusCode;
import com.tbs.app.model.Account;
import com.tbs.app.model.Transaction;
import com.tbs.app.model.User;
import com.tbs.app.model.enums.TrxSide;
import com.tbs.app.model.enums.TrxType;
import com.tbs.app.payload.request.DepositRequest;
import com.tbs.app.payload.request.WithdrawalRequest;
import com.tbs.app.payload.response.ApiResponse;
import com.tbs.app.security.UserPrincipal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tbs.app.repository.AccountRepository;
import com.tbs.app.repository.TransactionRepository;

@Service("TransactionService")
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRep;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public ResponseEntity<?> deposit(DepositRequest body, UserPrincipal currentUser) {
        User user = userService.getCurrentUser(currentUser.getId());
        if (user == null) {
            return new ResponseEntity(new ApiResponse(false, HttpStatusCode.BAD_REQUEST.asText(), "bad request", null),
                    HttpStatus.BAD_REQUEST);
        }
        Transaction trx = new Transaction();
        trx.setAccount(user.getAcount());
        trx.setAmount(body.getAmount());
        trx.setRemark(body.getRemark());
        trx.setSide(TrxSide.CREDIT);
        trx.setType(TrxType.DEPOSIT);
        trx.setTrxDate(new Date());
        transactionRep.save(trx);

        List<Transaction> trxList = transactionRep.findAllByAccountId(user.getAcount().getId());
        Map<TrxSide, Double> mapSums = trxList.stream().collect(
                Collectors.groupingBy(Transaction::getSide, Collectors.summingDouble(Transaction::getAmount)));

        double debet = 0;
        double credit = 0;
        double balance = 0;

        if (mapSums.containsKey(TrxSide.DEBET)) {
            debet = mapSums.get(TrxSide.DEBET);
        }

        if (mapSums.containsKey(TrxSide.CREDIT)) {
            credit = mapSums.get(TrxSide.CREDIT);
        }

        balance = credit - debet;

        Account account = trx.getAccount();
        account.setBalance(balance);
        accountRepository.save(account);

        return ResponseEntity.ok(new ApiResponse(true, null, "deposit success", null));
    }

    @Override
    @Transactional
    public ResponseEntity<?> withdrawal(WithdrawalRequest body, UserPrincipal currentUser) {
        User user = userService.getCurrentUser(currentUser.getId());
        if (user == null) {
            return new ResponseEntity(new ApiResponse(false, HttpStatusCode.BAD_REQUEST.asText(), "bad request", null),
                    HttpStatus.BAD_REQUEST);
        }

        double availableBalance = user.getAcount().getBalance();
        if (body.getAmount() > availableBalance) {
            return new ResponseEntity(new ApiResponse(false, HttpStatusCode.BAD_REQUEST.asText(), "balance not enough", null),
                    HttpStatus.BAD_REQUEST);
        }

        Transaction trx = new Transaction();
        trx.setAccount(user.getAcount());
        trx.setAmount(body.getAmount());
        trx.setRemark(body.getRemark());
        trx.setSide(TrxSide.DEBET);
        trx.setType(TrxType.WITHDRAWAL);
        trx.setTrxDate(new Date());
        transactionRep.save(trx);

        List<Transaction> trxList = transactionRep.findAllByAccountId(user.getAcount().getId());
        Map<TrxSide, Double> mapSums = trxList.stream().collect(
                Collectors.groupingBy(Transaction::getSide, Collectors.summingDouble(Transaction::getAmount)));

        double debet = 0;
        double credit = 0;
        double balance = 0;

        if (mapSums.containsKey(TrxSide.DEBET)) {
            debet = mapSums.get(TrxSide.DEBET);
        }

        if (mapSums.containsKey(TrxSide.CREDIT)) {
            credit = mapSums.get(TrxSide.CREDIT);
        }

        balance = credit - debet;

        Account account = trx.getAccount();
        account.setBalance(balance);
        accountRepository.save(account);

        return ResponseEntity.ok(new ApiResponse(true, null, "withdrawal success", null));
    }

}
