import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.tianscar.jme3.imgui.JmeImGui;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;

public class MultiViewportApplication extends SimpleApplication {

    public static void main(String[] args) {
        MultiViewportApplication app = new MultiViewportApplication();
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
        JmeImGui.init(getContext(), new Runnable() {
            @Override
            public void run() {
                final ImGuiIO io = ImGui.getIO();
                io.setIniFilename(null);                                // We don't want to save .ini file
                io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);    // Enable Multi-Viewport / Platform Windows
                io.setConfigViewportsNoAutoMerge(true);
            }
        });


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
            ImGui.text("Hello World!");
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

}
