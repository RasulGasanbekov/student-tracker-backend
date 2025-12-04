package com.edutrack.backend.service;

import com.edutrack.backend.dto.ProgressDto;
import com.edutrack.backend.entity.UserScore;
import com.edutrack.backend.repository.UserScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {
    private final UserScoreRepository userScoreRepository;


    public ProgressDto getProgressByUserId(Long userId) {
        List<UserScore> userScores = userScoreRepository.findByUserIdWithDetails(userId);

        // Рассчитываем общую статистику
        int totalScore = userScores.stream().mapToInt(UserScore::getScore).sum();
        int totalMaxScore = userScores.stream().mapToInt(us -> us.getComponent().getMaxScore()).sum();
        double overallProgress = totalMaxScore > 0 ? (double) totalScore / totalMaxScore * 100 : 0;

        // Группируем по предметам
        Map<String, List<UserScore>> scoresBySubject = userScores.stream()
                .collect(Collectors.groupingBy(us -> us.getComponent().getSubject().getName()));

        List<ProgressDto.SubjectProgress> subjectProgressList = scoresBySubject.entrySet().stream()
                .map(entry -> {
                    String subjectName = entry.getKey();
                    List<UserScore> subjectScores = entry.getValue();

                    int subjectScore = subjectScores.stream().mapToInt(UserScore::getScore).sum();
                    int subjectMaxScore = subjectScores.stream().mapToInt(us -> us.getComponent().getMaxScore()).sum();

                    List<ProgressDto.ComponentProgress> components = subjectScores.stream()
                            .map(us -> {
                                ProgressDto.ComponentProgress comp = new ProgressDto.ComponentProgress();
                                comp.setName(us.getComponent().getName());
                                comp.setScore(us.getScore());
                                comp.setMaxScore(us.getComponent().getMaxScore());
                                comp.setCompleted(us.getCompleted());
                                return comp;
                            })
                            .collect(Collectors.toList());

                    ProgressDto.SubjectProgress subjectProgress = new ProgressDto.SubjectProgress();
                    subjectProgress.setName(subjectName);
                    subjectProgress.setCurrentScore(subjectScore);
                    subjectProgress.setMaxScore(subjectMaxScore);
                    subjectProgress.setComponents(components);
                    return subjectProgress;
                })
                .collect(Collectors.toList());

        ProgressDto.Summary summary = new ProgressDto.Summary();
        summary.setTotalScore(totalScore);
        summary.setTotalMaxScore(totalMaxScore);
        summary.setOverallProgress(Math.round(overallProgress * 100.0) / 100.0); // Округление до 2 знаков

        ProgressDto progressDto = new ProgressDto();
        progressDto.setSummary(summary);
        progressDto.setSubjects(subjectProgressList);

        return progressDto;
    }
}