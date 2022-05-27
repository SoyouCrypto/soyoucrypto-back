package com.finalproject.chorok.todo.model;

import com.finalproject.chorok.login.model.User;
import com.finalproject.chorok.myPlant.model.MyPlant;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spraying_day")
public class Spraying {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sprayingDayNo;

    private LocalDate sprayingDay;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "my_plant_no",referencedColumnName = "my_plant_no")
    private MyPlant myPlant;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private User user;

    public Spraying(LocalDate sprayingDay, MyPlant myPlant, User user){
        this.sprayingDay = sprayingDay;
        this.myPlant = myPlant;
        this.user = user;
    }
}
