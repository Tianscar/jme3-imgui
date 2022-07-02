package com.tianscar.jme3.imgui;

import com.jme3.system.JmeContext;

public abstract class JmeImGuiDelegate {

    public abstract void init(JmeContext context);

    public abstract void refreshFontTexture();

    public abstract void startFrame();

    public abstract void endFrame();

    public abstract void dispose();

}
