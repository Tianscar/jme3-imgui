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

public class JmeGlfwImGui extends JmeImGuiDelegate {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private long windowHandle;

    public long getWindowHandle() {
        return windowHandle;
    }

    public ImGuiImplGlfw getImGuiGlfw() {
        return imGuiGlfw;
    }

    public ImGuiImplGl3 getImGuiGl3() {
        return imGuiGl3;
    }

    public JmeGlfwImGui() {}

    /**
     * Method to initialize imGui.
     */
    @Override
    public void init(JmeContext context) {
        windowHandle = ((LwjglWindow)context).getWindowHandle();
        ImGui.createContext();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init(decideGlslVersion());
    }

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
     * Method to dispose all used imGui resources.
     */
    public void dispose() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    protected String decideGlslVersion() {
        return JmeSystem.getPlatform().getOs() == Platform.Os.MacOS ? "#version 150" : "#version 130";
    }

    /**
     * Method called at the beginning of the main cycle.
     * It clears OpenGL buffer and starts an ImGui frame.
     */
    public void startFrame() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    /**
     * Method called in the end of the main cycle.
     * It renders ImGui and swaps GLFW buffers to show an updated frame.
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
