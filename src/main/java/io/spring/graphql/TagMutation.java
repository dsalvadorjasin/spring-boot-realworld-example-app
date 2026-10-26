package io.spring.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.core.article.ArticleRepository;
import io.spring.core.tagfollow.TagFollow;
import io.spring.core.tagfollow.TagFollowRepository;
import io.spring.core.user.User;
import io.spring.graphql.DgsConstants.MUTATION;
import io.spring.graphql.exception.AuthenticationException;
import io.spring.graphql.types.Tag;
import io.spring.graphql.types.TagPayload;
import lombok.AllArgsConstructor;

@DgsComponent
@AllArgsConstructor
public class TagMutation {

  private TagFollowRepository tagFollowRepository;
  private ArticleRepository articleRepository;

  @DgsMutation(field = MUTATION.FollowTag)
  public TagPayload followTag(@InputArgument("tag") String tagName) {
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
    io.spring.core.article.Tag tag =
        articleRepository.findTagByName(tagName).orElseThrow(ResourceNotFoundException::new);
    tagFollowRepository.save(new TagFollow(user.getId(), tag.getId()));
    return buildPayload(tag.getName(), true);
  }

  @DgsMutation(field = MUTATION.UnfollowTag)
  public TagPayload unfollowTag(@InputArgument("tag") String tagName) {
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
    io.spring.core.article.Tag tag =
        articleRepository.findTagByName(tagName).orElseThrow(ResourceNotFoundException::new);
    tagFollowRepository.remove(new TagFollow(user.getId(), tag.getId()));
    return buildPayload(tag.getName(), false);
  }

  private TagPayload buildPayload(String name, boolean following) {
    return TagPayload.newBuilder()
        .tag(Tag.newBuilder().name(name).following(following).build())
        .build();
  }
}
