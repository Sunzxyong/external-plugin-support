package com.zxy.idea.plugin.external.plugin.support;

import com.android.utils.Pair;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

/**
 * Created by zhengxiaoyong on 2018/06/29.
 */
public class TreeNodeFactory {

    private TreeNodeFactory() {
        throw new RuntimeException("Can not be an instance.");
    }

    public static AbstractTreeNode createExternalPluginsNode(Project project, ViewSettings settings) {
        return new ExternalPluginsNode(project, settings);
    }

    public static AbstractTreeNode createNamedLibraryNode(Project project, Pair<String, String> value, ViewSettings settings) {
        return new NamedLibraryNode(project, value, settings);
    }

    public static AbstractTreeNode createNamedLibraryElementNode(Project project, PsiDirectory value, ViewSettings settings) {
        return new NamedLibraryElementNode(project, value, settings);
    }

}
