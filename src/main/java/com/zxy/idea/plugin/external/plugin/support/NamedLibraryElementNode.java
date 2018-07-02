package com.zxy.idea.plugin.external.plugin.support;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileSystemItemFilter;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhengxiaoyong on 2018/07/01.
 */
public class NamedLibraryElementNode extends PsiDirectoryNode {

    private static final String SUFFIX = " library root";

    private Set<AbstractTreeNode> mChildren = new HashSet<>();

    public NamedLibraryElementNode(Project project, PsiDirectory value, ViewSettings viewSettings) {
        super(project, value, viewSettings);
    }

    public NamedLibraryElementNode(Project project, PsiDirectory value, ViewSettings viewSettings, @Nullable PsiFileSystemItemFilter filter) {
        super(project, value, viewSettings, filter);
    }

    public void addChildren(AbstractTreeNode node) {
        if (node == null)
            return;
        mChildren.add(node);
    }

    @Override
    public Collection<AbstractTreeNode> getChildrenImpl() {
        mChildren.clear();

        TreeStructureBuilder.buildTreeFromDirectory(getProject(), this, getValue(), getSettings());

        return mChildren;
    }

    @Override
    protected void updateImpl(PresentationData data) {
        super.updateImpl(data);
        data.setPresentableText(getValue().getName());
    }
}
