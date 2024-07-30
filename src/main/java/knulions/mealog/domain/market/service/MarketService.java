package knulions.mealog.domain.market.service;

import knulions.mealog.domain.Image.entity.Image;
import knulions.mealog.domain.Image.repository.ImageRepository;
import knulions.mealog.domain.Image.service.ImageService;
import knulions.mealog.domain.board.dto.request.BoardSaveRequest;
import knulions.mealog.domain.board.dto.response.BoardAllReadResponse;
import knulions.mealog.domain.board.dto.response.BoardIdReadResponse;
import knulions.mealog.domain.board.entity.Board;
import knulions.mealog.domain.market.dto.request.MarketSaveRequest;
import knulions.mealog.domain.market.dto.response.MarketAllReadResponse;
import knulions.mealog.domain.market.dto.response.MarketIdReadResponse;
import knulions.mealog.domain.market.entity.Market;
import knulions.mealog.domain.market.repository.MarketRepository;
import knulions.mealog.domain.member.entity.Member;
import knulions.mealog.domain.member.repository.MemberRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.error.Mark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketService{

    private final MarketRepository marketRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @Transactional
    public Long saveMarket(MarketSaveRequest marketSaveRequest, MultipartFile[] imageList, String email) throws IOException {

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("member 이 없습니다"));

        Market market = Market.builder()
                .title(marketSaveRequest.title())
                .price(marketSaveRequest.price())
                .member(member)
                .content(marketSaveRequest.content())
                .build();

        imageService.uploadMarketImages(market, imageList);

        marketRepository.save(market);

        return market.getId();

    }

    public List<MarketAllReadResponse> findAllMarket() {

        try{
            List<Market> marketList = marketRepository.findAll();

            List<MarketAllReadResponse> responseList = new ArrayList<>();

            for(Market market : marketList){
                Image image = imageRepository.findAllByMarketId(market.getId()).get(0);
                String imageUrl = image.getUrl();

                responseList.add(
                        new MarketAllReadResponse(market.getId(),market.getTitle(), market.getContent(), market.getPrice(), imageUrl)
                );
            }
            return responseList;
        }catch(Exception e){
            log.info("불러오기 실패");
        }
        return null;
    }

    public MarketIdReadResponse findOneMarket(Long id)throws IOException{
        Market market = marketRepository.findById(id).orElseThrow();

        List<Image> imageList =  imageService.getMarketImageList(Optional.ofNullable(market));

        List<String> imageUrls = new ArrayList<>();

        for(Image image: imageList){
            imageUrls.add(image.getUrl());
        }

        Member member = market.getMember();
        return MarketIdReadResponse.builder()
                .marketId(market.getId())
                .title(market.getTitle())
                .content(market.getContent())
                .price(market.getPrice())
                .imgUrls(imageUrls)
                .build();
    }



    public List<MarketAllReadResponse> findAllMyMarkets(String email) throws IOException{

        try{
            Member member = memberRepository.findByEmail(email).orElseThrow();
            List<Market> marketList = marketRepository.findAllByMember(member);

            List<MarketAllReadResponse> responseList = new ArrayList<>();

            for(Market market: marketList){
                Image image = imageRepository.findAllByMarketId(market.getId()).get(0);
                String imageUrl = image.getUrl();

                responseList.add(
                        new MarketAllReadResponse(market.getId(),market.getTitle(), market.getContent(), market.getPrice(), imageUrl)
                );
            }
            return responseList;
        }catch(Exception e){
            log.info("findAllMyPrograms 오류,{}",e);
        }
        return null;
    }

}
