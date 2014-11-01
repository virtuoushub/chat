package com.colapietro.chat.lwjgl;

/**
 * Created by Peter Colapietro on 10/27/14.
 *
 * @see <a href="http://lwjgl.org/wiki/index.php?title=LWJGL_Basics_5_(Fullscreen)">http://lwjgl.org/wiki/index.php?title=LWJGL_Basics_5_(Fullscreen)</a>
 */
public enum BitsPerPixel {

    WINDOWS(32),
    NIX(24),
    SIXTEEN_BIT(16),
    EIGHT_BIT(8),
    FOUR_BIT(4);

    private int bitsPerPixel;

    private BitsPerPixel(int bitsPerPixel) {
        this.bitsPerPixel = bitsPerPixel;
    }


}
