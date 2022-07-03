package com.tianscar.jme3.imgui;

import com.jme3.system.JmeContext;

/**
 * The delegate class to working with ImGui.
 */
public abstract class JmeImGuiDelegate {

    /**
     * Method to initialize ImGui.
     * @param context the JmeContext
     * @param beforeInit an interface for load additional ImGui settings
     */
    public abstract void init(JmeContext context, Runnable beforeInit);

    /**
     * Method to load generated font textures to GL.
     */
    public abstract void refreshFontTexture();

    /**
     * Method called at the beginning of the main cycle.
     * It starts a new ImGui frame.
     */
    public abstract void startFrame();

    /**
     * Method called in the end of the main cycle.
     * It renders ImGui to prepare an updated frame.
     */
    public abstract void endFrame();

    /**
     * Method to dispose all used ImGui resources.
     */
    public abstract void dispose();

}
