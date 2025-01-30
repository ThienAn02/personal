package com.annie.team.dao;

import com.annie.team.entity.Team;
import com.annie.utils.HibernateFactory;
import com.annie.utils.ImplementBaseDAO;
import jakarta.ejb.Stateless;
import org.hibernate.Session;

import java.util.Optional;

@Stateless
public class TeamDAO extends ImplementBaseDAO<Team, Long> {

    public TeamDAO() {
        super(Team.class);
    }

    public Optional<Team> findByName(String name) {
        try (Session session = HibernateFactory.getSession();) {
            Team team = session.createQuery("FROM Team WHERE name = :name", Team.class)
                        .setParameter("name", name)
                        .getSingleResultOrNull();
            return Optional.ofNullable(team);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
