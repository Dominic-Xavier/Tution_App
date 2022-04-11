package com.dataHelper;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

    String fileName;

    public PropertyReader(String fileName) {
        this.fileName = fileName;
    }

    public Object getProperty(String key) throws IOException {
        FileReader reader = new FileReader(fileName);
        Properties properties = new Properties();
        properties.load(reader);
        if(properties==null)
            return null;
        return properties.get(key);
    }
}
