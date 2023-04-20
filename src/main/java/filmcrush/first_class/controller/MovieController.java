package filmcrush.first_class.controller;

import filmcrush.first_class.dto.MovieDto;
import filmcrush.first_class.dto.MovieFormDto;
import filmcrush.first_class.dto.MovieImgDto;
import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.entity.MovieImg;
import filmcrush.first_class.repository.MovieImgRepository;
import filmcrush.first_class.repository.MovieRepository;
import filmcrush.first_class.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private final MovieImgRepository movieImgRepository;



    @GetMapping(value = "/movieMng/add/movie")
    public String movieForm(Model model) {
        model.addAttribute("movieFormDto", new MovieFormDto());
        return "movie/movieForm";
    }

    @PostMapping(value = "/movieMng/add/movie")
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


    @GetMapping(value = "/movieMng/add/movie/{movieIndex}")
    public String movieDtl(@PathVariable("movieIndex") Long movieIndex, Model model) {

            MovieFormDto movieFormDto = movieService.getMovieDtl(movieIndex);

            model.addAttribute("movieFormDto", movieFormDto);

        return "movie/movieForm";
    }


    @PostMapping(value = "/movieMng/add/movie/{movieIndex}")
    public String movieUpdate(@Valid MovieFormDto movieFormDto, BindingResult bindingResult,
                             @RequestParam("movieImgFile") MultipartFile movieImgFileList, Model model) throws Exception {
            movieService.updateMovie(movieFormDto, movieImgFileList);
        return "redirect:/";
    }


    @GetMapping(value = "/movieMng")
    public String movieBoard(Model model, @PageableDefault(page = 0, size = 10, sort = "movieIndex", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Movie> list = movieService.movieList(pageable);


        //페이지 블럭 처리
        //1을 더해주는 이유 : pageable은 0부터 처리됨
        // getPageable() : Page 객체를 생성할 때 사용된 Pageable 객체(페이지 번호, 사이즈, 정렬 방법등의 정보를 담고 있음) 반환
        // getPageNumber() : Pageable 객체에서 현재 페이지의 번호를 반환함.
        int nowPage = list.getPageable().getPageNumber() + 1;

        // 현재 페이지에서 가장 앞 페이지 번호를 보여줄 변수.
        // max 함수 : 현재 페이지에서 -4를 해줬을 때 1보다 작은 수가 나오면 안됨
        // 보통 UI에서 페이징 좌우로 4페이지 정도씩(총 9~10개정도) 보여주는게 일반적이므로 4로 지정.
        int startPage = Math.max(nowPage - 2, 1);

        int endPage = Math.min(nowPage + 2, list.getTotalPages());

        // 현재 페이지 1번일 때 1 뒤에 2, 3, 4, 5p까지 출력되도록함.
        if (nowPage == 1) {
            endPage = Math.min(nowPage + 4, list.getTotalPages());
        } else if (nowPage == 2) {
            // 현재 페이지 2번일 때 뒤에 3, 4, 5p까지 출력되도록 함.
            endPage = Math.min(nowPage + 3, list.getTotalPages());
        }

        // 현재 페이지가 마지막 페이지일 때
        // 현재 페이지 앞에 페이지를 뜨게 만듦
        if (nowPage == (list.getTotalPages() - 1)) {
            startPage = Math.min(nowPage - 3, list.getTotalPages());
        } else if (nowPage == (list.getTotalPages())) {
            startPage = Math.min(nowPage - 4, list.getTotalPages());
        }

        if (startPage < 1) {
            startPage = 1;
        }

        int prevPage = nowPage - 1;
        int nextPage = nowPage + 1;

        model.addAttribute("movieList", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("totalPages", list.getTotalPages());
        return "board/MovieMng";
    }

    @GetMapping(value = "/movieMng/delete/movie/{movieIndex}")
    public String deleteMovie(@PathVariable("movieIndex") Long movieIndex) {
        movieService.deleteMovie(movieIndex);
        return "redirect:/movieMng";
    }
}
