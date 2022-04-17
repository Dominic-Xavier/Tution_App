package com.gridViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FolderClass{

    private static final Map<String, List<String>> folderList = new HashMap<>();
    private static final Map<String, String> folderNameList = new HashMap<>();
    private static StringBuffer path = new StringBuffer();
    private static final List<String> list = new ArrayList<>();

    public static void loadData(String parentFolder, List<String> subFolders){
        folderList.put(parentFolder, subFolders);
    }

    public static List<String> getSubFolder(String parentFolder){
        List<String> folder = folderList.get(parentFolder);
        return folder;
    }

    public static void getFolderPath(String folderName){
        list.add(folderName+"/");
    }

    public static String getFolderPath(){
        for (String folders : list)
            path.append(folders+"/");
        return path.toString();
    }
}