package com.tianscar.jme3.imgui;

import com.jme3.system.JmeContext;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple helper class for use the Dear ImGui with jMonkeyEngine.
 */
public class JmeImGui {

    private static JmeImGuiDelegate imGuiDelegate;

    private static volatile boolean disposed = false;
    private static volatile boolean initted = false;

    private JmeImGui(){}

    /**
     * Internal use only.
     * @param imGuiDelegate the ImGuiDelegate object
     */
    public static void setImGuiDelegate(JmeImGuiDelegate imGuiDelegate) {
        JmeImGui.imGuiDelegate = imGuiDelegate;
    }

    /**
     * Method to load generated font textures to GL.
     */
    public static void refreshFontTexture() {
        checkDelegate();
        if (disposed) return;
        imGuiDelegate.refreshFontTexture();
    }

    /**
     * Method to initialize ImGui. Cannot be called more than once.
     * @param context the JmeContext
     * @param beforeInit an interface for load additional ImGui settings
     */
    public static void init(JmeContext context, Runnable beforeInit) {
        checkDelegate();
        checkInitted();
        if (disposed) return;
        disposed = false;
        initted = true;
        imGuiDelegate.init(context, beforeInit);
    }

    /**
     * Method to initialize ImGui.
     * @param context the JmeContext
     */
    public static void init(JmeContext context) {
        init(context, null);
    }

    /**
     * Method called at the beginning of the main cycle.
     * It starts a new ImGui frame.
     */
    public static void startFrame() {
        checkDelegate();
        if (disposed) return;
        imGuiDelegate.startFrame();
    }

    /**
     * Method called in the end of the main cycle.
     * It renders ImGui to prepare an updated frame.
     */
    public static void endFrame() {
        checkDelegate();
        if (disposed) return;
        imGuiDelegate.endFrame();
    }

    /**
     * Method to dispose all used ImGui resources. Can be called more than once, with the additional call nothing effects.
     */
    public static void dispose() {
        checkDelegate();
        if (disposed) return;
        disposed = true;
        imGuiDelegate.dispose();
    }

    /**
     * Gets whether the JmeImGui is disposed.
     * @return whether disposed
     */
    public static boolean isDisposed() {
        return disposed;
    }

    /**
     * Gets whether the JmeImGui is initialized.
     * @return whether initialized
     */
    public static boolean isInitted() {
        return initted;
    }

    private static void checkInitted() {
        if (initted) throw new RuntimeException("JmeImGui cannot be initialize twice!");
    }

    private static JmeImGuiDelegate tryLoadDelegate(String className) throws InstantiationException, IllegalAccessException {
        try {
            return (JmeImGuiDelegate) Class.forName(className).newInstance();
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    private static void checkDelegate() {
        if (imGuiDelegate == null) {
            try {
                imGuiDelegate = tryLoadDelegate("com.tianscar.jme3.imgui.JmeGlfwImGui");
                if (imGuiDelegate == null) {
                    imGuiDelegate = tryLoadDelegate("com.tianscar.jme3.imgui.android.JmeAndroidImGui");
                    if (imGuiDelegate == null) {
                        imGuiDelegate = tryLoadDelegate("com.tianscar.jme3.imgui.ios.JmeIosImGui");
                        if (imGuiDelegate == null) {
                            // None of the system delegates were found.
                            Logger.getLogger(JmeImGui.class.getName()).log(Level.SEVERE,
                                    "Failed to find a JmeImGui delegate!\n"
                                            + "Ensure either desktop or android jMEImGui jar is in the classpath.");
                        }
                    }
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(JmeImGui.class.getName()).log(Level.SEVERE, "Failed to create JmeImGui delegate:\n{0}", ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(JmeImGui.class.getName()).log(Level.SEVERE, "Failed to create JmeImGui delegate:\n{0}", ex);
            }
        }
    }

}
