package com.ninjacave.lwjgl.basics;

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
            System.exit(0);
        }
        // init OpenGL here
        while (!Display.isCloseRequested()) {
            // render OpenGL here
            Display.update();
        }
        Display.destroy();
    }
 
    public static void main(String[] argv) {
        DisplayExample displayExample = new DisplayExample();
        displayExample.start();
    }
}