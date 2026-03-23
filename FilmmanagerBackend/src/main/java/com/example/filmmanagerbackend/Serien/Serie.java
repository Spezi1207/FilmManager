package com.example.filmmanagerbackend.Serien;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Serie {
    @Id

    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private Integer id;
    private String title;
    private String genre;
    private Integer year;
    private Integer episodes;
    private Integer seasons;
    private String watchable;
    private String evaluation;



}
