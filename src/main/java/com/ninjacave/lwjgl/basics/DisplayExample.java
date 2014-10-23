package com.ninjacave.lwjgl.basics;

import java.lang.reflect.Field;
import java.lang.ClassLoader;
import java.util.Arrays;
import java.net.URL;
import java.net.URLDecoder;
import java.net.MalformedURLException;
import java.io.UnsupportedEncodingException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class DisplayExample {
    
    final static Logger log = LoggerFactory.getLogger(DisplayExample.class);

    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(800,600));
            Display.create();
        } catch (LWJGLException e) {
            log.error("Failed to create display. {}",e);
            System.exit(-1);//FIXME Define a spec for status codes.
        }
        // init OpenGL here
        while (!Display.isCloseRequested()) {
            // render OpenGL here
            Display.update();
        }
        Display.destroy();
    }
 
    /**
        * @see <a href="http://stackoverflow.com/a/16240426">Get URL of file in .jar that the program is running</a>
        */
    public static void main(String[] argv) {
        log.info("java.library.path: {}",System.getProperty("java.library.path"));
        log.info("java.class.path: {}",System.getProperty("java.class.path"));
        
        URL url = DisplayExample.class.getProtectionDomain().getCodeSource().getLocation();
        log.info("url: {}",url);
        try {
            String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
            final String pathToAdd = jarPath;
            log.info("pathToAdd: {}", pathToAdd);
            URL imgurl = new URL("file",jarPath,"/");
            log.info("imgurl: {}", imgurl);
            String foo = imgurl.toString().replace(".jar","");
            setLibraryPath(foo);
            DisplayExample displayExample = new DisplayExample();
            displayExample.start();
        } catch(MalformedURLException e) {
            log.error("{}",e);
        } catch (UnsupportedEncodingException e) {
            log.error("{}",e);
        }
    }
    
    /**
     * Sets the java library path to the specified path
     *
     * @param path the new library path
     * @throws Exception
     */
    public static void setLibraryPath(String path) {
        System.setProperty("java.library.path", path);
        log.info("java.library.path: {}",System.getProperty("java.library.path"));
 
        try {
        //set sys_paths to null
        final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
        sysPathsField.setAccessible(true);
        sysPathsField.set(null, null);
        } catch (NoSuchFieldException e) {
            log.error("{}",e);
        } catch (IllegalAccessException e) {
            log.error("{}",e);
        }
        log.info("java.library.path: {}",System.getProperty("java.library.path"));
        
    }
    
    /**
    * @see <a href="http://fahdshariff.blogspot.jp/2011/08/changing-java-library-path-at-runtime.html">Changing Java Library Path at Runtime</a>
    * Adds the specified path to the java library path
    *
    * @param pathToAdd the path to add
    * @throws Exception
    */
    public static void addLibraryPath(String pathToAdd) {
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
            log.error("{}",e);
        } catch (IllegalAccessException e) {
            log.error("{}",e);
        }
    }
}