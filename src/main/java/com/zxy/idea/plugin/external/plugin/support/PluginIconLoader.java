package com.zxy.idea.plugin.external.plugin.support;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Created by zhengxiaoyong on 2018/06/29.
 */
public class PluginIconLoader {
    public static final Icon Gradle = load("/icons/gradle-svg.svg");

    public PluginIconLoader() {
        throw new RuntimeException("Can not be an instance.");
    }

    private static Icon load(String path) {
        return IconLoader.getIcon(path, PluginIconLoader.class);
    }
}