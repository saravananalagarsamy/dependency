package com.salesforce.tests;

import com.salesforce.dependency.SalesforceMain;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.junit.runner.RunWith;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit Test runner: capture stdin/stdout for testing
 */
@RunWith(MockitoJUnitRunner.class)
public class BaseTest {

    @Rule
    public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests().enableLog();
    
    private SalesforceMain main;

    @Before
    public void setUp() {
        main = new SalesforceMain();
    }

    protected void runTest(String expectedOutput, String... input) {
        systemInMock.provideLines(input);
        SalesforceMain.main(new String[0]);
        Assert.assertEquals(expectedOutput, systemOutRule.getLogWithNormalizedLineSeparator());
    }
}
