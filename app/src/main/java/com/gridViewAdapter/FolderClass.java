package com.gridViewAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FolderClass {

    private static final Map<String, List<String>> folderList = new HashMap<>();
    private static final Map<String, String> folderNameList = new HashMap<>();
    private static final StringBuffer stringBuffer = new StringBuffer();

    public static void loadData(String parentFolder, List<String> subFolders){
        folderList.put(parentFolder, subFolders);
    }

    public static List<String> getSubFolder(String parentFolder){
        List<String> folder = folderList.get(parentFolder);
        return folder;
    }

    public static void folderPath(String folderName){
        stringBuffer.append(folderName+"/");
        folderNameList.put(folderName, stringBuffer.toString());
    }

    public static String getPath(String folderName){
        return folderNameList.get(folderName);
    }
}