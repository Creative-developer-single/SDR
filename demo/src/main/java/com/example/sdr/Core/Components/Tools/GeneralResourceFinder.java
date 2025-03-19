package com.example.sdr.Core.Components.Tools;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

public class GeneralResourceFinder {

    /**
     * 获取 `resources` 目录下的文件的**绝对路径**
     * 适用于 `Paths.get(...)`，如果文件在 JAR 包中，可能无法使用
     *
     * @param fileName 资源文件名，例如 `"config.json"`
     * @return 资源文件的绝对路径
     * @throws IllegalArgumentException 如果文件不存在
     */
    public static String getResourcePath(String fileName) {
        try {
            return Paths.get(Objects.requireNonNull(
                    GeneralResourceFinder.class.getClassLoader().getResource(fileName)).toURI()).toString();
        } catch (URISyntaxException | NullPointerException e) {
            throw new IllegalArgumentException("文件未找到: " + fileName, e);
        }
    }


    /**
     * 获取 `resources` 目录下文件的 **InputStream**
     * 适用于 **JAR 包和普通文件**
     *
     * @param fileName 资源文件名，例如 `"config.json"`
     * @return 资源文件的 InputStream
     * @throws IllegalArgumentException 如果文件不存在
     */
    public static InputStream getResourceStream(String fileName) {
        InputStream inputStream = GeneralResourceFinder.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("文件未找到: " + fileName);
        }
        return inputStream;
    }


    public String getFilePath(String fileName) {
        try {
            return Paths.get(this.getClass().getResource(fileName).toURI()).toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("文件未找到: " + fileName, e);
        }
    }
    
}
