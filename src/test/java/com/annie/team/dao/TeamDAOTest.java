package com.annie.team.dao;

import com.annie.team.entity.Team;
import com.annie.utils.HibernateFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamDAOTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Team> query;

    @InjectMocks
    private TeamDAO teamDAO;

    private Team team;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setId(1L);
        team.setName("Test Team");
    }

    @Test
    void findById_TeamExist_ReturnsTeam() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.find(Team.class, 1L)).thenReturn(team);

            Optional<Team> result = teamDAO.findById(1L);

            assertTrue(result.isPresent());
            assertEquals("Test Team", result.get().getName());
            verify(session, times(1)).find(Team.class, 1L);
        }
    }

    @Test
    void findById_TeamNotFound_ReturnsEmpty() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.find(Team.class, 1L)).thenReturn(null);

            Optional<Team> result = teamDAO.findById(1L);

            assertFalse(result.isPresent());
            verify(session, times(1)).find(Team.class, 1L);
        }
    }

    @Test
    void findById_ExceptionThrown_ReturnsEmpty() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            doThrow(new RuntimeException("Mock Error")).when(session).find(Team.class, 1L);

            Optional<Team> result = teamDAO.findById(1L);

            assertFalse(result.isPresent());
            verify(session, times(1)).find(Team.class, 1L);
        }
    }

    @Test
    void findAll_TeamExist_ReturnsAllTeams() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.createQuery("FROM com.annie.team.entity.Team", Team.class)).thenReturn(query);
            when(query.getResultList()).thenReturn(Collections.singletonList(team));

            List<Team> result = teamDAO.findAll();

            assertEquals(1, result.size());
            assertEquals("Test Team", result.get(0).getName());
            verify(session, times(1)).createQuery("FROM com.annie.team.entity.Team", Team.class);
            verify(query, times(1)).getResultList();
        }
    }

    @Test
    void findAll_NoTeams_ReturnsEmptyList() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.createQuery("FROM com.annie.team.entity.Team", Team.class)).thenReturn(query);
            when(query.getResultList()).thenReturn(Collections.emptyList());

            List<Team> result = teamDAO.findAll();

            assertTrue(result.isEmpty());
            verify(session, times(1)).createQuery("FROM com.annie.team.entity.Team", Team.class);
            verify(query, times(1)).getResultList();
        }
    }

    @Test
    void findAll_ExceptionThrown_ReturnsEmptyList() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.createQuery("FROM com.annie.team.entity.Team", Team.class)).thenReturn(query);
            doThrow(new RuntimeException("Mock Error")).when(query).getResultList();

            List<Team> result = teamDAO.findAll();

            assertTrue(result.isEmpty());
            verify(session, times(1)).createQuery("FROM com.annie.team.entity.Team", Team.class);
            verify(query, times(1)).getResultList();
        }
    }

    @Test
    void save_ValidNewTeam_SavesTeam() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            doNothing().when(session).persist(team);

            Team result = teamDAO.save(team);

            assertNotNull(result);
            assertEquals("Test Team", result.getName());
            verify(session, times(1)).persist(team);
        }
    }

    @Test
    void save_ExceptionThrown_ReturnsNull() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            doThrow(new RuntimeException("Mock Error")).when(session).persist(team);

            Team result = teamDAO.save(team);

            assertNull(result);
            verify(session, times(1)).persist(team);
        }
    }

    @Test
    void update_UpdatesTeam() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.merge(team)).thenReturn(team);

            Team result = teamDAO.update(team);

            assertNotNull(result);
            assertEquals("Test Team", result.getName());
            verify(session, times(1)).merge(team);
        }
    }

    @Test
    void update_ExceptionThrown_ReturnsNull() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            doThrow(new RuntimeException("Mock Error")).when(session).merge(team);

            Team result = teamDAO.update(team);

            assertNull(result);
            verify(session, times(1)).merge(team);
        }
    }

    @Test
    void delete_DeletesTeam() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.find(Team.class, 1L)).thenReturn(team);
            doNothing().when(session).remove(team);

            teamDAO.delete(1L);

            verify(session, times(1)).find(Team.class, 1L);
            verify(session, times(1)).remove(team);
        }
    }

    @Test
    void delete_TeamNotFound_DoesNothing() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.find(Team.class, 1L)).thenReturn(null);

            teamDAO.delete(1L);

            verify(session, times(1)).find(Team.class, 1L);
            verify(session, times(0)).remove(any(Team.class));
        }
    }

    @Test
    void findByName_TeamExist_ReturnsTeam() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.createQuery("FROM Team WHERE name = :name", Team.class)).thenReturn(query);
            when(query.setParameter("name", team.getName())).thenReturn(query);
            when(query.getSingleResultOrNull()).thenReturn(team);

            Optional<Team> result = teamDAO.findByName("Test Team");

            assertTrue(result.isPresent());
            assertEquals("Test Team", result.get().getName());
            verify(session, times(1)).createQuery("FROM Team WHERE name = :name", Team.class);
            verify(query, times(1)).setParameter("name", team.getName());
            verify(query, times(1)).getSingleResultOrNull();
        }
    }

    @Test
    void findByName_TeamNotFound_ReturnsNull() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.createQuery("FROM Team WHERE name = :name", Team.class)).thenReturn(query);
            when(query.setParameter("name", "Nonexistent Team")).thenReturn(query);
            when(query.getSingleResultOrNull()).thenReturn(null);

            Optional<Team> result = teamDAO.findByName("Nonexistent Team");

            assertFalse(result.isPresent());
            verify(session, times(1)).createQuery("FROM Team WHERE name = :name", Team.class);
            verify(query, times(1)).setParameter("name", "Nonexistent Team");
            verify(query, times(1)).getSingleResultOrNull();
        }
    }

    @Test
    void findByName_ExceptionThrown_ReturnsNull() {
        try (MockedStatic<HibernateFactory> mockedHibernateFactory = mockStatic(HibernateFactory.class)) {
            mockedHibernateFactory.when(HibernateFactory::getSession).thenReturn(session);
            when(session.createQuery("FROM Team WHERE name = :name", Team.class)).thenReturn(query);
            when(query.setParameter("name", team.getName())).thenReturn(query);
            doThrow(new RuntimeException("Mock Error")).when(query).getSingleResultOrNull();

            Optional<Team> result = teamDAO.findByName("Test Team");

            assertFalse(result.isPresent());
            verify(session, times(1)).createQuery("FROM Team WHERE name = :name", Team.class);
            verify(query, times(1)).setParameter("name", team.getName());
            verify(query, times(1)).getSingleResultOrNull();
        }
    }
}