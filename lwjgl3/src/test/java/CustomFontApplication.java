import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.tianscar.jme3.imgui.JmeImGui;
import imgui.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class CustomFontApplication extends SimpleApplication {

    public static void main(String[] args) {
        CustomFontApplication app = new CustomFontApplication();
        app.setShowSettings(false);
        AppSettings settings = new AppSettings(true);
        settings.setTitle("My Awesome Game with Dear ImGui");
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        getFlyByCamera().setEnabled(false);
        // Initialize the ImGui
        JmeImGui.init(getContext());
        ImGui.getIO().setIniFilename(null);
        // Load custom font
        initFonts(ImGui.getIO());

        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        // Start the ImGui frame
        JmeImGui.startFrame();
        // ----------- ImGui logic here ---------------
        if (!JmeImGui.isDisposed()) {
            ImGui.text("你好，世界！");
        }
        // --------------------------------------------
        // End the ImGui frame
        JmeImGui.endFrame();
    }

    @Override
    public void requestClose(boolean esc) {
        // Dispose the ImGui
        JmeImGui.dispose();
        super.requestClose(esc);
    }

    /**
     * Example of fonts configuration
     * For more information read: <a href="https://github.com/ocornut/imgui/blob/33cdbe97b8fd233c6c12ca216e76398c2e89b0d8/docs/FONTS.md">...</a>
     */
    private static void initFonts(ImGuiIO imGuiIO) {
        imGuiIO.getFonts().addFontDefault(); // Add default font for latin glyphs

        // You can use the ImFontGlyphRangesBuilder helper to create glyph ranges based on text input.
        // For example: for a game where your script is known, if you can feed your entire script to it (using addText) and only build the characters the game needs.
        // Here we are using it just to combine all required glyphs in one place
        ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder();          // Glyphs ranges provide
        rangesBuilder.addRanges(imGuiIO.getFonts().getGlyphRangesDefault());
        rangesBuilder.addRanges(imGuiIO.getFonts().getGlyphRangesCyrillic());
        rangesBuilder.addRanges(imGuiIO.getFonts().getGlyphRangesJapanese());
        rangesBuilder.addText("你好，世界！");
        //rangesBuilder.addRanges(io.getFonts().getGlyphRangesChineseSimplifiedCommon()); // Seems broken
        //rangesBuilder.addRanges(io.getFonts().getGlyphRangesChineseFull());             // Seems broken

        // Font config for custom fonts
        ImFontConfig imFontConfig = new ImFontConfig();
        imFontConfig.setMergeMode(true);      // Merge Default, Cyrillic, Japanese ranges and manual specific chars

        final short[] glyphRanges = rangesBuilder.buildRanges();
        //
        ImFont imFont = imGuiIO.getFonts().addFontFromMemoryTTF(Objects.requireNonNull(getResourcesAsBytes("droid_sans.ttf")),
                14f, imFontConfig, glyphRanges);
        imGuiIO.getFonts().build();           // Build custom font
        imGuiIO.setFontDefault(imFont);       // Set custom font to default

        JmeImGui.refreshFontTexture();        // Don't forget to refresh the font texture!

        imFontConfig.destroy();               // Destroy the font config
    }

    private static byte[] getResourcesAsBytes(String pathname) {
        if (!pathname.startsWith("/")) pathname = "/" + pathname;
        try {
            return Files.readAllBytes(Paths.get(Objects.requireNonNull(CustomFontApplication.class.getResource(pathname)).toURI()));
        } catch (IOException | URISyntaxException ignored) {
        }
        return null;
    }

}
