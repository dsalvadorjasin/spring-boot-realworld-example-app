package io.spring.core.tagfollow;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class TagFollow {
  private String userId;
  private String tagId;

  public TagFollow(String userId, String tagId) {
    this.userId = userId;
    this.tagId = tagId;
  }
}
