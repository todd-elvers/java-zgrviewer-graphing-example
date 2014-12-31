package com.toddelvers.graphing.core.file;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class GraphFileLocator {
    private static Logger log = Logger.getLogger(GraphFileLocator.class);

    public static String determineAbsoluteFilePathFor(String relativeFilePath) throws FileNotFoundException {
        log.debug("Looking for absolute file path for resource '" + relativeFilePath + "'...");

        String absoluteFilePath = lookInResourcesDirectory(relativeFilePath);
        if (absoluteFilePath == null) {
            log.debug("Unable to find '" + relativeFilePath + "' in resources directory, checking locally...");
            absoluteFilePath = lookLocally(relativeFilePath);
            if (absoluteFilePath == null) {
                log.debug("Unable to find '" + relativeFilePath + "' locally.");
                throw new FileNotFoundException("Unable to locate '" + relativeFilePath + "' in resources directory or locally. Program will now terminate.");
            }
        }

        log.debug("Found resource at: '" + absoluteFilePath + "'.");
        return removeLeadingSlashIfNecessary(absoluteFilePath);
    }


    private static String lookInResourcesDirectory(String relativeFilePath) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(relativeFilePath);
        return (resource != null && StringUtils.isNotEmpty(resource.getFile())) ? resource.getFile() : null;
    }

    private static String lookLocally(String relativeFilePath) {
        File resource = new File(new File("").getAbsolutePath(), relativeFilePath);
        return (resource.exists() && resource.canExecute()) ? resource.getAbsolutePath() : null;
    }

    /**
     * Resources located via .getResource() have a leading slash, and since these paths will be used
     * as command line parameters we need to remove them or we'll get a silent failure.
     */
    private static String removeLeadingSlashIfNecessary(String absoluteFilePath){
        return absoluteFilePath.startsWith("/") ? absoluteFilePath.substring(1) : absoluteFilePath;
    }
}
