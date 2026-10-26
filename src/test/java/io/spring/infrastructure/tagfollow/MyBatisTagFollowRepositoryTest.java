package io.spring.infrastructure.tagfollow;

import io.spring.core.tagfollow.TagFollow;
import io.spring.core.tagfollow.TagFollowRepository;
import io.spring.infrastructure.DbTestBase;
import io.spring.infrastructure.mybatis.mapper.TagFollowMapper;
import io.spring.infrastructure.repository.MyBatisTagFollowRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@Import({MyBatisTagFollowRepository.class})
public class MyBatisTagFollowRepositoryTest extends DbTestBase {
  @Autowired private TagFollowRepository tagFollowRepository;

  @Autowired private TagFollowMapper tagFollowMapper;

  @Autowired private JdbcTemplate jdbcTemplate;

  @Test
  public void should_save_and_fetch_tag_follow_success() {
    TagFollow tagFollow = new TagFollow("user1", "tag1");
    tagFollowRepository.save(tagFollow);
    Assertions.assertEquals(tagFollow, tagFollowRepository.find("user1", "tag1").get());
  }

  @Test
  public void should_ignore_duplicate_insert() {
    TagFollow tagFollow = new TagFollow("user1", "tag1");
    tagFollowRepository.save(tagFollow);
    tagFollowRepository.save(tagFollow);
    tagFollowMapper.insert(tagFollow);
    Assertions.assertEquals(
        1,
        jdbcTemplate.queryForObject(
            "select count(*) from tag_follows where user_id = ? and tag_id = ?",
            Integer.class,
            "user1",
            "tag1"));
  }

  @Test
  public void should_remove_tag_follow_success() {
    TagFollow tagFollow = new TagFollow("user1", "tag1");
    tagFollowRepository.save(tagFollow);
    tagFollowRepository.remove(tagFollow);
    Assertions.assertFalse(tagFollowRepository.find("user1", "tag1").isPresent());
    tagFollowRepository.remove(tagFollow);
  }
}
