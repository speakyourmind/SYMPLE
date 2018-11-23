/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.main;

import java.util.List;
import org.symfound.comm.file.PathReader;

/**
 *
 * @author Javed Gangjee
 */
public class TestMSIBuilder {

    /**
     *
     */
    public static void generateFileNames() {
        PathReader reader = new PathReader("D:\\Volunteer\\SpeakYourMind\\NetBeans\\SYMPLE\\symple-main\\target\\jfx\\app\\etc\\tobii\\");

        List<String> folderFileNames = reader.getFolderFileNames();
        String string = "";
        Integer n = 4000;
        for (String path : folderFileNames) {
            string = string.concat("<File Id=\"FileId" + n.toString()
                    + "\" Name=\"" + path + "\"  "
                    + "Source=\"app\\etc\\tobii\\" + path + "\" ProcessorArchitecture=\"x64\">"
                    + "</File>\n");
            n++;

        }

        PathReader reader2 = new PathReader("D:\\Volunteer\\SpeakYourMind\\NetBeans\\SYMPLE\\symple-main\\target\\jfx\\native\\SYMPLE\\runtime\\bin");

        List<String> folderFileNames2 = reader2.getFolderFileNames();

        String string2 = "";
        Integer n2 = 2000;
        for (String path : folderFileNames2) {
            string2 = string2.concat("<File Id=\"FileId" + n2.toString() + "\" "
                    + "Name=\"" + path + "\"  "
                    + "Source=\"runtime\\bin\\" + path + "\" "
                    + "ProcessorArchitecture=\"x64\"></File>\n");
            n2++;

        }
        PathReader reader3 = new PathReader("D:\\Volunteer\\SpeakYourMind\\NetBeans\\SYMPLE\\symple-main\\target\\jfx\\native\\SYMPLE\\app\\lib");

        List<String> folderFileNames3 = reader3.getFolderFileNames();
        String string3 = "";
        Integer n3 = 1000;
        for (String path : folderFileNames3) {
            string3 = string3.concat("<File Id=\"FileId" + n3.toString()
                    + "\" Name=\"" + path + "\"  "
                    + "Source=\"app\\lib\\" + path + "\" ProcessorArchitecture=\"x64\">"
                    + "</File>\n");
            n3++;

        }
         PathReader reader4 = new PathReader("D:\\Volunteer\\SpeakYourMind\\NetBeans\\SYMPLE\\symple-main\\target\\jfx\\native\\SYMPLE\\runtime\\lib\\security\\policy\\unlimited\\");

        List<String> folderFileNames4 = reader4.getFolderFileNames();
        String string4 = "";
        Integer n4 = 12000;
        for (String path : folderFileNames4) {
            string4 = string4.concat("<File Id=\"FileId" + n4.toString()
                    + "\" Name=\"" + path + "\"  "
                    + "Source=\"" + path + "\" ProcessorArchitecture=\"x64\">"
                    + "</File>\n");
            n4++;

        }
        System.out.println(string4);

    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        generateFileNames();
    }
}
