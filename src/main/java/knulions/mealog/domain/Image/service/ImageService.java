package knulions.mealog.domain.Image.service;


import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import knulions.mealog.domain.Image.entity.Image;
import knulions.mealog.domain.Image.entity.ImageType;
import knulions.mealog.domain.Image.repository.ImageRepository;
import knulions.mealog.domain.board.entity.Board;
import knulions.mealog.domain.market.entity.Market;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final Storage storage;

    private String bucketName = "matching_service";
    private final ImageRepository imageRepository;

    public void uploadBoardImages(Board board, MultipartFile[] multipartFiles) throws IOException {

        List<Image> images = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String uuid = UUID.randomUUID().toString();
            String originalFilename = multipartFile.getOriginalFilename();
            String type = FilenameUtils.getExtension(originalFilename);

            storage.create(BlobInfo.newBuilder(BlobId.of(bucketName, uuid))
                    .setContentType(type)
                    .build(), multipartFile.getInputStream());

            String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, uuid);
            Image image = Image.builder()
                    .url(fileUrl)
                    .board(board)
                    .imageType(ImageType.BOARD) // ImageType 설정
                    .build();

            imageRepository.save(image);
            images.add(image);
            board.getImages().add(image);

            log.info("저장!");
        }
    }


    public List<Image> getBoardImageList(Board board)
    {
        List<Image> images = imageRepository.findByBoard(Optional.ofNullable(board));

        return images;
    }

    public void uploadMarketImages(Market market, MultipartFile[] multipartFiles) throws IOException {

        List<Image> images = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String uuid = UUID.randomUUID().toString();
            String originalFilename = multipartFile.getOriginalFilename();
            String type = FilenameUtils.getExtension(originalFilename);

            storage.create(BlobInfo.newBuilder(BlobId.of(bucketName, uuid))
                    .setContentType(type)
                    .build(), multipartFile.getInputStream());

            String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, uuid);
            Image image = Image.builder()
                    .url(fileUrl)
                    .market(market)
                    .imageType(ImageType.MARKET) // ImageType 설정
                    .build();

            imageRepository.save(image);
            images.add(image);
            market.getImages().add(image);

            log.info("저장!");
        }
    }


    public List<Image> getMarketImageList(Optional<Market> market)
    {
        List<Image> images = imageRepository.findByMarket(market);

        return images;
    }



}


