package com.colapietro.chat.lwjgl;

import static com.colapietro.chat.lwjgl.NativesHelper.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Peter Colapietro on 10/27/14.
 */
public abstract class AbstractExampleConfigurable implements ExampleConfigurable {

    protected final static Logger LOG = LoggerFactory.getLogger(AbstractExampleConfigurable.class);
    protected final int DISPLAY_WIDTH = 800;
    protected final int DISPLAY_HEIGHT = 600;

    @Override
    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
            Display.create();
        } catch (LWJGLException e) {
            LOG.error("Failed to create display. {}", e);
            System.exit(-1);//FIXME Define a spec for status codes.
        }
    }

    @Override
    public void render() {
        // init OpenGL here
        while (!Display.isCloseRequested()) {
            // render OpenGL here
            Display.update();
        }
    }

    @Override
    public void finish() {
        Display.destroy();
    }

    @Override
    public void executeExampleLoop() {
        setUpNatives();
        start();
        render();
        finish();
    }
}
