package com.example.demo.controller;

import com.example.demo.controller.request.CreatePlayerRequest;
import com.example.demo.controller.request.PlayerRequest;
import com.example.demo.controller.request.UpdatePlayerRequest;
import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import com.example.demo.filter.Filter;
import com.example.demo.filter.PlayerOrder;
import com.example.demo.service.PlayerError;
import com.example.demo.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/players")
public class PanelController {
    private final PlayerService playerService;

    @GetMapping
    public List<Player> getPlayers(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) Race race,
                                   @RequestParam(required = false) Profession profession,
                                   @RequestParam(required = false) @Min(915148800L) Long after,
                                   @RequestParam(required = false) @Max(32535216000L) Long before,
                                   @RequestParam(required = false) Boolean banned,
                                   @RequestParam(required = false) @Min(-1) Integer minExperience,
                                   @RequestParam(required = false) @Max(10000001) Integer maxExperience,
                                   @RequestParam(required = false) Integer minLevel,
                                   @RequestParam(required = false) Integer maxLevel,
                                   @RequestParam(required = false, defaultValue = PlayerOrder.Names.ID) PlayerOrder order,
                                   @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                   @RequestParam(required = false, defaultValue = "3") Integer pageSize) {
        Filter filter = createFilter(name, title, race, profession, after, before, banned, minExperience, maxExperience,
                minLevel, maxLevel, order, pageNumber, pageSize);
        return playerService.getPlayersByFilter(filter);
    }

    @GetMapping("/count")
    public Integer getPlayersCount(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) Race race,
                                   @RequestParam(required = false) Profession profession,
                                   @RequestParam(required = false) Long after,
                                   @RequestParam(required = false) Long before,
                                   @RequestParam(required = false) Boolean banned,
                                   @RequestParam(required = false) Integer minExperience,
                                   @RequestParam(required = false) Integer maxExperience,
                                   @RequestParam(required = false) Integer minLevel,
                                   @RequestParam(required = false) Integer maxLevel) {
        Filter filter = createFilter(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel,
                maxLevel, null, null, null);
        return playerService.getPlayersByFilter(filter).size();
    }

    private Filter createFilter(String name, String title, Race race, Profession profession, Long after, Long before,
                                Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
                                Integer maxLevel, PlayerOrder playerOrder, Integer pageNumber, Integer pageSize) {
        Filter filter = new Filter();
        filter.setName(name);
        filter.setTitle(title);
        filter.setRace(race);
        filter.setProfession(profession);
        filter.setAfter(after);
        filter.setBefore(before);
        filter.setBanned(banned);
        filter.setMinExperience(minExperience);
        filter.setMaxExperience(maxExperience);
        filter.setMinLevel(minLevel);
        filter.setMaxLevel(maxLevel);
        filter.setOrder(playerOrder);
        filter.setPageNumber(pageNumber);
        filter.setPageSize(pageSize);
        return filter;
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody @Valid CreatePlayerRequest createPlayerRequest) {
        try {
            playerService.createPlayer(convertCreatePlayerRequest(createPlayerRequest));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(Exception e ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable long id) {
//        try {
            Player player = playerService.getPlayerById(id);
            return new ResponseEntity<>(player, HttpStatus.OK);
//        } catch(Exception e) {
//            return new ResponseEntity<>(new PlayerError(HttpStatus.BAD_REQUEST, "Игрок с таким id не найден").getStatusCode());
//        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody @Valid UpdatePlayerRequest updatePlayerRequest) {
        try {
            playerService.editPlayer(id, convertCreatePlayerRequest(updatePlayerRequest));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable long id) {
        try {
            playerService.deletePlayerById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Player convertCreatePlayerRequest(PlayerRequest playerRequest) {
        Player player = new Player();
        player.setName(playerRequest.getName());
        player.setTitle(playerRequest.getTitle());
        player.setRace(playerRequest.getRace());
        player.setProfession(playerRequest.getProfession());
        player.setExperience(playerRequest.getExperience());
        player.setBirthday(playerRequest.getBirthday().getTime());
        player.setBanned(playerRequest.getBanned());
        return player;
    }
}
