package com.avnt.soldi.service;

import com.avnt.soldi.model.cheque.Diagnostic;
import com.avnt.soldi.model.repositories.DiagnosticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Class DiagnosticService is service, that handle DiagnosticController.
 * Use @Autowired for connect to necessary repositories
 *
 * @version 1.1
 * @author Dmitry
 * @since 21.01.2016
 */

@Service
public class DiagnosticService {

    @Autowired DiagnosticRepository diagnosticRepository;

    /**
     * Method addDiagnostic add diagnostic to DB with current DateTime
     * @param chequeID is ID of cheque in database, in that client-side wants add a diagnostic comment
     * @param diagnostic is data for Diagnostic.class, that was create on client-side
     */
    @Modifying
    @Transactional
    public void addDiagnostic(Long chequeID, Diagnostic diagnostic) {
        diagnosticRepository.save(diagnostic.withDateTime());
    }

    /**
     * Method deleteDiagnostic delete diagnostic from DB
     * @param chequeID is ID of cheque in database, in that client-side wants delete diagnostic comment
     * @param diagnosticID is ID of diagnostic in database, that client-side wants to delete
     */
    @Modifying
    @Transactional
    public void deleteDiagnostic(Long chequeID, Long diagnosticID) {
        diagnosticRepository.delete(diagnosticID);
    }

    @Transactional(readOnly = true)
    public List<Diagnostic> getDiagnostics(Long chequeID) {
        return diagnosticRepository.findByChequeId(chequeID);
    }

}
