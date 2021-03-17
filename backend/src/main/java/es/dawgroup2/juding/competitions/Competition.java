package es.dawgroup2.juding.competitions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.dawgroup2.juding.auxTypes.attendances.Attendance;
import es.dawgroup2.juding.fight.Fight;
import es.dawgroup2.juding.users.User;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Competition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idCompetition;

    @Column(nullable = false)
    private String shortName;

    @Column(nullable = false, length = 2 * 1024 * 1024)
    private String additionalInfo;

    @Column(nullable = false)
    private int minWeight;

    @Column(nullable = false)
    private int maxWeight;

    @Column(nullable = false)
    private Timestamp startDate;

    @Column(nullable = false)
    private Timestamp endDate;

    @ManyToOne
    private User referee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Attendance refereeStatus;

    @OneToMany(mappedBy = "idFight", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Fight> fights;


    public Competition(String shortName, String additionalInfo, int minWeight, int maxWeight, Timestamp startDate, Timestamp endDate, User referee, Attendance refereeStatus) {
        this.shortName = shortName;
        this.additionalInfo = additionalInfo;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        this.startDate = startDate;
        this.endDate = endDate;
        this.referee = referee;
        this.refereeStatus = refereeStatus;
    }

    protected Competition() {

    }

    public int getIdCompetition() {
        return idCompetition;
    }

    public Competition setIdCompetition(int idCompetition) {
        this.idCompetition = idCompetition;
        return this;
    }

    public String getShortName() {
        return shortName;
    }

    public Competition setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public Competition setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    public int getMinWeight() {
        return minWeight;
    }

    public Competition setMinWeight(int minWeight) {
        this.minWeight = minWeight;
        return this;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public Competition setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
        return this;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Competition setStartDate(Timestamp startDate) {
        this.startDate = startDate;
        return this;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public Competition setEndDate(Timestamp endDate) {
        this.endDate = endDate;
        return this;
    }

    public User getReferee() {
        return referee;
    }

    public Competition setReferee(User referee) {
        this.referee = referee;
        return this;
    }

    public Attendance getRefereeStatus() {
        return refereeStatus;
    }

    public Competition setRefereeStatus(Attendance refereeStatus) {
        this.refereeStatus = refereeStatus;
        return this;
    }

    public List<Fight> getFights() {
        return fights;
    }

    public Competition setFights(List<Fight> fights) {
        this.fights = fights;
        return this;
    }

    public String translatingDates(Timestamp startDate, Timestamp endDate) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp actualDate = Timestamp.valueOf(localDateTime);
        int state = actualDate.compareTo(startDate);
        int state2 = actualDate.compareTo(endDate);
        if (state >= 0) {
            if (state2 < 0) {
                return "Comenzada";
            } else return "Finalizada";
        } else return "Por comenzar";
    }


    /**
     * Gets the start date of the competition and returns it in a user-friendly format.
     *
     * @return Start date in user-friendly format.
     */
    public String getFormattedStartDate() {
        SimpleDateFormat simpDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return simpDate.format(startDate);
    }

    /**
     * Gets the end date of the competition and returns it in a user-friendly format.
     *
     * @return End date in user-friendly format.
     */
    public String getFormattedEndDate() {
        SimpleDateFormat simpDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return simpDate.format(endDate);
    }

}
