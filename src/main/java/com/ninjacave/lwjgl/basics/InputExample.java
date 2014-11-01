package com.ninjacave.lwjgl.basics;

import com.colapietro.chat.lwjgl.AbstractExampleConfigurable;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * Created by Peter Colapietro on 10/27/14.
 *
 * @see <a href="http://lwjgl.org/wiki/index.php?title=LWJGL_Basics_2_(Input)">http://lwjgl.org/wiki/index.php?title=LWJGL_Basics_2_(Input)</a>
 */
public final class InputExample extends AbstractExampleConfigurable {

    @Override
    public void render() {
        // init OpenGL here
        while (!Display.isCloseRequested()) {
            // render OpenGL here
            pollInput();
            Display.update();
        }
    }

    private void pollInput() {

        if (Mouse.isButtonDown(0)) {
            int x = Mouse.getX();
            int y = Mouse.getY();

            LOG.info("MOUSE DOWN @ X: " + x + " Y: " + y);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            LOG.info("SPACE KEY IS DOWN");
        }

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                    LOG.info("A Key Pressed");
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_S) {
                    LOG.info("S Key Pressed");
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                    LOG.info("D Key Pressed");
                }
            } else {
                if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                    LOG.info("A Key Released");
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_S) {
                    LOG.info("S Key Released");
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                    LOG.info("D Key Released");
                }
            }
        }
    }

    public static void main(String[] argv) {
        InputExample inputExample = new InputExample();
        inputExample.executeExampleLoop();
    }
}