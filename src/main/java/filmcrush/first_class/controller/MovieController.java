package filmcrush.first_class.controller;

import filmcrush.first_class.dto.MovieFormDto;
import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.repository.MovieRepository;
import filmcrush.first_class.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    private final MovieRepository movieRepository;

    @GetMapping(value = "/add/movie")
    public String movieForm(Model model) {
        model.addAttribute("movieFormDto", new MovieFormDto());
        return "movie/movieForm";
    }

    @PostMapping(value = "/add/movie")
    public String movieNew(@Valid MovieFormDto movieFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("movieImgFile") List<MultipartFile> movieImgFileList) {
        if(bindingResult.hasErrors()) {
            return "movie/movieForm";
        }

        if(movieImgFileList.get(0).isEmpty() && movieFormDto.getMovieIndex() == null) {
            model.addAttribute("errorMessage", "첫번째 포스터는 필수 입력 값입니다.");
            return "movie/movieForm";
        }

        try {
            movieService.saveMovie(movieFormDto, movieImgFileList);
        } catch(Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "movie/movieForm";
        }
        return "redirect:/";
    }


    @GetMapping(value = "/add/movie/{movieIndex}")
    public String movieDtl(@PathVariable("movieIndex") Long movieIndex, Model model) {
        try {
            Movie movie = movieRepository.findById(movieIndex)
                    .orElseThrow(EntityNotFoundException::new);
            MovieFormDto movieFormDto = movieService.getMovieDtl(movie);
            model.addAttribute("movieFormDto", movieFormDto);
        } catch(EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 영화입니다.");
            model.addAttribute("movieFormDto", new MovieFormDto());
            return "movie/movieForm";
        }
        return "movie/movieForm";
    }


}
