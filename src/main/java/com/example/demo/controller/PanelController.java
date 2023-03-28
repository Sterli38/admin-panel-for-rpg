package com.example.demo.controller;

import com.example.demo.controller.request.CreatePlayerRequest;
import com.example.demo.controller.request.PlayerRequest;
import com.example.demo.controller.request.UpdatePlayerRequest;
import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import com.example.demo.filter.Filter;
import com.example.demo.filter.PlayerOrder;
import com.example.demo.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
                                   @RequestParam(required = false) Long after,
                                   @RequestParam(required = false) Long before,
                                   @RequestParam(required = false) Boolean banned,
                                   @RequestParam(required = false) Integer minExperience,
                                   @RequestParam(required = false) Integer maxExperience,
                                   @RequestParam(required = false) Integer minLevel,
                                   @RequestParam(required = false) Integer maxLevel,
                                   @RequestParam(required = false) PlayerOrder playerOrder,
                                   @RequestParam(required = false) Integer pageNumber,
                                   @RequestParam(required = false) Integer pageSize) {
        Filter filter = createFilter(name, title, race, profession, after, before, banned, minExperience, maxExperience,
                minLevel, maxLevel, playerOrder, pageNumber, pageSize);
        return playerService.getPlayersByFilter(filter);
    }

    @GetMapping("/count")
    public Integer getPlayersCount(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) Race race,
                                   @RequestParam(required = false) Profession profession,
                                   @RequestParam(required = false) Long after,
                                   @RequestParam(required = false) Long before,
                                   @RequestParam(required = false) Boolean baned,
                                   @RequestParam(required = false) Integer minExperience,
                                   @RequestParam(required = false) Integer maxExperience,
                                   @RequestParam(required = false) Integer minLevel,
                                   @RequestParam(required = false) Integer maxLevel) {
        Filter filter = createFilter(name, title, race, profession, after, before, baned, minExperience, maxExperience, minLevel,
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
    public void createPlayer(@RequestBody @Valid CreatePlayerRequest createPlayerRequest) {
        playerService.createPlayer(convertCreatePlayerRequest(createPlayerRequest));
    }

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable long id) {
    return playerService.getPlayerById(id);
    }

    @PostMapping("/{id}")
    public void updatePlayer(@PathVariable Long id, @RequestBody @Valid UpdatePlayerRequest updatePlayerRequest) {
        playerService.editPlayer(id, convertCreatePlayerRequest(updatePlayerRequest));
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable long id) {
    playerService.deletePlayerById(id);
    }

    private Player convertCreatePlayerRequest(PlayerRequest playerRequest) {
        Player player = new Player();
        player.setName(playerRequest.getName());
        player.setTitle(playerRequest.getTitle());
        player.setRace(playerRequest.getRace());
        player.setProfession(playerRequest.getProfession());
        player.setExperience(playerRequest.getExperience());
        player.setLevel(playerRequest.getLevel());
        player.setUntilNextLevel(playerRequest.getUntilNextLevel());
        player.setBirthday(playerRequest.getBirthday().getTime());
        player.setBanned(playerRequest.getBanned());
        return player;
    }

}
