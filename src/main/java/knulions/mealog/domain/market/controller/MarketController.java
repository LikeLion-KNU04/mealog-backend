package knulions.mealog.domain.market.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import knulions.mealog.domain.Image.service.ImageService;
import knulions.mealog.domain.board.dto.request.BoardSaveRequest;
import knulions.mealog.domain.board.dto.request.BoardUpdateRequest;
import knulions.mealog.domain.board.dto.response.BoardAllReadResponse;
import knulions.mealog.domain.board.dto.response.BoardIdReadResponse;
import knulions.mealog.domain.board.service.BoardService;
import knulions.mealog.domain.market.dto.request.MarketSaveRequest;
import knulions.mealog.domain.market.dto.request.MarketUpdateRequest;
import knulions.mealog.domain.market.dto.response.MarketAllReadResponse;
import knulions.mealog.domain.market.dto.response.MarketIdReadResponse;
import knulions.mealog.domain.market.service.MarketService;
import knulions.mealog.global.principal.PrincipalDetails;
import knulions.mealog.global.util.ApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/market")
@Tag(name = "마켓", description = "마켓 관련 Api")
@RequiredArgsConstructor
@Slf4j
public class MarketController {

    private final MarketService marketService;
    private final ImageService imageService;

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "마켓 추가", description = "마켓을 추가하는 로직")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> postMarket(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestPart(value="MarketSaveRequest") MarketSaveRequest marketSaveRequest,
            @RequestPart(value = "images", required = false) MultipartFile[] images) throws IOException {

        Long saveId = marketService.saveMarket(marketSaveRequest,images,principalDetails.getEmail());
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED,saveId));

    }

    @GetMapping("/view")
    @Operation(summary = "모든 마켓 조회", description = "모든 마켓을 조회하는 로직")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<MarketAllReadResponse>>> getAllProgram(){
        List<MarketAllReadResponse> allMarket = marketService.findAllMarket();
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, allMarket));
    }

    @GetMapping("/view/{marketId}")
    @Operation(summary = "마켓 하나 조회", description = "하나의 마켓을 조회하는 로직")
    public ResponseEntity<ApiUtil.ApiSuccessResult<MarketIdReadResponse>> getMarket(
            @PathVariable("marketId") Long marketId
    )throws IOException{
        MarketIdReadResponse marketIdReadResponse = marketService.findOneMarket(marketId);
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, marketIdReadResponse));
    }

    @GetMapping("/mine")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "내가 쓴 모든 마켓 조회", description = "내가 쓴 모든 마켓 조회")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<MarketAllReadResponse>>> getMyMarkets(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    )throws IOException{
        List<MarketAllReadResponse> marketAllReadResponse = marketService.findAllMyMarkets(principalDetails.getEmail());
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, marketAllReadResponse));
    }


    @GetMapping("/view/markets")
    @Operation(summary = "한 사람이 쓴 모든 마켓 조회", description = "내가 쓴 모든 마켓 조회")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<MarketAllReadResponse>>> getHeldPrograms(
            @RequestParam(name = "email") String email) throws IOException {
        log.info("email {} 들어옴", email);
        List<MarketAllReadResponse> marketAllReadResponse = marketService.findAllMyMarkets(email);
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, marketAllReadResponse));
    }

//    @PutMapping("/{marketId}")
//    @PreAuthorize("isAuthenticated()")
//    @Operation(summary = "마켓 수정", description = "특정 마켓을 수정하는 로직")
//    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> updateMarket(
//            @RequestPart(value="MarketUpdateRequest") MarketUpdateRequest marketUpdateRequest,
//            @RequestPart(value = "images", required = false) MultipartFile[] images,
//            @PathVariable("marketId") Long marketId,
//            @AuthenticationPrincipal PrincipalDetails principalDetails
//    )throws IOException{
//        Long updateId = marketService.updateMarket(marketUpdateRequest, images, boardId, principalDetails.getEmail());
//        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, updateId));
//    }

//    @DeleteMapping("/{marketId}")
//    @PreAuthorize("isAuthenticated()")
//    @Operation(summary = "마켓 삭제", description = "특정 마켓을 삭제하는 로직")
//    public ResponseEntity<ApiUtil.ApiSuccessResult<?>> deleteMarket(
//            @PathVariable("marketId") Long marketId,
//            @AuthenticationPrincipal PrincipalDetails principalDetails)throws IOException{
//        marketService.deleteMarket(marketId, principalDetails.getEmail());
//        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK));
//    }

}
