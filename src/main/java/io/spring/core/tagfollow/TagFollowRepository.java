package io.spring.core.tagfollow;

import java.util.Optional;

public interface TagFollowRepository {
  void save(TagFollow tagFollow);

  Optional<TagFollow> find(String userId, String tagId);

  void remove(TagFollow tagFollow);
}
