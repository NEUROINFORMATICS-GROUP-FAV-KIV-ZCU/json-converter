package cz.zcu.kiv.jsonconverter;

import cz.zcu.kiv.jsonconverter.Converter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;

/**
 * Class allowing the Converter to be executed as Maven plugin mojo.
 * If not defined otherwise in the project configuration, the plugin
 * will execute in the process-sources phase, that is before compilation.
 * Created by IntelliJ IDEA.
 * User: jnovotny
 * Date: 24.11.11
 * Time: 13:31
 * @goal json
 * @phase process-sources
 */
public class JsonConverterMavenMojo extends AbstractMojo {

    /**
     * Logger injected by maven, enables the plugin to output
     * messages visible by the user
     */
    Log logger;

     /**
     * Properties filename.
     * @parameter expression="${jsonconverter.srcFile}"
     * @required
     */
    File srcFile;
    /**
     * Filename, where created properties will be saved to.
     * @parameter expression="${jsonconverter.destFile}"
     * @required
     */
    File destFile;
    /**
    * Determines whether we want to save properties as JSON. TRUE means save as
    * JSON, FALSE means save as Java {@link java.util.Properties}.
    * @parameter expression="${jsonconverter.saveAsJSON}" default-value="true"
     */
    boolean saveAsJSON;

    /**
     * This method is executed by maven and converts Java properties to Json messages
     * using the Converter class
     * @throws MojoExecutionException
     */
    public void execute() throws MojoExecutionException {
        try {
            //createDirIfNeeded(destFile.getParentFile());//When saving to potentially non-existent target dir
            Converter.runConversion(srcFile, destFile, saveAsJSON);
            logger.info("JSON message file successfully created in " + destFile.getAbsolutePath());
        } catch (IOException e) {
            throw new MojoExecutionException("JSON message conversion failed: " + e.getMessage(),e);
        }
    }

//    private void createDirIfNeeded(File destDir) {
//        if(destDir != null && !destDir.exists()){
//            destDir.mkdirs();
//        }
//    }

    public void setLog(org.apache.maven.plugin.logging.Log logger){
        this.logger = logger;
    }
}
