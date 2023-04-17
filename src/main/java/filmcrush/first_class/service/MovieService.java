package filmcrush.first_class.service;

import filmcrush.first_class.dto.MovieFormDto;
import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.entity.MovieImg;
import filmcrush.first_class.repository.MovieImgRepository;
import filmcrush.first_class.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieImgService movieImgService;
  //  private final MovieImgRepository movieImgRepository;

    public Long saveMovie(MovieFormDto movieFormDto, List<MultipartFile> movieImgFileList) throws Exception {
        //상품 등록
        Movie movie = movieFormDto.createMovie();
        movieRepository.save(movie);

        //이미지 등록
        for(int i=0; i<movieImgFileList.size(); i++) {
            MovieImg movieImg = new MovieImg();
            movieImg.setMovie(movie);

            movieImgService.saveMovieImg(movieImg, movieImgFileList.get(i));
        }
        return movie.getMovieIndex();
    }

//    @Transactional(readOnly = true)
//    public ItemFormDto getItemDtl(Long itemId) {
//        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
//        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
//        for (ItemImg itemImg : itemImgList) {
//            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
//            itemImgDtoList.add(itemImgDto);
//        }
//
//        Item item = itemRepository.findById(itemId)
//                .orElseThrow(EntityNotFoundException::new);
//        ItemFormDto itemFormDto = ItemFormDto.of(item);
//        itemFormDto.setItemImgDtoList(itemImgDtoList);
//        return itemFormDto;
//
//    }
//
//    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
//        //상품 수정
//        Item item = itemRepository.findById(itemFormDto.getId())
//                .orElseThrow(EntityNotFoundException::new);
//        item.updateItem(itemFormDto);
//
//        List<Long> itemImgIds = itemFormDto.getItemImgIds();
//
//        //이미지 등록
//        for(int i=0; i<itemImgFileList.size(); i++) {
//            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
//        }
//        return item.getId();
//    }
//
//    @Transactional(readOnly = true)
//    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
//        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
//    }
//
//    @Transactional(readOnly = true)
//    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
//        return itemRepository.getMainItemPage(itemSearchDto, pageable);
//    }
}
