package com.finalproject.chorok.post.controller;

import com.finalproject.chorok.post.dto.*;
import com.finalproject.chorok.post.repository.PostRepository;
import com.finalproject.chorok.post.service.PostService;
import com.finalproject.chorok.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * [Controller] - 게시판 Controller
 *
 * @class   : PostController
 * @author  : 김주호
 * @since   : 2022.04.30
 * @version : 1.0
 *
 *   수정일     수정자             수정내용
 *  --------   --------    ---------------------------
 *
 */

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;


    /*
     * 플랜테리어 조회(queryParameter로 필터링)
     * @Example : read-posts?postTypecode = postType01
     */
    @GetMapping("/read-posts")
    public ResponseEntity<PostPagingDto> readPosts
    (
            @Valid PlantriaFilterRequestDto postSearchRequestDto,
            @PageableDefault Pageable pageable
    ){
        return ResponseEntity.status(HttpStatus.OK).body(postService.planteriorReadPosts(postSearchRequestDto,pageable));
  
    }

    /*
     * 플렌테리어 통합 검색
     * @Example : /search-post/planterior?keyword=
     */
    @GetMapping("/search-post/integrate/planterior")
    public ResponseEntity<PostSearchResponseDto> integrateSearchPlanterior
    (
            PlantriaFilterRequestDto postSearchRequestDto
    ){

        return ResponseEntity.status(HttpStatus.OK).body(postService.integrateSearchPlanterior(postSearchRequestDto));

    }

    /*
     * 플렌테리어 통합 검색 - 사진
     */
    @GetMapping("/search-post/photo/planterior")
    public ResponseEntity<PostPagingDto> photoSearchPlanterior(
            PlantriaFilterRequestDto postSearchRequestDto,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.photoSearchPlanterior(postSearchRequestDto,pageable));

    }

    /*
     * 플렌테리어 통합 검색 - 식물도감
     * 식물도감 조회
     */
    @GetMapping("/search-post/dictionary/planterior")
    public ResponseEntity<PostPagingDto> dictionarySearchPlanterior(
            DictionaryFilterDto dictionaryFilterDto,
            @PageableDefault Pageable pageable
    ) {
        return  ResponseEntity.status(HttpStatus.OK).body(postService.dictionarySearchPlantria(dictionaryFilterDto,pageable));

    }

    /*
     * 초록톡 전체 조회 (로그인 시)
     * @Example : read-posts/community?postTypeCode=
     */
    @GetMapping("/read-posts/community")
    public ResponseEntity<PostPagingDto> readPostsCommunity(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(name = "postTypeCode",required = false) String postTypeCode,
            @PageableDefault Pageable pageable
            // ? keyword
           // @RequestParam(name="keyword", required = false)  String keyword
    )  {

        return   ResponseEntity.status(HttpStatus.OK).body(postService.readPostsCommunity(userDetails.getUser(), postTypeCode,pageable));

    }

    /*
     * 초록톡 전체 조회 (비 로그인 시)
     */
    @GetMapping("/non-login/read-posts/community")
    public ResponseEntity<PostPagingDto> nonLoginReadPostsCommunity(
            @RequestParam(name = "postTypeCode",required = false) String postTypeCode,
            @PageableDefault Pageable pageable
    ) {

        return   ResponseEntity.status(HttpStatus.OK).body(postService.nonLoginReadPostsCommunity(postTypeCode,pageable));

    }

    /*
     * 플렌테리어, 초록톡 -  상세조회
     */
    @GetMapping("/read-post/detail/{postId}")
    public ResponseEntity<PostDetailResponseDto> readPostDetail(@Valid @PathVariable Long postId,@AuthenticationPrincipal UserDetailsImpl userDetails){

        return   ResponseEntity.status(HttpStatus.OK).body(postService.readPostDetail(postId, userDetails.getUser()));

    }


    // 게시글 작성
    @PostMapping("/write-post")
    public ResponseEntity<PostResponseDto> writePost(
            @ModelAttribute PostWriteRequestDto postWriteRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        System.out.println("PostController.writePost");
        System.out.println(postWriteRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(postService.writePost(postWriteRequestDto, userDetails.getUser()));


    }

    // 게시글 삭제
    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<HashMap<String, String>> deletePost(
            @Valid @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails

    ) throws IllegalAccessException {

        return  ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(postId,userDetails.getUser()));

    }

    // 게시글 수정
    @PostMapping("/update-post/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @Valid @ModelAttribute PostWriteRequestDto postWriteRequestDto,
            @RequestParam(value = "originalUrl",required = false) String originalUrl,
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException, IllegalAccessException {

        return  ResponseEntity.status(HttpStatus.OK).body( postService.updatePost(postId,postWriteRequestDto,originalUrl,userDetails.getUser()));
    }

    // 게시글 좋아요 기능
    @GetMapping("like-post/{postId}")
    public ResponseEntity<HashMap<String, String>> likePost(@Valid @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return ResponseEntity.status(HttpStatus.OK).body(postService.likePost(postId,userDetails.getUser()));

    }

    // 게시글 북마크 기능
    @GetMapping("bookmark-post/{postId}")
    public ResponseEntity<HashMap<String, String>> bookMarkPost(@Valid @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return ResponseEntity.status(HttpStatus.OK).body(postService.bookMarkPost(postId,userDetails.getUser()));
    }

}

