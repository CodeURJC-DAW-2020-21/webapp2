package es.dawgroup2.juding.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import es.dawgroup2.juding.auxTypes.belts.Belt;
import es.dawgroup2.juding.auxTypes.gender.Gender;
import es.dawgroup2.juding.auxTypes.refereeRange.RefereeRange;
import es.dawgroup2.juding.auxTypes.roles.Role;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(indexes = {@Index(columnList = "dni, nickname", unique = true)})
public class User {
    @Id
    private String licenseId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private Gender gender;

    private Integer phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonIgnore
    private Date birthDate;

    @Column(nullable = false)
    private String dni;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String securityQuestion;

    @Column(nullable = false)
    private String securityAnswer;

    @Lob
    @JsonIgnore
    private Blob imageFile;

    @JsonIgnore
    private String mimeProfileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Belt belt;

    private String gym;

    private Integer weight;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private RefereeRange refereeRange;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(nullable = false, length = 1)
    private Set<Role> roles;

    @ElementCollection(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Integer> competitorMedals;


    public User() {
    }

    public User(String licenseId, String name, String surname, Gender gender, int phone, String email, Date birthDate, String dni, String nickname, String password, String securityQuestion, String securityAnswer, Blob imageFile, String mimeProfileImage, Belt belt, String gym, Integer weight, RefereeRange refereeRange, Set<Role> roles) {
        this.licenseId = licenseId;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.dni = dni;
        this.nickname = nickname;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.imageFile = imageFile;
        this.mimeProfileImage = mimeProfileImage;
        this.belt = belt;
        this.gym = gym;
        this.weight = weight;
        this.refereeRange = refereeRange;
        this.roles = roles;
        if (roles.contains(Role.C)) {
            this.competitorMedals = new ArrayList<>();
        }
    }

    public String getLicenseId() {
        return licenseId;
    }

    public User setLicenseId(String licenseId) {
        this.licenseId = licenseId;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public User setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public User setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public Integer getPhone() {
        return phone;
    }

    public User setPhone(Integer phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public User setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getDni() {
        return dni;
    }

    public User setDni(String dni) {
        this.dni = dni;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public User setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
        return this;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public User setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
        return this;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public User setImageFile(Blob profileImage) {
        this.imageFile = profileImage;
        return this;
    }

    public String getMimeProfileImage() {
        return mimeProfileImage;
    }

    public User setMimeProfileImage(String mimeProfileImage) {
        this.mimeProfileImage = mimeProfileImage;
        return this;
    }

    public Belt getBelt() {
        return belt;
    }

    public User setBelt(Belt belt) {
        this.belt = belt;
        return this;
    }

    public String getGym() {
        return gym;
    }

    public User setGym(String gym) {
        this.gym = gym;
        return this;
    }

    public Integer getWeight() {
        return weight;
    }

    public User setWeight(Integer weight) {
        this.weight = weight;
        return this;
    }

    public RefereeRange getRefereeRange() {
        return refereeRange;
    }

    public User setRefereeRange(RefereeRange refereeRange) {
        this.refereeRange = refereeRange;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public User addRole(Role role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
        return this;
    }

    public List<Integer> getCompetitorMedals() {
        return this.competitorMedals;
    }

    /**
     * Adds a new medal for this user
     *
     * @param points Medal (gold = 3, silver = 2, bronze = 1)
     */
    public void addPoints(int points) {
        this.competitorMedals.add(points);
    }

    /**
     * Gets the birth date of the user and returns it in a user-friendly format.
     *
     * @return Birth date in user-friendly format.
     */
    @JsonProperty("birthDate")
    public String getFormattedBirthDate() {
        SimpleDateFormat simpDate = new SimpleDateFormat("dd/MM/yyyy");
        return simpDate.format(birthDate);
    }

    /**
     * Returns true if the current user is male or false if it is female.
     *
     * @return Gender boolean.
     */
    @JsonIgnore
    public boolean isMale() {
        return gender == Gender.H;
    }

    /**
     * Returns true if the current user includes the role specified.
     *
     * @param role Role searched.
     * @return True if one of the user's roles match with included one.
     */
    public boolean isRole(Role role) {
        return (this.getRoles() != null) && (roles.contains(role));
    }

    // Warning: also used in RankingDTO
    @JsonProperty("imageFile")
    public String imageFile() {
        return "/api/users/" + getLicenseId() + "/image";
    }
}
