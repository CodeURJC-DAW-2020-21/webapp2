package es.dawgroup2.juding.competitions;

import es.dawgroup2.juding.attendances.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;

@Component
public class CompetitionDataLoader {
    @Autowired
    CompetitionRepository competitionRepository;

    @PostConstruct
    public void competitionLoader(){
        competitionRepository.save(new Competition("Copa Platano","nah",45,50, new Timestamp(953596800),new Timestamp(953596800),"JU-1234567890", Attendance.C));
        competitionRepository.save(new Competition("Campeonato Champiñon", "nah", 45, 50, new Timestamp(953596800), new Timestamp(953596800), "JU-1234567890", Attendance.N));
        competitionRepository.save(new Competition("Campeonato Placeholder", "Buenos dias", 80, 100, new Timestamp(953596800), new Timestamp(953596800), "JU-1234567870", Attendance.R));

    }
}
