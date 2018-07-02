package com.zxy.idea.plugin.external.plugin.support;

import com.android.utils.Pair;

import java.util.Set;

/**
 * Created by zhengxiaoyong on 2018/07/01.
 */
public class ClasspathFilter {

    private static final String SOURCES_DOT_JAR = "-sources.jar";

    public static String select(Set<String> paths) {
        if (paths == null || paths.isEmpty())
            return null;

        Pair<Boolean, String> pair = null;

        String firstPath = null;

        for (String path : paths) {

            if (firstPath == null) {
                firstPath = path;
            }
            if (path.endsWith(SOURCES_DOT_JAR)) {
                pair = Pair.of(true, path);
                break;
            }
        }

        String path;

        if (pair != null && pair.getFirst()) {
            path = pair.getSecond();
        } else {
            path = firstPath;
        }
        return path;
    }

}
