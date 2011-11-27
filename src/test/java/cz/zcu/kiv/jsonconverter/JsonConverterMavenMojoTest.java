package cz.zcu.kiv.jsonconverter;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jnovotny
 * Date: 24.11.11
 * Time: 12:47
 */
public class JsonConverterMavenMojoTest extends AbstractMojoTestCase {
    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * {@inheritDoc}
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testJsonConverter() throws Exception {
        File pom = getTestFile("src/test/resources/dummy-maven-project/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        JsonConverterMavenMojo jm = (JsonConverterMavenMojo) lookupMojo("json", pom);
        assertNotNull(jm);
        jm.execute();

        assertTrue(jm.destFile.exists());
    }
}