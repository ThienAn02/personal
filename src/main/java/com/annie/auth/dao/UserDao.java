package com.annie.auth.dao;

import com.annie.auth.entity.User;
import com.annie.utils.ImplementBaseDAO;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Stateless
public class UserDao extends ImplementBaseDAO<User, Long> {
    @PersistenceContext
    private EntityManager entityManager;

    public UserDao() {
        super(User.class);
    }

    public Optional<User> findByEmail(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root).where(cb.equal(root.get("email"), email.trim()));

        TypedQuery<User> query = entityManager.createQuery(cq);
        List<User> result = query.getResultList();

        return result.stream().findFirst();
    }

    public int count() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<User> root = query.from(User.class);
        query.select(cb.count(root)); // Đếm số lượng bản ghi trong bảng User

        Long result = entityManager.createQuery(query).getSingleResult();
        return result != null ? result.intValue() : 0; // Trả về int
    }
}
