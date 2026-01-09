package com.example.join.dto;

import java.util.List;
import java.util.Map;

public class SignupFormDTO {
    private Map<String, List<String>> prefecturesByRegion;

    public SignupFormDTO() {
    }

    public SignupFormDTO(Map<String, List<String>> prefecturesByRegion) {
        this.prefecturesByRegion = prefecturesByRegion;
    }

    public Map<String, List<String>> getPrefecturesByRegion() {
        return prefecturesByRegion;
    }

    public void setPrefecturesByRegion(Map<String, List<String>> prefecturesByRegion) {
        this.prefecturesByRegion = prefecturesByRegion;
    }
}