# jME3-ImGui
A simple helper library for use the Dear ImGui with jMonkeyEngine. Currently supports LWJGL3 platform only.  
Supported jMonkeyEngine version: v3.5.2 or later

## Add the library to your project (gradle)
1. Add the Maven Central repository (if not exist) to your build file:
```groovy
repositories {
    mavenCentral()
}
```

2. Add the dependency:
```groovy
dependencies {
    implementation 'com.tianscar.jme3:jme3-imgui:1.0.0'
}
```

## Usage
```java
public class Usage extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        // Initialize the ImGui
        JmeImGui.init(getContext());
    }

    @Override
    public void simpleRender(RenderManager rm) {
        // Start the ImGui frame
        JmeImGui.startFrame();
        // ----------- ImGui logic here ---------------
        if (!JmeImGui.isDisposed()) {
            // Draw widgets here
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
```
[JavaDoc](https://docs.tianscar.com/jme3-imgui)  
[Examples](lwjgl3/src/test/java/com/tianscar/jme3/imgui/test)

## License
[MIT](/LICENSE) (c) Tianscar

### Dependencies
imgui-java - [Apache-2.0](https://github.com/SpaiR/imgui-java/blob/main/LICENSE)  
jMonkeyEngine - [BSD-3-Clause](https://github.com/jMonkeyEngine/jmonkeyengine/blob/master/LICENSE.md)

### Resources be used for test
[Droid Sans Fallback](/lwjgl3/src/test/resources/droid_sans.ttf) - Apache-2.0
