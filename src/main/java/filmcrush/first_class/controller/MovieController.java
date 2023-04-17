package filmcrush.first_class.controller;

import filmcrush.first_class.dto.MovieFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MovieController {
    @GetMapping(value = "/add/movie")
    public String movieForm(Model model) {
        model.addAttribute("movieFormDto", new MovieFormDto());
        return "movie/movieForm";
    }
}
