package cz.zcu.kiv.jsonconverter;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * Serves as a storage of loaded properties.
 *
 * @author Jenda Kolena, jendakolena@gmail.com
 * @version 1.0
 */
public class Node
{
    /**
     * Name of the node.
     */
    private String name;
    /**
     * Value of the node.
     */
    private String defaultValue;
    /**
     * Subnodes of the node.
     */
    private TreeMap<String, Node> subcategories = new TreeMap<String, Node>();

    /**
     * Constructor.
     *
     * @param name Name of the node.
     */
    public Node(String name)
    {
        super();
        this.name = name;
    }

    /**
     * Adds subcategory - subnode.
     *
     * @param node Node to add.
     */
    public void addSubcategory(Node node)
    {
        subcategories.put(node.getName(), node);
    }

    /**
     * Finds out, if subnode of defined name exists.
     *
     * @param name Name of the subnode.
     * @return TRUE, if the subnode exists.
     */
    public boolean isSubcategory(String name)
    {
        return subcategories.containsKey(name);
    }

    /**
     * Gets subnode with defined name.
     *
     * @param name Name of the subnode.
     * @return {@link Node}, if it exists. NULL otherwise.
     */
    public Node getSubcategory(String name)
    {
        return isSubcategory(name) ? subcategories.get(name) : null;
    }

    /**
     * Gets node name.
     *
     * @return Node name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets node name.
     *
     * @param name Node name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets value of this node.
     *
     * @return Value of this node.
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * Sets value of this node.
     *
     * @param defaultValue New value of this node.
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets subnodes od this node.
     *
     * @return Subnodes.
     */
    public TreeMap<String, Node> getSubcategories()
    {
        return subcategories;
    }

    /**
     * Gets iterator of subnodes keys (names).
     *
     * @return {@link Iterator}.
     */
    public Iterator<String> getSubcategoriesKeysIterator()
    {
        return subcategories.keySet().iterator();
    }
}
