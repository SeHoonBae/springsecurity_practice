package com.github.prgrms.social.controller.post;

import com.github.prgrms.social.controller.ApiResult;
import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.post.Writer;
import com.github.prgrms.social.model.user.User;
import com.github.prgrms.social.security.JwtAuthentication;
import com.github.prgrms.social.service.post.PostService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.prgrms.social.controller.ApiResult.OK;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("api")
public class PostRestController {

  private final PostService postService;

  public PostRestController(PostService postService) {
    this.postService = postService;
  }

  @PostMapping(path = "post")
  public ApiResult<PostDto> posting(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestBody PostingRequest request
  ) {
    return OK(
      new PostDto(
        postService.write(
          request.newPost(authentication.id, new Writer(authentication.email))
        )
      )
    );
  }

  @GetMapping(path = "user/{userId}/post/list")
  public ApiResult<List<PostDto>> posts(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long userId
    /*Pageable pageable*/
  ) {
    // TODO query parameter에 offset, limit 파라미터를 추가하고 페이징 처리한다.
    // offset: 페이징 offset, 기본값 0
    // limit: 최대 조회 갯수, 기본값 5
    long offset = 0;
    int limit = 5;
    return OK(
      postService.findAll(Id.of(User.class, userId) /*추가로 필요한 인자들을 선언*/, offset, limit).stream()
        .map(PostDto::new)
        .collect(toList())
    );
  }

  @PatchMapping(path = "user/{userId}/post/{postId}/like")
  public ApiResult<PostDto> like(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @PathVariable Long userId,
    @PathVariable Long postId
  ) {
    return OK(
      postService.like(/*필요한 인자들을 선언*/)
        .map(PostDto::new)
        .orElseThrow(() -> new NotImplementedException("구현이 필요합니다."))
    );
  }

}