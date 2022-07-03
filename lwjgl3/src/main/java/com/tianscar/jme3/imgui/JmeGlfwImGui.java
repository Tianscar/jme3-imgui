package com.tianscar.jme3.imgui;

import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import com.jme3.system.Platform;
import com.jme3.system.lwjgl.LwjglWindow;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImInt;
import org.lwjgl.glfw.GLFW;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11C.*;

/**
 * The LWJGL3 platform's JmeImGuiDelegate.
 */
public class JmeGlfwImGui extends JmeImGuiDelegate {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private long windowHandle;

    /**
     * Gets the window handle pointer.
     * @return the window handle
     */
    public long getWindowHandle() {
        return windowHandle;
    }

    /**
     * Gets the ImGuiImplGlfw object.
     * @return the ImGuiGlfw object
     */
    public ImGuiImplGlfw getImGuiGlfw() {
        return imGuiGlfw;
    }

    /**
     * Gets the ImGuiGl3 object.
     * @return the ImGuiGl3 object
     */
    public ImGuiImplGl3 getImGuiGl3() {
        return imGuiGl3;
    }

    /**
     * Creates a JmeGlfwImGui Object with nothing done. You should call {@link JmeGlfwImGui#init(JmeContext, Runnable)} to initialize.
     */
    public JmeGlfwImGui() {}

    /**
     * Method to initialize ImGui.
     * @param context the JmeContext
     * @param beforeInit an interface for load additional ImGui settings
     */
    @Override
    public void init(JmeContext context, Runnable beforeInit) {
        windowHandle = ((LwjglWindow)context).getWindowHandle();
        ImGui.createContext();
        if (beforeInit != null) beforeInit.run();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init(decideGlslVersion());
    }

    /**
     * Method to load generated font textures to GL.
     */
    @Override
    public void refreshFontTexture() {
        ImInt fontW = new ImInt();
        ImInt fontH = new ImInt();
        ImGuiIO imGuiIO = ImGui.getIO();
        ByteBuffer fontData = imGuiIO.getFonts().getTexDataAsRGBA32(fontW, fontH);
        int originalTexture = glGetInteger(GL_TEXTURE_BINDING_2D);
        int fontTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, fontTexture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, fontW.get(), fontH.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, fontData);
        imGuiIO.getFonts().setTexID(fontTexture);
        glBindTexture(GL_TEXTURE_2D, originalTexture);
    }

    /**
     * Method to dispose all used ImGui resources.
     */
    public void dispose() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    /**
     * Decides the glsl version.
     * @return the glsl version string
     */
    protected String decideGlslVersion() {
        return JmeSystem.getPlatform().getOs() == Platform.Os.MacOS ? "#version 150" : "#version 130";
    }

    /**
     * Method called at the beginning of the main cycle.
     * It starts a new ImGui frame.
     */
    public void startFrame() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    /**
     * Method called in the end of the main cycle.
     * It renders ImGui to prepare an updated frame.
     */
    public void endFrame() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }

    }

}
