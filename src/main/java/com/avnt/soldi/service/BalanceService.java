package com.avnt.soldi.service;

import com.avnt.soldi.model.repositories.BalanceRepository;
import com.avnt.soldi.model.repositories.ChequeRepository;
import com.avnt.soldi.model.cheque.Balance;
import com.avnt.soldi.model.cheque.Cheque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class BalanceService is service, that handle BalanceController.
 * Use @Autowired for connect to necessary repositories
 *
 * @version 1.1
 * @author Dmitry
 * @since 21.01.2016
 */

@Service
public class BalanceService {

    @Autowired
    BalanceRepository balanceRepository;
    @Autowired
    ChequeRepository chequeRepository;

    /**
     * Method getBalanceOfCheque return balance of cheque
     * @param chequeID is id of cheque, that balance should be saved
     * @return balance of cheque with request cheque id
     */
    @Transactional(readOnly = true)
    public Balance getBalanceOfCheque(Long chequeID) {return balanceRepository.findByChequeId(chequeID);}

    /**
     * Method synchronizeBalanceOfCheque add balance to DB and return it to client
     * @param chequeID is id of cheque, that balance should be saved
     * @param balance is balance object, that should be saved
     * @return this Balance entity after saving
     */
    @Modifying
    @Transactional
    public Balance synchronizeBalanceOfCheque(Long chequeID, Balance balance) {

        Cheque cheque = chequeRepository.findOne(chequeID);

        if (balance.getPayments() != null)
            balance.getPayments().stream().forEach(payment -> payment.setBalance(balance));

        balance.setCheque(cheque);
        balanceRepository.save(balance);

        return balanceRepository.findByChequeId(chequeID);
    }
}
