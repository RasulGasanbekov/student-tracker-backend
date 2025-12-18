package com.edutrack.backend.dto;


import lombok.Data;
import java.util.List;

@Data
public class ProgressDto {
    private Summary summary;
    private List<SubjectProgress> subjects;

    @Data
    public static class Summary {
        private Integer totalScore;
        private Integer totalMaxScore;
        private Integer overallProgress;
    }

    @Data
    public static class SubjectProgress {
        private String name;
        private Integer currentScore;
        private Integer maxScore;
        private List<ComponentProgress> components;
    }

    @Data
    public static class ComponentProgress {
        private String name;
        private Integer score;
        private Integer maxScore;
        private Boolean completed;
    }
}
