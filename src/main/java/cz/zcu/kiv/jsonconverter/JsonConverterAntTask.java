package cz.zcu.kiv.jsonconverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import cz.zcu.kiv.jsonconverter.Converter;
import org.apache.tools.ant.Task;

/**
 * Class allowing the Converter to be executed as Ant task
 * Created by IntelliJ IDEA.
 * User: jnovotny from the code in Converter.java by Jenda Kolena
 * Date: 24.11.11
 * Time: 13:17
 */
public class JsonConverterAntTask extends Task {

    /**
     * Properties filename.
     */
    private String srcFilename;
    /**
     * Filename, where created properties will be saved to.
     */
    private String destFilename;
    /**
     * Determines whether we want to save properties as JSON. TRUE means save as
     * JSON, FALSE means save as Java {@link Properties}.
     */
    private boolean saveAsJSON;


    /**
     * Ant tasks execution method.
     */
    public void execute() {
        try {
            Converter.runConversion(srcFilename, destFilename, saveAsJSON);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Filename with source properties.
     *
     * @param srcFilename Filename.
     */
    public void setSrcFilename(String srcFilename) {
        this.srcFilename = srcFilename;
    }

    /**
     * Filename, where created properties will be saved to.
     *
     * @param destFilename Filename.
     */
    public void setDestFilename(String destFilename) {
        this.destFilename = destFilename;
    }

    /**
     * Determines whether we want to save properties as JSON.
     *
     * @param saveAsJSON TRUE means save as JSON, FALSE means save as Java
     *                   {@link Properties}.
     */
    public void setSaveAsJSON(boolean saveAsJSON) {
        this.saveAsJSON = saveAsJSON;
    }
}
