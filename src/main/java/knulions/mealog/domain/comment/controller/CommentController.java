package knulions.mealog.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import knulions.mealog.domain.comment.dto.request.CommentSaveRequest;
import knulions.mealog.domain.comment.dto.response.CommentAllReadResponse;
import knulions.mealog.domain.comment.service.CommentService;
import knulions.mealog.global.principal.PrincipalDetails;
import knulions.mealog.global.util.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/board/{boardId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;



    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "댓글 등록", description = "댓글를 등록하는 로직")
    public ResponseEntity<ApiUtil.ApiSuccessResult<Long>> creatReview(
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody() CommentSaveRequest commentSaveRequest
    ) throws IOException {
        Long saveId = commentService.saveComment(commentSaveRequest, boardId, principalDetails.getEmail());

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.CREATED,saveId));
    }

    @GetMapping("/view")
    @Operation(summary = "한 게시글 관련 댓글 전체 조회", description = "한 게시글에 등록된 모든 댓글 조회하는 로직")
    public ResponseEntity<ApiUtil.ApiSuccessResult<List<CommentAllReadResponse>>> getAllReview(
            @PathVariable("boardId") Long boardId
    ){
        List<CommentAllReadResponse> allComment= commentService.findAllComment(boardId);
        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK, allComment));
    }


    /**
     * 댓글 삭제
     *
     * @param commentId 댓글 아이디
     * @return ok
     */
    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제 하는 로직")
    public ResponseEntity<ApiUtil.ApiSuccessResult<?>> deleteReview(
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        commentService.deleteOneComment( commentId, principalDetails.getEmail());

        return ResponseEntity.ok().body(ApiUtil.success(HttpStatus.OK));
    }


}
