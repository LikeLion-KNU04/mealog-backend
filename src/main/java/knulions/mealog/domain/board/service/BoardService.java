package knulions.mealog.domain.board.service;

import knulions.mealog.domain.Image.entity.Image;
import knulions.mealog.domain.Image.repository.ImageRepository;
import knulions.mealog.domain.Image.service.ImageService;
import knulions.mealog.domain.board.dto.request.BoardSaveRequest;
import knulions.mealog.domain.board.dto.request.BoardUpdateRequest;
import knulions.mealog.domain.board.dto.response.BoardAllReadResponse;
import knulions.mealog.domain.board.dto.response.BoardIdReadResponse;
import knulions.mealog.domain.board.entity.Board;
import knulions.mealog.domain.board.repository.BoardRepository;
import knulions.mealog.domain.member.entity.Member;
import knulions.mealog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveBoard(BoardSaveRequest boardSaveRequest, MultipartFile[] imageList, String email) throws IOException {

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("member 이 없습니다"));


        Board board = Board.builder()
                .member(member)// 글을 쓴 사람이다.
                .content(boardSaveRequest.content())
                .build();

        imageService.uploadBoardImages(board, imageList);

        boardRepository.save(board);

        return board.getId();

    }

    public List<BoardAllReadResponse> findAllBoard() {

         try{
            List<Board> boardList = boardRepository.findAll();

            List<BoardAllReadResponse> responseList = new ArrayList<>();

            for(Board board : boardList){
                Image image = imageRepository.findAllByBoardId(board.getId()).get(0);
                String imageUrl = image.getUrl();

                responseList.add(
                        new BoardAllReadResponse(board.getId(),  writingTimeToString(board.getCreateDate()),board.getContent(),imageUrl)
                );
            }
            return responseList;
        }catch(Exception e){
             log.info("불러오기 실패");
        }
        return null;
    }

    public BoardIdReadResponse findOneBoard(Long id)throws IOException{
        Board board = boardRepository.findById(id)
                .orElseThrow();

        List<Image> imageList =  imageService.getBoardImageList(board);

        List<String> imageUrls = new ArrayList<>();

        for(Image image: imageList){
            imageUrls.add(image.getUrl());
        }

        Member member = board.getMember();
        return BoardIdReadResponse.builder()
                .boardId(board.getId())
                .memberEmail(member.getEmail())
                .memberName(member.getNickName())
                .content(board.getContent())
                .writingTime(writingTimeToString(board.getCreateDate()))
                .images(imageUrls)
                .build();
    }



    public List<BoardAllReadResponse> findAllMyBoards(String email) throws IOException{

        try{
            Member member = memberRepository.findByEmail(email).orElseThrow();
            List<Board> boardList = boardRepository.findAllByMember(member);

            List<BoardAllReadResponse> responseList = new ArrayList<>();

            for(Board board : boardList){
                Image image = imageRepository.findAllByBoardId(board.getId()).get(0);
                String imageUrl = image.getUrl();

                responseList.add(
                        new BoardAllReadResponse(board.getId(),  writingTimeToString(board.getCreateDate()),board.getContent(), imageUrl)
                );
            }
            return responseList;
        }catch(Exception e){
            log.info("findAllMyPrograms 오류,{}",e);
        }
        return null;
    }


    @Transactional
    public Long updateBoard(BoardUpdateRequest boardUpdateRequest, MultipartFile[] newImageList, Long boardId, String email)throws IOException{

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("program이 없습니다"));

        //Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("member 이 없습니다"));
        if(!board.getMember().getEmail().equals(email)){
            throw new AccessDeniedException("program을 수정할 권한이 없습니다.");
        }

        imageRepository.deleteAllByBoardId(boardId);

        imageService.uploadBoardImages(board, newImageList);

        board.updateBoard(boardUpdateRequest);

        boardRepository.save(board);

        return board.getId();
    }


    @Transactional
    public void deleteBoard(Long boardId,String email) throws AccessDeniedException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("board 이 없습니다"));
        if(!board.getMember().getEmail().equals(email)){
            throw new AccessDeniedException("board 을 삭제할 권한이 없습니다.");
        }

        // delete private chat
        boardRepository.delete(board);
    }


    public static String writingTimeToString(LocalDateTime writingTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.of("Asia/Seoul"));

        log.info("시간 {}",writingTime.atZone(ZoneId.of("Asia/Seoul")).format(formatter));
        return writingTime.atZone(ZoneId.of("Asia/Seoul")).format(formatter);
    }

    public static String convertStringToCustomFormat(String writingTimeStr) {
        LocalDateTime writingTime = LocalDateTime.parse(writingTimeStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return writingTime.format(formatter);
    }


}
