package com.recyclerViewAdapters;

import java.util.Map;

public interface Attendance {
    //Here key is studentID and Value is studentName
    void getPresentStudents(Map<String, Object> present);
    void getAbsentStudents(Map<String, Object> absent);
    void getLateStudents(Map<String, Object> late);
}
