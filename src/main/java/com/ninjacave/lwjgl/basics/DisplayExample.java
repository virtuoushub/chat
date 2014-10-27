package com.ninjacave.lwjgl.basics;

import com.colapietro.chat.lwjgl.AbstractExampleConfigurable;

/**
 * @see <a href="http://lwjgl.org/wiki/index.php?title=LWJGL_Basics_1_(The_Display)">http://lwjgl.org/wiki/index.php?title=LWJGL_Basics_1_(The_Display)</a>
 */
public final class DisplayExample extends AbstractExampleConfigurable {

    public static void main(String[] argv) {
        executeDisplayExample();
    }

    private static void executeDisplayExample() {
        DisplayExample displayExample = new DisplayExample();
        displayExample.executeExampleLoop();
    }

}