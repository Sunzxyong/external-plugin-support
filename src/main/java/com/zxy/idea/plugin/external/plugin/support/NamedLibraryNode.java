package com.zxy.idea.plugin.external.plugin.support;

import com.android.utils.Pair;
import com.intellij.icons.AllIcons;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

/**
 * Created by zhengxiaoyong on 2018/06/29.
 */
public class NamedLibraryNode extends ProjectViewNode<Pair<String, String>> {

    private static final String PREFIX = "Plugin: ";

    private static final String SUFFIX = "@jar";

    private static final String DOT_JAR = ".jar";

    protected NamedLibraryNode(Project project, Pair<String, String> pair, ViewSettings viewSettings) {
        super(project, pair, viewSettings);
    }

    @Override
    public boolean canNavigate() {
        return true;
    }

    @Override
    public boolean canNavigateToSource() {
        return canNavigate();
    }

    @NotNull
    @Override
    public Collection<? extends AbstractTreeNode> getChildren() {
        return computeChildren(getProject());
    }

    @Override
    protected void update(PresentationData presentationData) {
        presentationData.setPresentableText(getNodeText());
        presentationData.setIcon(AllIcons.Nodes.PpLibFolder);
    }

    private Set<AbstractTreeNode> computeChildren(Project project) {
        Set<AbstractTreeNode> children = new HashSet<>();
        if (project == null)
            return children;

        String path = getValue().getSecond();
        VirtualFile virtualFile = VfsUtil.findFileByIoFile(new File(path), true);
        if (virtualFile == null)
            return children;

        VirtualFile libraryFile = JarFileSystem.getInstance().getJarRootForLocalFile(virtualFile);

        if (libraryFile == null)
            return children;

        PsiManager psiManager = PsiManager.getInstance(project);
        PsiDirectory libraryPsiDirectory = psiManager.findDirectory(libraryFile);
        children.add(TreeNodeFactory.createNamedLibraryElementNode(project, libraryPsiDirectory, getSettings()));

        return children;
    }

    private String getNodeText() {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX);
        String libraryName = getValue().getFirst();
        sb.append(libraryName);
        String path = getValue().getSecond();
        if (path.endsWith(DOT_JAR))
            sb.append(SUFFIX);
        return sb.toString();
    }

    @Override
    public boolean contains(@NotNull VirtualFile virtualFile) {
        return false;
    }
}
