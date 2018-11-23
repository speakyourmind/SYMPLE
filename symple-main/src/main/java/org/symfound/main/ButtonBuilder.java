/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.main;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.symfound.comm.file.PathReader;
import org.symfound.comm.file.PathWriter;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class ButtonBuilder {

    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        PathReader reader = new PathReader("D:\\AAC\\BW\\EN_BW_Pictograms");
        List<String> folderFileNames = reader.getFolderFileNames();
        //System.out.println(folderFileNames);
        String string = "";
        for (int i = 0; i < folderFileNames.size(); i++) {
            String fileName = WordUtils.capitalize(folderFileNames.get(i).replaceAll(".png", "").replaceAll("_", " "));
            string = string + " <node name=\"" + fileName.toLowerCase().replaceAll(" ", "") + "\">\n"
                    + "                <map>\n"
                    + "                    <entry key=\"keyCodeConfig\" value=\"-100\"/>\n"
                    + "                    <entry key=\"speakable\" value=\"true\"/>\n"
                    + "                    <entry key=\"typable\" value=\"true\"/>\n"
                    + "                    <entry key=\"title\" value=\"" + fileName + "\"/>\n"
                    + "                    <entry key=\"titlePos\" value=\"BOTTOM_RIGHT\"/>\n"
                    + "                    <entry key=\"textColour\" value=\"DARK\"/>\n"
                    + "                    <entry key=\"fontScale\" value=\"22.5\"/>\n"
                    + "                    <entry key=\"overrideStyle\" value=\"-fx-background-color:white;&#10;-fx-background-size:contain;\"/>\n"
                    + "                    <entry key=\"backgroundColour\" value=\"LIGHT\"/>\n"
                    + "                    <entry key=\"backgroundURL\" value=\"http://speakyourmindfoundation.org/downloads/SYMPLE/symbols/EN_BW_Pictograms/" + folderFileNames.get(i) + "\"/>\n"
                    + "                    <entry key=\"showTitle\" value=\"true\"/>\n"
                    + "                    <entry key=\"selectable\" value=\"true\"/>\n"
                    + "                    <entry key=\"disablePrimary\" value=\"false\"/>\n"
                    + "                    <entry key=\"rowExpand\" value=\"0\"/>\n"
                    + "                    <entry key=\"columnExpand\" value=\"0\"/>"
                    + "                </map>\n"
                    + "              </node>\n";
            System.out.println(fileName);
        };

        PathWriter writer = new PathWriter(System.getProperty("user.home") + "/Documents/SYMPLE/Phrases.xml");
        try {
            writer.writeToFile(string, Boolean.TRUE, Boolean.TRUE);
        } catch (IOException ex) {

        }

   /*     String string1 = "";

        List<String> keys = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X", "C", "V", "B", "N", "M");
        for (int i = 0; i < keys.size(); i++) {
            string1 = string1 + "<node name=\"" + keys.get(i).toLowerCase() + "\">\n"
                    + "                  <map>\n"
                    + "                    <entry key=\"title\" value=\"" + keys.get(i) + "\"/>\n"
                    + "                    <entry key=\"titlePos\" value=\"CENTER\"/>\n"
                    + "                    <entry key=\"showTitle\" value=\"true\"/>\n"
                    + "                    <entry key=\"overrideStyle\" value=\"-fx-text-fill:-fx-dark;&#10;-fx-font-size:48pt;\"/>\n"
                    + "                    <entry key=\"keyCodeConfig\" value=\"-100\"/>\n"
                    + "                    <entry key=\"rowExpand\" value=\"0\"/>\n"
                    + "                    <entry key=\"columnExpand\" value=\"0\"/>\n"
                    + "                    <entry key=\"speakable\" value=\"true\"/>\n"
                    + "                    <entry key=\"typable\" value=\"false\"/>\n"
                    + "                  </map>\n"
                    + "                </node>";
        }

        System.out.println(string1);*/
    }
}
