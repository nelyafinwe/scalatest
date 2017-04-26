package com.mockrunner.test.jdbc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

import org.junit.Test;

import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockDataSource;
import com.mockrunner.mock.jdbc.MockDriver;

public class JDBCMockObjectFactoryTest
{
	@Test
    public void testCreate() throws Exception
    {
        JDBCMockObjectFactory factory = new JDBCMockObjectFactory();
        assertTrue(factory.getMockDriver().connect("", null) == factory.getMockDataSource().getConnection());
        assertNotNull(DriverManager.getConnection("test"));
        assertTrue(factory.getMockDriver().connect("", null) == DriverManager.getConnection("test"));
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        drivers.nextElement();
        assertFalse(drivers.hasMoreElements());
    }
    
	@Test
    public void testRestoreDivers() throws Exception
    {
        DriverManager.registerDriver(new TestDriver());
        DriverManager.registerDriver(new TestDriver());
        ArrayList<Driver> oldDrivers = new ArrayList<Driver>();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while(drivers.hasMoreElements())
        {
            oldDrivers.add(drivers.nextElement());
        }
        JDBCMockObjectFactory factory = new JDBCMockObjectFactory();
        new JDBCMockObjectFactory();
        new JDBCMockObjectFactory();
        drivers = DriverManager.getDrivers();
        assertTrue(drivers.nextElement() instanceof MockDriver);
        assertFalse(drivers.hasMoreElements());
        factory.restoreDrivers();
        drivers = DriverManager.getDrivers();
        int numberDrivers = 0;
        while(drivers.hasMoreElements())
        {
            numberDrivers++;
            assertTrue(oldDrivers.contains(drivers.nextElement()));
        }
        factory.restoreDrivers();
        factory.restoreDrivers();
        factory.restoreDrivers();
        int newNumberDrivers = 0;
        drivers = DriverManager.getDrivers();
        while(drivers.hasMoreElements())
        {
            newNumberDrivers++;
            assertTrue(oldDrivers.contains(drivers.nextElement()));
        }
        assertTrue(numberDrivers == newNumberDrivers);
        factory.registerMockDriver();
        drivers = DriverManager.getDrivers();
        numberDrivers = 0;
        while(drivers.hasMoreElements())
        {
            numberDrivers++;
            assertTrue(drivers.nextElement() instanceof MockDriver);
        }
        assertTrue(numberDrivers == 1);
        factory.registerMockDriver();
        factory.registerMockDriver();
        factory.restoreDrivers();
        factory.restoreDrivers();
        drivers = DriverManager.getDrivers();
        numberDrivers = 0;
        while(drivers.hasMoreElements())
       {
           numberDrivers++;
           assertTrue(oldDrivers.contains(drivers.nextElement()));
       }
       assertTrue(numberDrivers == newNumberDrivers);
    }
    
	@Test
    public void testOverrideCreate()
    {
        JDBCMockObjectFactory factory = new TestJDBCMockObjectFactory();
        assertNotSame(factory.getMockConnection().getClass(), MockConnection.class);
        assertNotSame(factory.getMockDataSource().getClass(), MockDataSource.class);
        assertNotSame(factory.getMockDriver().getClass(), MockDriver.class);
    }
    
    public static class TestDriver implements Driver
    {
    
        public boolean acceptsURL(String url) throws SQLException
        {
            return false;
        }

        public Connection connect(String url, Properties info) throws SQLException
        {
            return null;
        }

        public int getMajorVersion()
        {
            return 0;
        }

        public int getMinorVersion()
        {
            return 0;
        }

        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException
        {
            return null;
        }

        public boolean jdbcCompliant()
        {
            return false;
        }

        public Logger getParentLogger() throws SQLFeatureNotSupportedException
        {
            return null;
        }
    }
    
    public static class TestJDBCMockObjectFactory extends JDBCMockObjectFactory
    {
        public MockConnection createMockConnection()
        {
            return new MockConnection() {};
        }

        public MockDataSource createMockDataSource()
        {
            return new MockDataSource() {};
        }

        public MockDriver createMockDriver()
        {
            return new MockDriver() {};
        }
    }
}
