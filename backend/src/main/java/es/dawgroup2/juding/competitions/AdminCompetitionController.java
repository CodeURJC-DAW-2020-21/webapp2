package es.dawgroup2.juding.competitions;

import es.dawgroup2.juding.auxTypes.attendances.AttendanceService;
import es.dawgroup2.juding.fight.Fight;
import es.dawgroup2.juding.fight.FightService;
import es.dawgroup2.juding.main.DateService;
import es.dawgroup2.juding.users.User;
import es.dawgroup2.juding.users.UserService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminCompetitionController {

    @Autowired
    CompetitionService competitionService;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    DateService dateService;

    @Autowired
    UserService userService;

    @Autowired
    FightService fightService;

    /**
     *
     * @param model model of the view
     * @return the view of the competition list
     */
    @GetMapping("/admin/competition/list")
    public String competitionList(Model model) {
        List<Competition> competitionList = competitionService.findAll();
        model.addAttribute("competitionList", competitionList);
        return "/admin/competition/list";
    }

    /**
     *
     * @param idCompetition id of the competition
     * @param model model of the view
     * @return view of the competition to edit
     */
    @GetMapping("/admin/competition/edit/{idCompetition}")
    public String editCompetition(@PathVariable String idCompetition, Model model) {
        Competition competition = competitionService.findById(idCompetition);
        model.addAttribute("competition", competition)
                .addAttribute("attendance", attendanceService.getAttendanceToString(competition.getRefereeStatus()));
        return "/admin/competition/edit";
    }

    /**
     *
     * @param model model of the view
     * @return view of the competition to add
     */
    @GetMapping("/admin/competition/newCompetition")
    public String newCompetition(Model model) {
        return "/admin/competition/newCompetition";
    }

    /**
     * Generates a new competition
     * @param shortName The short name of the competition
     * @param additionalInfo Info about the competition
     * @param minWeight The minimum weight allowed in a competition
     * @param maxWeight The maximum weight allowed in a competition
     * @param startDate The start date of a competition
     * @param endDate The end date of a competition
     * @param referee referee The license of the referee in charge of the competition
     * @param status A String of that show the status of the referee attendance
     * @param imageFile Image that represent the competition
     * @return The new competition
     * @throws IOException
     */
    @PostMapping("/admin/competition/newCompetition")
    public String addACompetition(@RequestParam String shortName,
                                  @RequestParam String additionalInfo,
                                  @RequestParam int minWeight,
                                  @RequestParam int maxWeight,
                                  @RequestParam String startDate,
                                  @RequestParam String endDate,
                                  @RequestParam String referee,
                                  @RequestParam String status,
                                  MultipartFile imageFile) throws IOException, ParseException {
        Competition competition=new Competition();
        competition.setShortName(shortName)
                .setAdditionalInfo(additionalInfo)
                .setMinWeight(minWeight)
                .setMaxWeight(maxWeight)
                .setStartDate(dateService.stringToTimestamp(startDate))
                .setEndDate(dateService.stringToTimestamp(endDate))
                .setReferee(userService.getUserOrNull(referee))
                .setRefereeStatus(attendanceService.findAttendanceById(status));
        if (imageFile != null) {
            if (!imageFile.isEmpty()) {
                competition.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            }
        }
        competitionService.add(competition);
        List<Fight> fights= new ArrayList<>();
        // Generar combate final
        Fight finalFight = new Fight(competition, 0, null, null, null, false, null, null);
        fightService.save(finalFight);
        List<Fight> semifinal = new ArrayList<>();
        for (int i = 0; i <= 1; i++)
            semifinal.add(new Fight(competition, 1, null, null, finalFight, false, null, null));
        fightService.saveAll(semifinal);
        List<Fight> cuartos = new ArrayList<>();
        for (int i = 0; i <= 3; i++)
            cuartos.add(new Fight(competition, 2, null, null, semifinal.get(i / 2), false, null, null));
        fightService.saveAll(cuartos);
        List<Fight> octavos = new ArrayList<>();
        for (int i = 0; i <= 7; i++)
            octavos.add(new Fight(competition, 3, null, null, cuartos.get(i / 2), false, null, null));
        fightService.saveAll(octavos);

        int cont=0;
        finalFight.setUpFight(semifinal.get(0)).setDownFight(semifinal.get(1));
        for (int i = 0; i <= 1; i++){
                semifinal.get(i).setUpFight(cuartos.get(cont)).setDownFight(cuartos.get(cont + 1));
                cont=cont+2;
        }
        cont=0;
        for (int i = 0; i <= 3; i++){
            cuartos.get(i).setUpFight(octavos.get(cont)).setDownFight(octavos.get(cont + 1));
            cont=cont+2;
        }
        fightService.save(finalFight);
        fightService.saveAll(semifinal);
        fightService.saveAll(cuartos);
        fightService.saveAll(octavos);
        return "redirect:/admin/competition/list";
    }

    /**
     * Form to edit a competition
     * @param idCompetition Id of a competition
     * @param shortName The name of a competitiom
     * @param additionalInfo Information of a competition
     * @param minWeight The minimum weight allowed in a competition
     * @param maxWeight The maximum weight allowed in a competition
     * @param startDate The start date of a competition
     * @param endDate The end date of a competition
     * @param referee The license of the referee in charge of the competition
     * @param refereeStatus A String of that show the status of the referee attendance
     * @param imageFile Image that represent the competition
     * @return The competition edited
     * @throws IOException
     * @throws SQLException
     */
    @PostMapping("/admin/competition/edit")
    public String updatingCompetitionInfo(@RequestParam String idCompetition,
                                          @RequestParam String shortName,
                                          @RequestParam String additionalInfo,
                                          @RequestParam int minWeight,
                                          @RequestParam int maxWeight,
                                          @RequestParam String startDate,
                                          @RequestParam String endDate,
                                          @RequestParam String referee,
                                          @RequestParam String refereeStatus,
                                          MultipartFile imageFile) throws IOException, ParseException {
        Competition competition = competitionService.findById(idCompetition);
        if (imageFile != null) {
            if (!imageFile.isEmpty()) {
                competition.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            }
        }
        competition.setShortName(shortName)
                .setAdditionalInfo(additionalInfo)
                .setMinWeight(minWeight)
                .setMaxWeight(maxWeight)
                .setReferee(userService.getUserOrNull(referee))
                .setRefereeStatus(attendanceService.findAttendanceById(refereeStatus))
                .setStartDate(dateService.stringToTimestamp(startDate))
                .setEndDate(dateService.stringToTimestamp(endDate));
        competitionService.updatingInfoCompetition(competition);
        return "redirect:/admin/competition/list";

    }

    /**
     * This method deletes the competition selected
     * @param model model of the view
     * @param idCompetition id of the competition
     * @return view of the competition list
     */
    @GetMapping("/admin/competition/delete/{idCompetition}")
    public String showCompetitionToDelete(Model model, @PathVariable String idCompetition) {
        Competition competition = competitionService.findById(idCompetition);
        //List<Fight> fights= fightService.findByIdCompetition(idCompetition);
        //competition.setFights(null);
        //fightService.deleteAll(fights);
        model.addAttribute("competition", competition);
        competitionService.deleteById(idCompetition);
        return "redirect:/admin/competition/list";
    }

}
