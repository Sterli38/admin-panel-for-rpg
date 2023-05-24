package com.example.demo.controller;

import com.example.demo.controller.request.CreatePlayerRequest;
import com.example.demo.controller.request.PlayerRequest;
import com.example.demo.controller.request.UpdatePlayerRequest;
import com.example.demo.controller.response.PlayerResponse;
import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import com.example.demo.filter.Filter;
import com.example.demo.filter.PlayerOrder;
import com.example.demo.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/players")
public class PanelController {
    private final PlayerService playerService;

    @GetMapping
    public List<PlayerResponse> getPlayers(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) Race race,
                                   @RequestParam(required = false) Profession profession,
                                   @RequestParam(required = false) @Min(946684800000L) Long after,
                                   @RequestParam(required = false) @Max(32535216000000L) Long before,
                                   @RequestParam(required = false) Boolean banned,
                                   @RequestParam(required = false) @Min(-1) Integer minExperience,
                                   @RequestParam(required = false) @Max(10000001) Integer maxExperience,
                                   @RequestParam(required = false) Integer minLevel,
                                   @RequestParam(required = false) Integer maxLevel,
                                   @RequestParam(required = false, defaultValue = "ID") PlayerOrder order,
                                   @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                   @RequestParam(required = false, defaultValue = "3") Integer pageSize) {
        Filter filter = createFilter(name, title, race, profession, after, before, banned, minExperience, maxExperience,
                minLevel, maxLevel, order, pageNumber, pageSize);
        return playerService.getPlayersByFilter(filter).stream()
                .map(this::convertPlayer)
                .collect(Collectors.toList());
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
        filter.setAfter(after == null ? null : new Date(after));
        filter.setBefore(before == null ? null : new Date(before));
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
    public ResponseEntity<PlayerResponse> createPlayer(@RequestBody CreatePlayerRequest createPlayerRequest) {
        Player player = playerService.createPlayer(convertPlayerRequest(createPlayerRequest));
        PlayerResponse playerResponse = convertPlayer(player);
        return new ResponseEntity<>(playerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable long id) {
//        try {
            Player player = playerService.getPlayerById(id);
            PlayerResponse playerResponse = convertPlayer(player);
            if(playerResponse != null) {
                return new ResponseEntity<>(playerResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
//        } catch(Exception e) {
//            return new ResponseEntity<>(new PlayerError(HttpStatus.BAD_REQUEST, "Игрок с таким id не найден").getStatusCode());
//        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<PlayerResponse> updatePlayer(@PathVariable Long id, @RequestBody  UpdatePlayerRequest updatePlayerRequest) {
        try {
            playerService.editPlayer(id, convertPlayerRequest(updatePlayerRequest));
            PlayerResponse playerResponse = convertPlayer(playerService.getPlayerById(id));
            return new ResponseEntity<>(playerResponse, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//    playerService.editPlayer(id, convertCreatePlayerRequest(updatePlayerRequest));
//    return getPlayer(id).getBody();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable long id) {
            Player player = playerService.deletePlayerById(id);
            if(player != null) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    }

    private Player convertPlayerRequest(PlayerRequest playerRequest) {
        Player player = new Player();
        player.setName(playerRequest.getName());
        player.setTitle(playerRequest.getTitle());
        player.setRace(playerRequest.getRace());
        player.setProfession(playerRequest.getProfession());
        player.setExperience(playerRequest.getExperience());
        player.setBirthday(playerRequest.getBirthday());
        player.setBanned(playerRequest.getBanned());
        return player;
    }

    private PlayerResponse convertPlayer(Player player) {
        PlayerResponse playerResponse = new PlayerResponse();
        playerResponse.setId(player.getId());
        playerResponse.setName(player.getName());
        playerResponse.setTitle(player.getTitle());
        playerResponse.setRace(player.getRace());
        playerResponse.setProfession(player.getProfession());
        playerResponse.setExperience(player.getExperience());
        playerResponse.setLevel(player.getLevel());
        playerResponse.setUntilNextLevel(player.getUntilNextLevel());
        playerResponse.setBirthday(player.getBirthday().getTime());
        playerResponse.setBanned(player.getBanned());
        return playerResponse;
    }
}
