package org.symfound.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * list resources available from the classpath @ *
 */
public class ResourceLister {

    /**
     *
     */
    public String path;

    /**
     *
     * @param path
     */
    public ResourceLister(String path) {
        this.path = path;
    }

    /**
     * for all elements of java.class.path get a Collection of resources Pattern
     * pattern = Pattern.compile(".*"); gets all resources
     *
     * @param pattern the pattern to match
     * @return the resources in the order they are found
     */
    public List<String> getResources(final Pattern pattern) {
        final ArrayList<String> resources = new ArrayList<>();
        final String classPath = System.getProperty("java.class.path", ".");
        final String[] classPathElements = classPath.split(";");
        for (final String element : classPathElements) {
            resources.addAll(getResources(element, pattern));
        }

        return resources;
    }

    /**
     *
     * @param element
     * @param pattern
     * @return
     */
    private List<String> getResources(final String element, final Pattern pattern) {
        final ArrayList<String> resources = new ArrayList<>();
        final File file = new File(element);
        if (file.isDirectory()) {
            resources.addAll(getResourcesFromDirectory(file, pattern));
        } else {
            resources.addAll(getResourcesFromJarFile(file, pattern));
        }
        return resources;
    }

    /**
     *
     * @param file
     * @param pattern
     * @return
     */
    private List<String> getResourcesFromJarFile(final File file, final Pattern pattern) {
        final ArrayList<String> resources = new ArrayList<>();
        ZipFile zf;
        try {
            zf = new ZipFile(file);
        } catch (final ZipException e) {
            throw new Error(e);
        } catch (final IOException e) {
            throw new Error(e);
        }
        final Enumeration e = zf.entries();
        while (e.hasMoreElements()) {
            final ZipEntry ze = (ZipEntry) e.nextElement();
            String fileName = ze.getName();
            final boolean accept = pattern.matcher(fileName).matches();
            if (accept) {
                // LOGGER.info("Checking file name " + fileName);
                String fileName1 = fileName.replaceAll("/", "").replaceAll("\\\\", "");
                if (fileName1.contains(path.replaceAll("/", ""))) {
                    resources.add(fileName);
                }
            }
        }
        try {
            zf.close();
        } catch (final IOException e1) {
            throw new Error(e1);
        }
        return resources;
    }

    /**
     *
     * @param directory
     * @param pattern
     * @return
     */
    private List<String> getResourcesFromDirectory(final File directory, final Pattern pattern) {
        final ArrayList<String> resources = new ArrayList<>();
        final File[] fileList = directory.listFiles();
        for (final File file : fileList) {
            if (file.isDirectory()) {
                resources.addAll(getResourcesFromDirectory(file, pattern));
            } else {
                try {
                    String fileName = file.getCanonicalPath();
                    final boolean accept = pattern.matcher(fileName).matches();
                    if (accept) {
                        //   LOGGER.info("Checking file name " + fileName);
                        String fileName1 = fileName.replaceAll("/", "").replaceAll("\\\\", "");
                        if (fileName1.contains(path.replaceAll("/", ""))) {
                            resources.add(fileName);
                        }
                    }
                } catch (final IOException e) {
                    throw new Error(e);
                }
            }
        }
        return resources;
    }

    /**
     *
     * @return
     */
    public List<String> getFileNames() {
        List<String> resources = getFilePaths();
        List<String> fileList = new ArrayList<>();
        for (String resource : resources) {
            Integer separatorIndex = 1;
            separatorIndex += resource.replaceAll("\\\\", "/").lastIndexOf("/");
            String fileName = resource.substring(separatorIndex, resource.length());
            if (!fileName.isEmpty()) {
                fileList.add(fileName);
            }
        }
        return fileList;
    }

    /**
     *
     * @return
     */
    public List<String> getFilePaths() {
        Pattern pattern = Pattern.compile(".*");
        final List<String> resourceList = getResources(pattern);
        return resourceList;
    }
}
