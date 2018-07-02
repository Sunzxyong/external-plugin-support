package com.zxy.idea.plugin.external.plugin.support;

import com.android.tools.idea.gradle.dsl.api.BuildScriptModel;
import com.android.tools.idea.gradle.dsl.api.GradleBuildModel;
import com.android.tools.idea.gradle.dsl.api.GradleModelProvider;
import com.android.tools.idea.gradle.dsl.api.dependencies.ArtifactDependencyModel;
import com.android.tools.idea.gradle.dsl.api.dependencies.DependenciesModel;
import com.android.tools.idea.gradle.dsl.api.ext.ResolvedPropertyModel;
import com.android.tools.idea.gradle.dsl.api.values.GradleNotNullValue;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhengxiaoyong on 2018/06/25.
 */
public class BuildFileDataProvider {

    /**
     * 'classpath' is the configuration name used to specify buildscript dependencies.
     */
    private static final String CLASSPATH_CONFIGURATION_NAME = "classpath";

    public static List<GradleBuildModel> getGradleBuildModels(Project project) {
        Module[] modules = ModuleManager.getInstance(project).getModules();

        List<GradleBuildModel> buildModels = new ArrayList<>();

        for (Module module : modules) {
            String moduleDir = ModuleUtil.getModuleDirPath(module);

            File buildFile = new File(moduleDir, "build.gradle");

            if (!buildFile.exists() || !buildFile.isFile())
                continue;

            VirtualFile virtualFile = VfsUtil.findFileByIoFile(buildFile, true);
            if (virtualFile == null) {
                throw new RuntimeException("Failed to find " + buildFile.getPath());
            }

            GradleBuildModel buildModel;

            try {
                buildModel = GradleBuildModel.parseBuildFile(virtualFile, project);
            } catch (Exception e) {
                buildModel = GradleModelProvider.get().parseBuildFile(virtualFile, project);
            }

            buildModels.add(buildModel);
        }

        return buildModels;

    }

    public static List<BuildScriptModel> getBuildScriptModels(Project project) {
        List<GradleBuildModel> buildModels = getGradleBuildModels(project);

        List<BuildScriptModel> buildScriptModels = new ArrayList<>();

        for (GradleBuildModel model : buildModels) {
            buildScriptModels.add(model.buildscript());
        }

        return buildScriptModels;
    }

    public static List<String> getApplyPlugins(Project project) {
        List<GradleBuildModel> buildModels = getGradleBuildModels(project);

        List<String> plugins = new ArrayList<>();

        for (GradleBuildModel model : buildModels) {
            List<GradleNotNullValue<String>> gradleNotNullValues = model.appliedPlugins();
            for (GradleNotNullValue<String> notNullValue : gradleNotNullValues) {
                plugins.add(notNullValue.value());
            }
        }
        return plugins;
    }


    public static List<DependenciesModel> getBuildScriptDependencyModels(Project project) {
        List<BuildScriptModel> buildScriptModels = getBuildScriptModels(project);

        List<DependenciesModel> buildScriptDependencyModels = new ArrayList<>();

        for (BuildScriptModel model : buildScriptModels) {
            buildScriptDependencyModels.add(model.dependencies());
        }

        return buildScriptDependencyModels;
    }

    public static Set<String> getBuildScriptDependencies(Project project) {
        List<DependenciesModel> dependenciesModels = getBuildScriptDependencyModels(project);

        Set<String> dependencies = new HashSet<>();

        for (DependenciesModel model : dependenciesModels) {

            List<ArtifactDependencyModel> artifactDependencyModels = model.artifacts(CLASSPATH_CONFIGURATION_NAME);

            for (ArtifactDependencyModel artifactDependencyModel : artifactDependencyModels) {

                String value = null;
                try {
                    //compat AS-3.2
                    Method method = ArtifactDependencyModel.class.getDeclaredMethod("compactNotation");
                    method.setAccessible(true);
                    Object object = method.invoke(artifactDependencyModel);
                    if (object instanceof String) {
                        value = (String) object;
                    } else if (object instanceof GradleNotNullValue) {
                        value = (String) ((GradleNotNullValue) object).value();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (value == null || value.trim().length() == 0) {
                    StringBuilder builder = new StringBuilder();

                    Object group = artifactDependencyModel.group();
                    Object name = artifactDependencyModel.name();
                    Object version = artifactDependencyModel.version();
                    //compat AS-3.2
                    if (group instanceof ResolvedPropertyModel && name instanceof ResolvedPropertyModel && version instanceof ResolvedPropertyModel) {
                        try {
                            Method method = ResolvedPropertyModel.class.getDeclaredMethod("getResultModel");
                            method.setAccessible(true);
                            builder.append(method.invoke(group).toString());
                            builder.append(":");
                            builder.append(method.invoke(name).toString());
                            builder.append(":");
                            builder.append(method.invoke(version).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (group instanceof GradleNotNullValue && name instanceof GradleNotNullValue && version instanceof GradleNotNullValue) {
                        builder.append(((GradleNotNullValue) group).value());
                        builder.append(":");
                        builder.append(((GradleNotNullValue) name).value());
                        builder.append(":");
                        builder.append(((GradleNotNullValue) version).value());
                    }

                    value = builder.toString();
                }

                dependencies.add(value);
            }
        }

        return dependencies;
    }

}
