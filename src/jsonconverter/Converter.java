package cz.zcu.kiv.jsonconverter;

import org.apache.tools.ant.Task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Serves for conversion from JAVA Properties to JSON.
 *
 * @author Jenda Kolena, jendakolena@gmail.com
 * @version 1.0
 */
public class Converter extends Task
{

    /**
     * Constant used as tabulator while saving to JSON format.
     */
    public static final String TABULATOR = "  ";

    /**
     * Root keys storage.
     */
    private TreeMap<String, Node> keys;
    /**
     * Properties attribute.
     */
    private Properties props;
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
     * Lod the properties file into data structure.
     *
     * @param properties Properties file.
     * @return Created TreeMap<String, Node>.
     * @throws FileNotFoundException If file does not exist.
     */
    public TreeMap<String, Node> load(Properties properties) throws FileNotFoundException
    {
        this.props = properties;
        Enumeration<Object> propKeys = props.keys();
        keys = new TreeMap<String, Node>();
        while (propKeys.hasMoreElements())
        {
            String key = (String) propKeys.nextElement();
            String[] keyPart = key.split("\\.");
            keys.put(keyPart[0], getNodes(keys.get(keyPart[0]), getNextKey(key), properties.getProperty(key)));
        }
        return keys;
    }

    /**
     * Gets nodes from properties file.
     *
     * @param root  Root node, from which we need to determine structure.
     * @param key   Key found in properties file.
     * @param value Value of the key.
     * @return Root {@link Node}.
     */
    private static Node getNodes(Node root, String key, String value)
    {
        String[] keyPart = key.split("\\.");
        if (root == null)
        {
            root = new Node(keyPart[0]);
        }

        if (keyPart.length == 1)
        {
            Node tmp = new Node(keyPart[0]);
            tmp.setDefaultValue(value);
            root.addSubcategory(tmp);
            return root;
        }
        else
        {
            Node tmp;
            if (root.isSubcategory(keyPart[0]))
            {
                tmp = root.getSubcategory(keyPart[0]);
            }
            else
            {
                tmp = new Node(keyPart[0]);
            }

            root.addSubcategory(getNodes(tmp, getNextKey(key), value));
            return root;
        }
    }

    /**
     * Crop first element of key and return remainder.
     *
     * @param original Original key.
     * @return Croped key.
     */
    public static String getNextKey(String original)
    {
        return original.substring(original.indexOf(".") + 1);
    }

    /**
     * Gets loaded data in {@link Properties} format.
     *
     * @param path Path to the data.
     * @param node Node to write his data.
     * @return Formatted data in {@link String}.
     */
    private String writeInPropertiesFormat(String path, Node node)
    {
        if (node.getSubcategories().isEmpty())
        {
            return (path + "=" + node.getDefaultValue() + "\n");
        }
        Iterator<String> it = node.getSubcategoriesKeysIterator();
        StringBuilder builder = new StringBuilder();
        while (it.hasNext())
        {
            String name = it.next();
            builder.append(writeInPropertiesFormat(path + "." + name, node.getSubcategory(name)));
        }
        return builder.toString();
    }

    /**
     * Gets structure in {@link Properties} format.
     *
     * @return {@link String} with data.
     */
    public String getStructureAsProperties()
    {
        Iterator<String> it = keys.keySet().iterator();
        StringBuilder builder = new StringBuilder();
        while (it.hasNext())
        {
            String name = it.next();
            builder.append(writeInPropertiesFormat(name, keys.get(name)));
        }
        return builder.toString();
    }

    /**
     * Save data in {@link Properties} format with UTF-8 encoding.
     *
     * @param filename Filename to save to.
     * @return TRUE, if save was succesful.
     * @see {@link #saveAsProperties(String, String)}
     */
    public boolean saveAsProperties(String filename)
    {
        return saveAsProperties(filename, "utf-8");
    }

    /**
     * Save data in {@link Properties} format with defined encoding.
     *
     * @param filename Filename to save to.
     * @param encoding Encoding to save data.
     * @return TRUE, if save was succesful.
     * @see #saveAsProperties(String)
     */
    public boolean saveAsProperties(String filename, String encoding)
    {
        try
        {
            PrintWriter pw = new PrintWriter(filename, encoding);
            pw.write(getStructureAsProperties());
            pw.close();
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Gets loaded data in JSON format.
     *
     * @param level Level of data (recursion).
     * @param node  Node to determine data from.
     * @return Formatted data in {@link String}.
     * @see <a href="http://www.json.org/">JSON.org</a>
     */
    private String writeInJsonFormat(int level, Node node)
    {
        if (node.getSubcategories().isEmpty())
        {
            return ("\"" + node.getDefaultValue().replaceAll("\"", "\\\\\"") + "\",\n");
        }

        Iterator<String> it = node.getSubcategoriesKeysIterator();
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (int i = 1; i <= level - 1; i++)
        {
            builder.append(TABULATOR);
        }
        builder.append("{\n");
        while (it.hasNext())
        {
            String name = it.next();
            for (int i = 1; i <= level; i++)
            {
                builder.append(TABULATOR);
            }
            builder.append("\"" + name + "\":" + writeInJsonFormat(level + 1, node.getSubcategory(name)));
        }
        builder.deleteCharAt(builder.length() - 2);
        for (int i = 1; i <= level - 1; i++)
        {
            builder.append(TABULATOR);
        }
        builder.append("},\n");
        return builder.toString();
    }

    /**
     * Gets structure in JSON format.
     *
     * @return {@link String} with data.
     * @see <a href="http://www.json.org/">JSON.org</a>
     */
    public String getStructureAsJson()
    {
        Iterator<String> it = keys.keySet().iterator();
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        while (it.hasNext())
        {
            String name = it.next();
            builder.append(TABULATOR + "\"" + name + "\":");
            builder.append(writeInJsonFormat(2, keys.get(name)));
        }
        builder.deleteCharAt(builder.length() - 2);
        builder.append("}");
        return builder.toString();
    }

    /**
     * Save data in JSON format with UTF-8 encoding.
     *
     * @param filename Filename to save to.
     * @return TRUE, if save was succesful.
     * @see {@link #saveAsJson(String, String)}
     */
    public boolean saveAsJson(String filename)
    {
        return saveAsJson(filename, "utf-8");
    }

    /**
     * Save data in {@link Properties} format with UTF-8 encoding.
     *
     * @param filename Filename to save to.
     * @param encoding Encoding to save data.
     * @return TRUE, if save was succesful.
     * @see {@link #saveAsProperties(String, String)}
     */
    public boolean saveAsJson(String filename, String encoding)
    {
        try
        {
            PrintWriter pw = new PrintWriter(filename, encoding);
            pw.write(getStructureAsJson());
            pw.close();
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Filename with source properties.
     *
     * @param srcFilename Filename.
     */
    public void setSrcFilename(String srcFilename)
    {
        this.srcFilename = srcFilename;
    }

    /**
     * Filename, where created properties will be saved to.
     *
     * @param destFilename Filename.
     */
    public void setDestFilename(String destFilename)
    {
        this.destFilename = destFilename;
    }

    /**
     * Determines whether we want to save properties as JSON.
     *
     * @param saveAsJSON TRUE means save as JSON, FALSE means save as Java
     *                   {@link Properties}.
     */
    public void setSaveAsJSON(boolean saveAsJSON)
    {
        this.saveAsJSON = saveAsJSON;
    }

    /**
     * Ant tasks execution method.
     */
    public void execute()
    {
        Properties props = new Properties();
        Converter c;
        try
        {
            FileInputStream fis = new FileInputStream(srcFilename);
            props.load(fis);
            fis.close();

            c = new Converter();
            c.load(props);

            if (saveAsJSON)
            {
                c.saveAsJson(destFilename);
            }
            else
            {
                c.saveAsProperties(destFilename);
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
