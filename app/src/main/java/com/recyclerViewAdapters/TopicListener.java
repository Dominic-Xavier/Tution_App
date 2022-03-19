package com.recyclerViewAdapters;

import java.util.List;

@FunctionalInterface
public interface TopicListener {
    void selectTopic(List<String> list);
}
