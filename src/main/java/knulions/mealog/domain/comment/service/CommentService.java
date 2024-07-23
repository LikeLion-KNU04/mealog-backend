package knulions.mealog.domain.comment.service;


import knulions.mealog.domain.board.dto.request.BoardSaveRequest;
import knulions.mealog.domain.board.entity.Board;
import knulions.mealog.domain.board.repository.BoardRepository;
import knulions.mealog.domain.comment.dto.request.CommentSaveRequest;
import knulions.mealog.domain.comment.dto.response.CommentAllReadResponse;
import knulions.mealog.domain.comment.entity.Comment;
import knulions.mealog.domain.comment.repository.CommentRepository;
import knulions.mealog.domain.member.entity.Member;
import knulions.mealog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;


    public Long saveComment(CommentSaveRequest commentSaveRequest, Long boardId, String email) throws AccessDeniedException {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new IllegalArgumentException("게시물을 찾을 수 없습니다."));


        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("ReviewService/saveReview/member을 찾을 수 없습니다."));


        Comment comment = Comment.builder()
                .content(commentSaveRequest.content())
                .member(member)
                .board(board)
                .build();

        commentRepository.save(comment);

        return comment.getId();

    }

    //시간 포함
    private LocalDateTime convertStringToLocalDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            return LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("옳지 않은 데이터 형식: " + dateStr);
            return null;
        }
    }


    public List<CommentAllReadResponse> findAllComment(Long boardId){
        try{

            Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
            List<Comment> commentList = commentRepository.findByBoard(board);

            List<CommentAllReadResponse> responseList = new ArrayList<>();

            for(Comment comment : commentList){
                responseList.add(
                        new CommentAllReadResponse(comment.getId(), comment.getContent(), writingTimeToString(comment.getCreateDate()))
                );
            }

            return responseList;
        }catch(Exception e){
        }
        return null;
    }


    @Transactional
    public void deleteOneComment(Long commentId, String email)throws IOException{
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow();
        if(!comment.getMember().getEmail().equals(email)){
            throw new AccessDeniedException("본인만 리뷰를 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }
    public static String writingTimeToString(LocalDateTime writingTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return writingTime.format(formatter);
    }
}
