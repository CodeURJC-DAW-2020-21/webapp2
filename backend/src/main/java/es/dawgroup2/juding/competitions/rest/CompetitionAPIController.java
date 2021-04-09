package es.dawgroup2.juding.competitions.rest;

import es.dawgroup2.juding.competitions.Competition;
import es.dawgroup2.juding.competitions.CompetitionService;
import es.dawgroup2.juding.fight.FightService;
import es.dawgroup2.juding.main.DateService;
import es.dawgroup2.juding.main.HeaderInflater;
import es.dawgroup2.juding.users.User;
import es.dawgroup2.juding.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/competition")
public class CompetitionAPIController {
    @Autowired
    HeaderInflater headerInflater;

    @Autowired
    UserService userService;

    @Autowired
    CompetitionService competitionService;

    @Autowired
    FightService fightService;

    @Autowired
    DateService dateService;

    /**
     * Gets the competition
     * @param idCompetition Id of the competition
     * @return Response Entity with the competition or bad request
     */
    @GetMapping("/{idCompetition}")
    public ResponseEntity<Competition> showCompetition(@PathVariable String idCompetition) {
        Competition competition = competitionService.findById(Integer.parseInt(idCompetition));
        return (competition == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(competition);
    }

    /**
     * Joins a competition
     * @param idCompetition Id of the competition
     * @param request HTTP Servlet Request
     * @return Response Entity with the competition or bad request
     */
    @PutMapping("/{idCompetition}/join")
    public ResponseEntity<Competition> joinCompetition(@PathVariable String idCompetition, HttpServletRequest request) {
        Competition competition = competitionService.findById(Integer.parseInt(idCompetition));
        User user = userService.findByNickname(request.getUserPrincipal().getName());
        if (fightService.checkParticipation(competition, user)) {
            return ResponseEntity.badRequest().build();
        } else {
            competitionService.joinCompetition(competition, user);
            competitionService.save(competition);
            return (competition == null) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(competition);
        }
    }
}

