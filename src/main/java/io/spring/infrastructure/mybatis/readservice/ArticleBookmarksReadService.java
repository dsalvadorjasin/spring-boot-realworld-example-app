package io.spring.infrastructure.mybatis.readservice;

import io.spring.application.Page;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleBookmarksReadService {
  boolean isUserBookmark(@Param("userId") String userId, @Param("articleId") String articleId);

  Set<String> userBookmarks(@Param("ids") List<String> ids, @Param("userId") String userId);

  List<String> bookmarkedArticleIds(@Param("userId") String userId, @Param("page") Page page);

  int countBookmarkedArticles(@Param("userId") String userId);
}
