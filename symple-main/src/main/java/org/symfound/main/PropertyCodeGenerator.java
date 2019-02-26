/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.main;

/**
 *
 * @author Administrator
 */
public class PropertyCodeGenerator {

    static String NAME = "DateFormat";
    static String DATA_TYPE = "String";
    static String PROPERTY_TYPE = "String";

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        String string = " private static final " + DATA_TYPE + " " + NAME.toUpperCase() + "_KEY = \"" + NAME.toLowerCase() + "\";\n"
                + "    private static final " + DATA_TYPE + " DEFAULT_" + NAME.toUpperCase() + " = \"\";\n"
                + "    private " + PROPERTY_TYPE + "Property " + NAME.toLowerCase() + ";\n"
                + "\n"
                + "    /**\n"
                + "     *\n"
                + "     * @param value\n"
                + "     */\n"
                + "    public void set" + NAME + "(" + DATA_TYPE + " value) {\n"
                + "        " + NAME.toLowerCase() + "Property().setValue(value);\n"
                + "        getPreferences().put(\"" + NAME.toLowerCase() + "\", value);\n"
                + "        LOGGER.info(\"" + NAME + " set to: \" + value);\n"
                + "\n"
                + "    }\n"
                + "\n"
                + "    /**\n"
                + "     *\n"
                + "     * @return\n"
                + "     */\n"
                + "    public " + DATA_TYPE + " get" + NAME + "() {\n"
                + "        return " + NAME.toLowerCase() + "Property().getValue();\n"
                + "    }\n"
                + "\n"
                + "    /**\n"
                + "     *\n"
                + "     * @return\n"
                + "     */\n"
                + "    public " + PROPERTY_TYPE + "Property " + NAME.toLowerCase() + "Property() {\n"
                + "        //   if (" + NAME.toLowerCase() + " == null) {\n"
                + "        " + NAME.toLowerCase() + " = new Simple" + PROPERTY_TYPE + "Property(getPreferences().get(" + NAME.toUpperCase() + "_KEY, DEFAULT_" + NAME.toUpperCase() + "));\n"
                + "        // }\n"
                + "        return " + NAME.toLowerCase() + ";\n"
                + "    }";
    }
}
