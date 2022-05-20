package com.finalproject.chorok.post.utils;

import com.finalproject.chorok.common.Image.ImageRepository;
import com.finalproject.chorok.common.Image.S3Uploader;
import com.finalproject.chorok.login.model.User;
import com.finalproject.chorok.mypage.model.PlantBookMark;
import com.finalproject.chorok.mypage.repository.PlantBookMarkRepository;
import com.finalproject.chorok.plant.model.Plant;
import com.finalproject.chorok.plant.repository.PlantRepository;
import com.finalproject.chorok.post.dto.comment.CommentResponseDto;
import com.finalproject.chorok.post.model.*;
import com.finalproject.chorok.post.repository.*;
import com.finalproject.chorok.plant.model.PlantPlace;
import com.finalproject.chorok.plant.repository.PlantPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommUtils {
    private final PlantRepository plantRepository;
    private final PlantPlaceRepository plantPlaceRepository;
    private final PostTypeRepository postTypeRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final PostBookMarkRepository postBookMarkRepository;
    private final ImageRepository imageRepository;
    private final PlantBookMarkRepository plantBookMarkRepository;
    private final S3Uploader s3Uploader;

    public static final Long DEFAULT_PAGE_SIZE = 10L;

    // 식물장소코드로 식물장소 검색
    public  PlantPlace getPlantPlace(String plantPlaceCode){
        return plantPlaceRepository.findById(plantPlaceCode).orElseThrow(
                () -> new NullPointerException("해당 식물장소 아이디가 존재하지 않습니다.")
        );

    }
    // 게시글타입코드로 게시글타입 검색
    public  PostType getPostType(String postTypeCode){
        return postTypeRepository.findById(postTypeCode).orElseThrow(
                () -> new NullPointerException("해당 게시글 타입이 존재하지 않습니다.")
        );
    }
    // 식물번호로 식물 조회
    public Plant getPlant(Long plantNo){
        return plantRepository.findById(plantNo).orElseThrow(
                () -> new NullPointerException("해당 식물 아이디가 존재하지 않습니다.")
        );
    }
    // 게시글 번호로 게시글 조회
    public Post getPost(Long postId){
        return postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("해당 게시글이 존재하지 않습니다.")
        );
    }
    // 댓글 번호로 댓글 조회
    public Comment getComment(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(
                ()-> new NullPointerException("해당 댓글이 존재하지 않습니다.")
        );
    }
    // 게시글 번호로 댓글 리스트 조회
    public List<CommentResponseDto>  getCommentList(Long postId){
        return commentRepository.findCommentToPostIdQuery(postId);
    }
    // 게시글 좋아요 조회
    public PostLike getLikePost(Long postId, User user){

        return postLikeRepository.findUserLikeQuery(user.getUserId(),postId);

    }
    // 게시글 북마크 조회
    public PostBookMark getBookMarkPost(Long postId, User user){

        return postBookMarkRepository.findUserBookMarkQuery(user.getUserId(),postId);

    }

    // 식물 북마크 조회
    public PlantBookMark getPlantBookMark(User user, Long plantNo){

        return plantBookMarkRepository.findUserPlantBookMarkQuery(user.getUserId(),plantNo);

    }

    // 반환값 없는 API 반환값 설정
    public HashMap<String,String> responseHashMap(HttpStatus httpCode){
        HashMap<String,String> hs = new HashMap<>();

        hs.put("StatusCode",String.valueOf(httpCode));
        hs.put("msg","성공적으로 완료되었습니다");
        return hs;
    }
    // 좋아요 북마크 반환값
    public HashMap<String,String> toggleResponseHashMap(Boolean result){
        HashMap<String,String> hs = new HashMap<>();

        hs.put("result",String.valueOf(result));
        hs.put("msg","성공적으로 완료되었습니다");
        return hs;
    }

    // 게시글 좋아요 값 유무 확인
    public Boolean LikePostChk(Long postId, User user){
        return getLikePost(postId, user) != null;
    }
    // 게시글 북마크 값 유무 확인
    public Boolean BookMarkPostChk(Long postId, User user){
        return getBookMarkPost(postId, user) != null;
    }

    // 초록톡 - 전체 postTypeCode 조회
    public List<String> getAllCommunityCode(){
        return postTypeRepository.findByAllCommunityQuery();
    }

    // 초록톡 - postTypeCode 하나 조회
    public List<String> getCommunityCode(String postTypeCode){

        return postTypeRepository.findAllByPostTypeCodeQuery(postTypeCode);
    }

    // 게시글에 사진 있는지 확인하고 있으면 삭제
    public void postPhotoDelete(Long postId) {
        Post post = getPost(postId);
        if(!post.getPostImgUrl().isEmpty() || post.getPostImgUrl() == null){
            imageRepository.deleteByImageUrl(post.getPostImgUrl());
        }
    }
    // 사진 저장
    public String postPhotoSave(MultipartFile file) throws IOException {

        if(!file.isEmpty() || file !=null){
            return s3Uploader.upload(file, "static");
        }
        return null;
    }
    // 플랜테리어 사진 유무 체크
    public void planteriorFileChk(String postTypeCode, MultipartFile file){
        if(postTypeCode.equals("postType01") || postTypeCode == "postType01"){
            if(file.isEmpty() || file ==null)
                throw new NullPointerException("플랜테이어는 사진이 필수조건입니다.");
        }
    }

    // 게시글 등록할때 플렌테리어 이외를 게시판 plantPlaceCode ExceptionCHk

    public String planteriorPlantPlaceChk(String postTypeCode, String plantPlaceCode) {

        if (!postTypeCode.equals("postType01")) {
            return null;
        }
        return plantPlaceCode;
    }

}

