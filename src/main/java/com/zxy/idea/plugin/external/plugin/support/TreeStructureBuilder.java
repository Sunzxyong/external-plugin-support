package com.zxy.idea.plugin.external.plugin.support;

import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;

/**
 * Created by zhengxiaoyong on 2018/07/01.
 */
public class TreeStructureBuilder {

    private TreeStructureBuilder() {
        throw new RuntimeException("Can not be an instance.");
    }

    public static void buildTreeFromDirectory(Project project, NamedLibraryElementNode parentNode, PsiDirectory directory, ViewSettings settings) {
        if (project == null || parentNode == null || directory == null || settings == null)
            return;

        PsiFile[] psiDirChildrenFiles = directory.getFiles();

        if (psiDirChildrenFiles.length != 0) {
            for (PsiFile psiFile : psiDirChildrenFiles) {
                parentNode.addChildren(new PsiFileNode(project, psiFile, settings));
            }
        }

        PsiDirectory[] psiDirChildrenDirectories = directory.getSubdirectories();

        if (psiDirChildrenDirectories.length != 0) {
            for (PsiDirectory psiDirectory : psiDirChildrenDirectories) {
                NamedLibraryElementNode psiDirectoryNode = new NamedLibraryElementNode(project, psiDirectory, settings);
                parentNode.addChildren(psiDirectoryNode);
                buildTreeFromDirectory(project, psiDirectoryNode, psiDirectory, settings);
            }
        }
    }
}
