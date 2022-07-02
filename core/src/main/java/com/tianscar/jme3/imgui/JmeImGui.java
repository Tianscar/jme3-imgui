package com.tianscar.jme3.imgui;

import com.jme3.system.JmeContext;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JmeImGui {

    private static JmeImGuiDelegate imGuiDelegate;

    private static volatile boolean disposed = false;
    private static volatile boolean initted = false;

    private JmeImGui(){}

    public static void setImGuiDelegate(JmeImGuiDelegate imGuiDelegate) {
        JmeImGui.imGuiDelegate = imGuiDelegate;
    }

    public static void refreshFontTexture() {
        checkDelegate();
        if (disposed) return;
        imGuiDelegate.refreshFontTexture();
    }

    public static void init(JmeContext context) {
        checkDelegate();
        checkInitted();
        if (disposed) return;
        disposed = false;
        initted = true;
        imGuiDelegate.init(context);
    }

    public static void startFrame() {
        checkDelegate();
        if (disposed) return;
        imGuiDelegate.startFrame();
    }

    public static void endFrame() {
        checkDelegate();
        if (disposed) return;
        imGuiDelegate.endFrame();
    }

    public static void dispose() {
        checkDelegate();
        if (disposed) return;
        disposed = true;
        imGuiDelegate.dispose();
    }

    public static boolean isDisposed() {
        return disposed;
    }

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
