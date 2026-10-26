package io.spring.infrastructure.repository;

import io.spring.core.tagfollow.TagFollow;
import io.spring.core.tagfollow.TagFollowRepository;
import io.spring.infrastructure.mybatis.mapper.TagFollowMapper;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MyBatisTagFollowRepository implements TagFollowRepository {
  private TagFollowMapper mapper;

  public MyBatisTagFollowRepository(TagFollowMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void save(TagFollow tagFollow) {
    mapper.insert(tagFollow);
  }

  @Override
  public Optional<TagFollow> find(String userId, String tagId) {
    return Optional.ofNullable(mapper.find(userId, tagId));
  }

  @Override
  public void remove(TagFollow tagFollow) {
    mapper.delete(tagFollow);
  }
}
