package com.mockrunner.test.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import com.mockrunner.jdbc.XMLResultSetFactory;
import com.mockrunner.mock.jdbc.MockResultSet;

public class XMLResultSetFactoryTest
{
    /**
     * Test for the Sybase Dialect of the XMLResultSetFactory
     */
//	@Test
    public void testSybaseCreate() 
    {
        XMLResultSetFactory goodSybaseXMLRSF = new XMLResultSetFactory("target/test-classes/com/mockrunner/test/jdbc/xmltestresult.xml");
        MockResultSet goodMRS = goodSybaseXMLRSF.create("Good-ResultSet-ID");
        assertNotNull(goodSybaseXMLRSF.getXMLFile());
        doTestGoodResultSet(goodSybaseXMLRSF, goodMRS);
        
        goodSybaseXMLRSF = new XMLResultSetFactory("/com/mockrunner/test/jdbc/xmltestresult.xml");
        goodMRS = goodSybaseXMLRSF.create("Good-ResultSet-ID");
        assertNotNull(goodSybaseXMLRSF.getXMLFile());
        doTestGoodResultSet(goodSybaseXMLRSF, goodMRS);
        
    }
    
    private void doTestGoodResultSet(XMLResultSetFactory goodSybaseXMLRSF, MockResultSet goodMRS)
    {
        assertEquals("Dialects should be equal!", XMLResultSetFactory.SYBASE_DIALECT, goodSybaseXMLRSF.getDialect());
        assertEquals("There should be 2 columns!", 2, goodMRS.getColumnCount());
        assertEquals("There should be 3 rows!", 3, goodMRS.getRowCount());
    }
    
    /**
     * Test for the SquirrelSQL Dialect of the XMLResultSetFactory
     */
    public void testSquirrelCreate() 
    {
        XMLResultSetFactory goodSquirrelXMLRSF = new XMLResultSetFactory("target/test-classes/com/mockrunner/test/jdbc/squirrelxmltestresult.xml");
        goodSquirrelXMLRSF.setDialect(XMLResultSetFactory.SQUIRREL_DIALECT);
        MockResultSet goodMRS = goodSquirrelXMLRSF.create("Good-ResultSet-ID");
        assertNotNull(goodSquirrelXMLRSF.getXMLFile());
        doTestGoodSquirrelResultSet(goodSquirrelXMLRSF, goodMRS);
    }
    
    private void doTestGoodSquirrelResultSet(XMLResultSetFactory goodSquirrelXMLRSF, MockResultSet goodMRS)
    {
        assertEquals("Dialects should be equal!", XMLResultSetFactory.SQUIRREL_DIALECT, goodSquirrelXMLRSF.getDialect());
        assertEquals("There should be 2 columns!", 2, goodMRS.getColumnCount());
        assertEquals("There should be 3 rows!", 3, goodMRS.getRowCount());
    }
    
    /**
     * Test for a bad create where there is no actual file 
     * passed to the XMLResultSetFactory
     */
    @Test
    public void testBadCreate() 
    {
        try
        {
            XMLResultSetFactory badXMLRSF = new XMLResultSetFactory("target/test-classes/com/mockrunner/test/jdbc/nonexisting.xml");
            assertNull(badXMLRSF.getXMLFile());
            badXMLRSF.create("Bad-ResultSet-ID");
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            XMLResultSetFactory badXMLRSF = new XMLResultSetFactory(new File("target/test-classes/com/mockrunner/test/jdbc/nonexisting.xml"));
            assertNull(badXMLRSF.getXMLFile());
            badXMLRSF.create("Bad-ResultSet-ID");
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
    
//    @Test
    public void testGetXMLFile()
    {
        XMLResultSetFactory factory = new XMLResultSetFactory("target/test-classes/com/mockrunner/test/jdbc/xmltestresult.xml");
        assertEquals(new File("target/test-classes/com/mockrunner/test/jdbc/xmltestresult.xml"), factory.getXMLFile());
        factory = new XMLResultSetFactory(new File("target/test-classes/com/mockrunner/test/jdbc/xmltestresult.xml"));
        assertEquals(new File("target/test-classes/com/mockrunner/test/jdbc/xmltestresult.xml"), factory.getXMLFile());
        factory = new XMLResultSetFactory("badfile");
        try
        {
            factory.getXMLFile();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        factory = new XMLResultSetFactory(new File("badfile"));
        try
        {
            factory.getXMLFile();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
}
