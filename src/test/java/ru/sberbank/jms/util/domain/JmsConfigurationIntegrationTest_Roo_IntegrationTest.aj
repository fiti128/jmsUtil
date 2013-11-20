// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ru.sberbank.jms.util.domain;

import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.JmsConfigurationDataOnDemand;
import ru.sberbank.jms.util.domain.JmsConfigurationIntegrationTest;

privileged aspect JmsConfigurationIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JmsConfigurationIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JmsConfigurationIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JmsConfigurationIntegrationTest: @Transactional;
    
    @Autowired
    JmsConfigurationDataOnDemand JmsConfigurationIntegrationTest.dod;
    
    @Test
    public void JmsConfigurationIntegrationTest.testCountOptions() {
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to initialize correctly", dod.getRandomJmsConfiguration());
        long count = JmsConfiguration.countOptions();
        Assert.assertTrue("Counter for 'JmsConfiguration' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JmsConfigurationIntegrationTest.testFindJmsConfiguration() {
        JmsConfiguration obj = dod.getRandomJmsConfiguration();
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to initialize correctly", obj);
        String id = obj.getConfigurationName();
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to provide an identifier", id);
        obj = JmsConfiguration.findJmsConfiguration(id);
        Assert.assertNotNull("Find method for 'JmsConfiguration' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JmsConfiguration' returned the incorrect identifier", id, obj.getConfigurationName());
    }
    
    @Test
    public void JmsConfigurationIntegrationTest.testFindAllOptions() {
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to initialize correctly", dod.getRandomJmsConfiguration());
        long count = JmsConfiguration.countOptions();
        Assert.assertTrue("Too expensive to perform a find all test for 'JmsConfiguration', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JmsConfiguration> result = JmsConfiguration.findAllOptions();
        Assert.assertNotNull("Find all method for 'JmsConfiguration' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JmsConfiguration' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JmsConfigurationIntegrationTest.testFindJmsConfigurationEntries() {
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to initialize correctly", dod.getRandomJmsConfiguration());
        long count = JmsConfiguration.countOptions();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JmsConfiguration> result = JmsConfiguration.findJmsConfigurationEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JmsConfiguration' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JmsConfiguration' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JmsConfigurationIntegrationTest.testFlush() {
        JmsConfiguration obj = dod.getRandomJmsConfiguration();
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to initialize correctly", obj);
        String id = obj.getConfigurationName();
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to provide an identifier", id);
        obj = JmsConfiguration.findJmsConfiguration(id);
        Assert.assertNotNull("Find method for 'JmsConfiguration' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJmsConfiguration(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'JmsConfiguration' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JmsConfigurationIntegrationTest.testMergeUpdate() {
        JmsConfiguration obj = dod.getRandomJmsConfiguration();
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to initialize correctly", obj);
        String id = obj.getConfigurationName();
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to provide an identifier", id);
        obj = JmsConfiguration.findJmsConfiguration(id);
        boolean modified =  dod.modifyJmsConfiguration(obj);
        Integer currentVersion = obj.getVersion();
        JmsConfiguration merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getConfigurationName(), id);
        Assert.assertTrue("Version for 'JmsConfiguration' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JmsConfigurationIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to initialize correctly", dod.getRandomJmsConfiguration());
        JmsConfiguration obj = dod.getNewTransientJmsConfiguration(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JmsConfiguration' identifier to be null", obj.getConfigurationName());
        try {
            obj.persist();
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        obj.flush();
        Assert.assertNotNull("Expected 'JmsConfiguration' identifier to no longer be null", obj.getConfigurationName());
    }
    
    @Test
    public void JmsConfigurationIntegrationTest.testRemove() {
        JmsConfiguration obj = dod.getRandomJmsConfiguration();
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to initialize correctly", obj);
        String id = obj.getConfigurationName();
        Assert.assertNotNull("Data on demand for 'JmsConfiguration' failed to provide an identifier", id);
        obj = JmsConfiguration.findJmsConfiguration(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'JmsConfiguration' with identifier '" + id + "'", JmsConfiguration.findJmsConfiguration(id));
    }
    
}
