package com.zxy.idea.plugin.external.plugin.support;

import com.android.tools.idea.gradle.project.GradleProjectInfo;
import com.android.tools.idea.navigator.nodes.AndroidViewProjectNode;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.ProjectViewProjectNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.List;


/**
 * Created by zhengxiaoyong on 2018/06/25.
 */
public class GradlePluginTreeStructureProvider implements TreeStructureProvider {

    @NotNull
    @Override
    public Collection<AbstractTreeNode> modify(@NotNull AbstractTreeNode parent, @NotNull Collection<AbstractTreeNode> children, ViewSettings settings) {

        Project project = parent.getProject();
        if (project == null)
            return children;

        if ((parent instanceof ProjectViewProjectNode || parent instanceof AndroidViewProjectNode) && GradleProjectInfo.getInstance(project).isBuildWithGradle()) {
            List<AbstractTreeNode> modifiedChildren = ContainerUtil.newArrayList();
            modifiedChildren.addAll(children);
            modifiedChildren.add(TreeNodeFactory.createExternalPluginsNode(project, settings));
            return modifiedChildren;
        }

        return children;
    }

    @Nullable
    @Override
    public Object getData(Collection<AbstractTreeNode> selected, String dataName) {
        return null;
    }
}
