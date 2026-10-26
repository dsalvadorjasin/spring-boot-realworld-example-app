package io.spring.infrastructure.mybatis.mapper;

import io.spring.core.tagfollow.TagFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TagFollowMapper {
  TagFollow find(@Param("userId") String userId, @Param("tagId") String tagId);

  void insert(@Param("tagFollow") TagFollow tagFollow);

  void delete(@Param("tagFollow") TagFollow tagFollow);
}
