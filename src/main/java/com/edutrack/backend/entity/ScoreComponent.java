package com.edutrack.backend.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "score_components")
@Data
public class ScoreComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "max_score", nullable = false)
    private Integer maxScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(mappedBy = "component", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserScore> userScores = new ArrayList<>();
}
