package com.github.maskedlaodi.util;

import com.github.maskedlaodi.ann.Service;
import com.github.maskedlaodi.common.Framework;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SysUtil {

    public static Map<String, Object>                   map = new ConcurrentHashMap<String, Object>();
    private static final ClassLoader                    classLoader = Thread.currentThread().getContextClassLoader();

    public static Map getService(String ... strs) {
        List<String> fileNames = null;
        boolean childPackage = true;
        for (String str : strs) {
            str = str.replace(".", "/");
            URL url = classLoader.getResource(str);
            if (null != url) {
                final String type = url.getProtocol();
                if ("file".equalsIgnoreCase(type)) {
                    fileNames = getClassNameByFile(url.getPath(), null, childPackage);
                } else if ("jar".equalsIgnoreCase(type)) {
                    fileNames = getClassNameByJar(url.getPath(), childPackage);
                }
            } else {
                fileNames = getClassNameByJars(((URLClassLoader) classLoader).getURLs(), str, childPackage);
            }

            for (String fileName : fileNames) {
                final String[] split = fileName.split(str);
                fileName = fileName.replace(split[0], "");
                fileName = fileName.replace("/", ".");
                getObjectByClassPath(fileName);
            }
        }
        return map;
    }

    private static void getObjectByClassPath(String classPath) {
        try {
            final Class<?> aClass = classLoader.loadClass(classPath);
            final Service annotation = aClass.getAnnotation(Service.class);
            if (null != annotation) {
                final Class<?>[] interfaceClazzs = classLoader.loadClass(classPath).getInterfaces();
                final String interfaceStr = SimilarityUtils.getSimilarestByClass(classPath, Arrays.asList(interfaceClazzs));
                map.put(interfaceStr, aClass.newInstance());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从项目文件获取某包下所有类
     */
    private static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                if (childPackage) {
                    myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage));
                }
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9,
                            childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                }
            }
        }
        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     */
    private static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (childPackage) {
                        if (entryName.startsWith(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        } else {
                            myPackagePath = entryName;
                        }
                        if (myPackagePath.equals(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myClassName;
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     */
    private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        if (urls != null) {
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                String urlPath = url.getPath();
                // 不必搜索classes文件夹
                if (urlPath.endsWith("classes/")) {
                    continue;
                }
                String jarPath = urlPath + "!/" + packagePath;
                myClassName.addAll(getClassNameByJar(jarPath, childPackage));
            }
        }
        return myClassName;
    }


}
