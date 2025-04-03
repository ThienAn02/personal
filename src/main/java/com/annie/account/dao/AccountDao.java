package com.annie.account.dao;

import com.annie.account.entity.Account;
import com.annie.base.dao.BaseDAO;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Stateless
public class AccountDao extends BaseDAO<Account> {

    public AccountDao() {
        super(Account.class);
    }

    public Optional<Account> getByEmail(String email) {
        try {
            return Optional.ofNullable(entityManager.createQuery(
                            "SELECT a FROM Account a WHERE a.email = :email", Account.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    public Account findAccountByDoctorId(Long doctorId) {
        TypedQuery<Account> query = entityManager.createQuery(
                "SELECT a FROM Account a WHERE a.doctor.id = :doctorId", Account.class);
        query.setParameter("doctorId", doctorId);
        List<Account> accounts = query.getResultList();
        return accounts.isEmpty() ? null : accounts.get(0);
    }

    public Account findAccountByPatientId(Long patientId) {
        TypedQuery<Account> query = entityManager.createQuery(
                "SELECT a FROM Account a WHERE a.patient.id = :patientId", Account.class);
        query.setParameter("patientId", patientId);
        List<Account> accounts = query.getResultList();
        return accounts.isEmpty() ? null : accounts.get(0);
    }

}
