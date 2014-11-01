package com.colapietro.chat.lwjgl;

import com.ninjacave.lwjgl.basics.DisplayExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 * Created by Peter Colapietro on 10/27/14.
 *
 * @see <a href="http://stackoverflow.com/a/16240426">Get URL of file in .jar that the program is running</a>
 */
public class NativesHelper {

    private final static Logger LOG = LoggerFactory.getLogger(NativesHelper.class);

    public static void setUpNatives() {
        LOG.info("java.library.path: {}", System.getProperty("java.library.path"));
        LOG.info("java.class.path: {}", System.getProperty("java.class.path"));

        URL url = DisplayExample.class.getProtectionDomain().getCodeSource().getLocation();
        LOG.info("url: {}", url);
        try {
            final String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
            String pathToAdd = jarPath;
            if(pathToAdd.contains("classes/main/")) {
                pathToAdd = pathToAdd.replace("classes/main/","libs/chat-0.1.0.jar");
            }
            LOG.info("pathToAdd: {}", pathToAdd);
            URL imgurl = new URL("file",pathToAdd,"/");
            LOG.info("imgurl: {}", imgurl);
            String foo = imgurl.toString().replace(".jar","");
            setLibraryPath(foo);
        } catch(MalformedURLException e) {
            LOG.error("{}", e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("{}", e);
        }
    }

    /**
     * Sets the java library path to the specified path
     *
     * @param path the new library path
     * @throws Exception
     */
    private static void setLibraryPath(String path) {
        System.setProperty("java.library.path", path);
        LOG.info("java.library.path: {}", System.getProperty("java.library.path"));

        try {
            //set sys_paths to null
            final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
        } catch (NoSuchFieldException e) {
            LOG.error("{}", e);
        } catch (IllegalAccessException e) {
            LOG.error("{}", e);
        }
        LOG.info("java.library.path: {}", System.getProperty("java.library.path"));

    }

    /**
     * @see <a href="http://fahdshariff.blogspot.jp/2011/08/changing-java-library-path-at-runtime.html">Changing Java Library Path at Runtime</a>
     * Adds the specified path to the java library path
     *
     * @param pathToAdd the path to add
     * @throws Exception
     */
    private static void addLibraryPath(String pathToAdd) {
        try {
            final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
            usrPathsField.setAccessible(true);

            //get array of paths
            final String[] paths = (String[])usrPathsField.get(null);

            //check if the path to add is already present
            for(String path : paths) {
                if(path.equals(pathToAdd)) {
                    return;
                }
            }

            //add the new path
            final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
            newPaths[newPaths.length-1] = pathToAdd;
            usrPathsField.set(null, newPaths);
        } catch (NoSuchFieldException e) {
            LOG.error("{}", e);
        } catch (IllegalAccessException e) {
            LOG.error("{}", e);
        }
    }

}
