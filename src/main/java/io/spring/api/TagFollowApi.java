package io.spring.api;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.data.TagData;
import io.spring.core.article.ArticleRepository;
import io.spring.core.article.Tag;
import io.spring.core.tagfollow.TagFollow;
import io.spring.core.tagfollow.TagFollowRepository;
import io.spring.core.user.User;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "tags/{tag}/follow")
@AllArgsConstructor
public class TagFollowApi {
  private TagFollowRepository tagFollowRepository;
  private ArticleRepository articleRepository;

  @PostMapping
  public ResponseEntity followTag(
      @PathVariable("tag") String tagName, @AuthenticationPrincipal User user) {
    Tag tag = articleRepository.findTagByName(tagName).orElseThrow(ResourceNotFoundException::new);
    tagFollowRepository.save(new TagFollow(user.getId(), tag.getId()));
    return responseTagData(new TagData(tag.getName(), true));
  }

  @DeleteMapping
  public ResponseEntity unfollowTag(
      @PathVariable("tag") String tagName, @AuthenticationPrincipal User user) {
    Tag tag = articleRepository.findTagByName(tagName).orElseThrow(ResourceNotFoundException::new);
    tagFollowRepository.remove(new TagFollow(user.getId(), tag.getId()));
    return responseTagData(new TagData(tag.getName(), false));
  }

  private ResponseEntity<HashMap<String, Object>> responseTagData(final TagData tagData) {
    return ResponseEntity.ok(
        new HashMap<String, Object>() {
          {
            put("tag", tagData);
          }
        });
  }
}
