package com.zxy.idea.plugin.external.plugin.support;

import com.android.utils.Pair;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.gradle.service.GradleBuildClasspathManager;

import java.util.*;


/**
 * Created by zhengxiaoyong on 2018/06/25.
 */
public class ExternalPluginsNode extends ProjectViewNode<String> {

    private static final String NODE_NAME = "External Plugins";

    /**
     * Creates an instance of the project view node.
     *
     * @param project      the project containing the node.
     * @param viewSettings the settings of the project view.
     */
    public ExternalPluginsNode(@NotNull Project project, ViewSettings viewSettings) {
        super(project, NODE_NAME, viewSettings);
    }

    @Override
    public boolean contains(@NotNull VirtualFile file) {
        return false;
    }

    @NotNull
    @Override
    public Collection<? extends AbstractTreeNode> getChildren() {
        Project project = getProject();
        List<AbstractTreeNode> children = new ArrayList<>();

        if (project == null)
            return children;

        List<VirtualFile> files = GradleBuildClasspathManager.getInstance(project).getAllClasspathEntries();

        Set<String> dependencies = BuildFileDataProvider.getBuildScriptDependencies(project);

        Set<String> results = new HashSet<>();

        for (String dependency : dependencies) {
            if (dependency == null || dependency.length() == 0)
                continue;

            results.clear();

            for (VirtualFile file : files) {
                if (file == null)
                    continue;

                String fileUrl = file.getPresentableUrl();

                if (fileUrl.length() == 0)
                    continue;

                if (fileUrl.contains(dependency.replace(":", "/"))) {
                    results.add(fileUrl);
                }
            }

            if (results.isEmpty())
                continue;

            String fileUrl = ClasspathFilter.select(results);
            if (fileUrl == null)
                continue;

            children.add(TreeNodeFactory.createNamedLibraryNode(project, Pair.of(dependency, fileUrl), getSettings()));
            System.out.println("dependencies----->" + fileUrl);

        }

        return children;
    }

    @Override
    protected void update(PresentationData presentation) {
        presentation.setPresentableText(NODE_NAME);
        presentation.setIcon(PluginIconLoader.Gradle);
    }
}
