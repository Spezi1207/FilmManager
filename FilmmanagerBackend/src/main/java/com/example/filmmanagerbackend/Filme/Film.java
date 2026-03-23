package com.example.filmmanagerbackend.Filme;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class Film {
@Id

@GeneratedValue(
        strategy = GenerationType.IDENTITY
)

    private Integer id;
    private String title;
    private String genre;
    private Integer year;
    private Integer duration;
    private String watchable;
    private String evaluation;


}
