package pl.inzynier.bukmacher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.inzynier.bukmacher.domain.Game;
import pl.inzynier.bukmacher.repositories.GameRepository;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    GameRepository gameRepository;

    @GetMapping
    List<Game> getStudents() {
        return gameRepository.findAll();
    }

    @PostMapping
    Game createGame(@RequestBody Game game) {
        return gameRepository.save(game);
    }
}
