package com.ninjacave.lwjgl.basics;

import com.colapietro.chat.lwjgl.AbstractExampleConfigurable;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 * Created by Peter Colapietro on 10/27/14.
 *
 * @see <a href="http://www.lwjgl.org/wiki/index.php?title=LWJGL_Basics_5_(Fullscreen)">http://www.lwjgl.org/wiki/index.php?title=LWJGL_Basics_5_(Fullscreen)</a>
 */
public final class FullscreenExample extends AbstractExampleConfigurable {

    private static final int FPS = 60;
    /** position of quad */
    private float x = 400.0f, y = 300.0f;
    /** angle of quad rotation */
    private float rotation = 0.0f;

    /** time at last frame */
    private long lastFrame;

    /** frames per second */
    private int fps;
    /** last fps time */
    private long lastFPS;

    /** is VSync Enabled */
    private boolean vsync;

    @Override
    public void start() {
        super.start();
        initGL(); // init OpenGL
        getDelta(); // call once before loop to initialise lastFrame
        lastFPS = getTime(); // call before loop to initialise fps timer
    }

    @Override
    public void render() {
        while (!Display.isCloseRequested()) {
            int delta = getDelta();

            update(delta);
            renderGL();

            Display.update();
            Display.sync(FPS); // cap fps to 60fps
        }
    }

    private void update(int delta) {
        // rotate quad
        rotation += 0.15f * delta;

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) x -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) x += 0.35f * delta;

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) y -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) y += 0.35f * delta;

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_F) {
                    setDisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT, !Display.isFullscreen());
                }
                else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
                    vsync = !vsync;
                    Display.setVSyncEnabled(vsync);
                }
            }
        }

        // keep quad on the screen
        if (x < 0) x = 0;
        if (x > DISPLAY_WIDTH) x = DISPLAY_WIDTH;
        if (y < 0) y = 0;
        if (y > DISPLAY_HEIGHT) y = DISPLAY_HEIGHT;

        updateFPS(); // update FPS Counter
    }

    /**
     * Set the display mode to be used
     *
     * @param width The width of the display required
     * @param height The height of the display required
     * @param fullscreen True if we want fullscreen mode
     */
    private void setDisplayMode(int width, int height, boolean fullscreen) {

        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width) &&
                (Display.getDisplayMode().getHeight() == height) &&
                (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (int i=0;i<modes.length;i++) {
                    DisplayMode current = modes[i];

                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        // if we've found a match for bpp and frequence against the
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
                                (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width,height);
            }

            if (targetDisplayMode == null) {
                LOG.info("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

        } catch (LWJGLException e) {
            LOG.error("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
        }
    }

    /**
     * Calculate how many milliseconds have passed
     * since last frame.
     *
     * @return milliseconds passed since last frame
     */
    private int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    /**
     * Get the accurate system time
     *
     * @return The system time in milliseconds
     */
    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    /**
     * Calculate the FPS and set it in the title bar
     */
    private void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    private void initGL() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, DISPLAY_WIDTH, 0, DISPLAY_HEIGHT, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    private void renderGL() {
        // Clear The Screen And The Depth Buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // R,G,B,A Set The Color To Blue One Time Only
        GL11.glColor3f(0.5f, 0.5f, 1.0f);

        // draw quad
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glRotatef(rotation, 0f, 0f, 1f);
        GL11.glTranslatef(-x, -y, 0);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - 50, y - 50);
        GL11.glVertex2f(x + 50, y - 50);
        GL11.glVertex2f(x + 50, y + 50);
        GL11.glVertex2f(x - 50, y + 50);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static void main(String[] argv) {
        FullscreenExample fullscreenExample = new FullscreenExample();
        fullscreenExample.executeExampleLoop();
    }
}