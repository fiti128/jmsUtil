// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ru.sberbank.jms.util.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.jms.util.domain.JmsMessage;

privileged aspect JmsMessage_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager JmsMessage.entityManager;
    
    public static final EntityManager JmsMessage.entityManager() {
        EntityManager em = new JmsMessage().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long JmsMessage.countJmsMessages() {
        return entityManager().createQuery("SELECT COUNT(o) FROM JmsMessage o", Long.class).getSingleResult();
    }
    
    public static List<JmsMessage> JmsMessage.findAllJmsMessages() {
        return entityManager().createQuery("SELECT o FROM JmsMessage o", JmsMessage.class).getResultList();
    }
    
    public static JmsMessage JmsMessage.findJmsMessage(Long id) {
        if (id == null) return null;
        return entityManager().find(JmsMessage.class, id);
    }
    
    public static List<JmsMessage> JmsMessage.findJmsMessageEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM JmsMessage o", JmsMessage.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void JmsMessage.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void JmsMessage.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            JmsMessage attached = JmsMessage.findJmsMessage(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void JmsMessage.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void JmsMessage.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public JmsMessage JmsMessage.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        JmsMessage merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
