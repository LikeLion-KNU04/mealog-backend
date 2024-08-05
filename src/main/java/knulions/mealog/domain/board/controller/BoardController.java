package knulions.mealog.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import knulions.mealog.domain.Image.service.ImageService;
import knulions.mealog.domain.board.dto.request.BoardSaveRequest;
import knulions.mealog.domain.board.dto.request.BoardUpdateRequest;
import knulions.mealog.domain.board.dto.response.BoardAllReadResponse;
import knulions.mealog.domain.board.dto.response.BoardIdReadResponse;
import knulions.mealog.domain.board.repository.BoardRepository;
import knulions.mealog.domain.board.service.BoardService;
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
@RequestMapping("/board")
@Tag(name = "게시글", description = "게시글 관련 Api")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final ImageService imageService;
    private final BoardRepository boardRepository;

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "게시글 추가", description = "게시글을 추가하는 로직")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> createProgram(
            HttpServletRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestPart(value="BoardSaveRequest") BoardSaveRequest boardSaveRequest,
            @RequestPart(value = "images", required = false) MultipartFile[] images) throws IOException {

            Long saveId = boardService.saveBoard(boardSaveRequest,images,principalDetails.getEmail());
            return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED,saveId));

    }

    @GetMapping("/view")
    @Operation(summary = "모든 게시글 조회", description = "모든 게시글을 조회하는 로직, 메인 게시판 용도")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<BoardAllReadResponse>>> getAllProgram(){
        List<BoardAllReadResponse> allBoards = boardService.findAllBoard();
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, allBoards));
    }

    @GetMapping("/view/{boardId}")
    @Operation(summary = "게시글 하나 조회", description = "하나의 게시글을 조회하는 로직")
    public ResponseEntity<ApiUtil.ApiSuccessResult<BoardIdReadResponse>> getProgram(
            @PathVariable("boardId") Long boardId
    )throws IOException{
        BoardIdReadResponse boardIdReadResponse = boardService.findOneBoard(boardId);
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, boardIdReadResponse));
    }

    @GetMapping("/mine")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "내가 쓴 모든 게시글 조회", description = "내가 쓴 모든 게시글 조회")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<BoardAllReadResponse>>> getMyPrograms(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    )throws IOException{
        List<BoardAllReadResponse> boardAllReadResponse = boardService.findAllMyBoards(principalDetails.getEmail());
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, boardAllReadResponse));
    }


    @GetMapping("/view/boards")
    @Operation(summary = "글쓴이가 쓴 게시글 조회", description = "글쓴이가 쓴 게시글 조회")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<BoardAllReadResponse>>> getHeldPrograms(
            @RequestParam(name = "email") String email) throws IOException {
        log.info("email {} 들어옴", email);
        List<BoardAllReadResponse> boardAllReadResponse = boardService.findAllMyBoards(email);
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, boardAllReadResponse));
    }

    @PutMapping("/{boardId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "게시글 수정", description = "특정 게시글을 수정하는 로직")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> updateProgram(
            @RequestPart(value="BoardUpdateRequest") BoardUpdateRequest boardUpdateRequest,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    )throws IOException{
        Long updateId = boardService.updateBoard(boardUpdateRequest, images, boardId, principalDetails.getEmail());
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, updateId));
    }

    @DeleteMapping("/{boardId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제하는 로직")
    public ResponseEntity<ApiUtil.ApiSuccessResult<?>> deleteBoard(
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal PrincipalDetails principalDetails)throws IOException{
        boardService.deleteBoard(boardId, principalDetails.getEmail());
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK));
    }

}
