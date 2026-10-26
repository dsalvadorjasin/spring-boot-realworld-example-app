package io.spring.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.spring.JacksonCustomizations;
import io.spring.api.security.WebSecurityConfig;
import io.spring.core.article.ArticleRepository;
import io.spring.core.article.Tag;
import io.spring.core.tagfollow.TagFollow;
import io.spring.core.tagfollow.TagFollowRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TagFollowApi.class)
@Import({WebSecurityConfig.class, JacksonCustomizations.class})
public class TagFollowApiTest extends TestWithCurrentUser {
  @Autowired private MockMvc mvc;

  @MockBean private TagFollowRepository tagFollowRepository;

  @MockBean private ArticleRepository articleRepository;

  private Tag tag;

  @BeforeEach
  public void setUp() throws Exception {
    super.setUp();
    RestAssuredMockMvc.mockMvc(mvc);
    tag = new Tag("java");
    when(articleRepository.findTagByName(eq("java"))).thenReturn(Optional.of(tag));
    when(articleRepository.findTagByName(eq("unknown"))).thenReturn(Optional.empty());
  }

  @Test
  public void should_follow_tag_success() throws Exception {
    given()
        .header("Authorization", "Token " + token)
        .when()
        .post("/tags/{tag}/follow", "java")
        .prettyPeek()
        .then()
        .statusCode(200)
        .body("tag.name", equalTo("java"))
        .body("tag.following", equalTo(true));

    verify(tagFollowRepository).save(new TagFollow(user.getId(), tag.getId()));
  }

  @Test
  public void should_follow_tag_twice_success() throws Exception {
    for (int i = 0; i < 2; i++) {
      given()
          .header("Authorization", "Token " + token)
          .when()
          .post("/tags/{tag}/follow", "java")
          .then()
          .statusCode(200)
          .body("tag.following", equalTo(true));
    }

    verify(tagFollowRepository, times(2)).save(new TagFollow(user.getId(), tag.getId()));
  }

  @Test
  public void should_unfollow_tag_success() throws Exception {
    given()
        .header("Authorization", "Token " + token)
        .when()
        .delete("/tags/{tag}/follow", "java")
        .prettyPeek()
        .then()
        .statusCode(200)
        .body("tag.name", equalTo("java"))
        .body("tag.following", equalTo(false));

    verify(tagFollowRepository).remove(new TagFollow(user.getId(), tag.getId()));
  }

  @Test
  public void should_unfollow_tag_not_followed_success() throws Exception {
    for (int i = 0; i < 2; i++) {
      given()
          .header("Authorization", "Token " + token)
          .when()
          .delete("/tags/{tag}/follow", "java")
          .then()
          .statusCode(200)
          .body("tag.following", equalTo(false));
    }
  }

  @Test
  public void should_get_404_when_following_unknown_tag() throws Exception {
    given()
        .header("Authorization", "Token " + token)
        .when()
        .post("/tags/{tag}/follow", "unknown")
        .then()
        .statusCode(404);
    given()
        .header("Authorization", "Token " + token)
        .when()
        .delete("/tags/{tag}/follow", "unknown")
        .then()
        .statusCode(404);

    verify(tagFollowRepository, never()).save(any());
    verify(tagFollowRepository, never()).remove(any());
  }

  @Test
  public void should_get_401_when_following_tag_without_login() throws Exception {
    given().when().post("/tags/{tag}/follow", "java").then().statusCode(401);
    given().when().post("/tags/{tag}/follow/", "java").then().statusCode(401);
    given().when().delete("/tags/{tag}/follow", "java").then().statusCode(401);
    given().when().delete("/tags/{tag}/follow/", "java").then().statusCode(401);

    verify(tagFollowRepository, never()).save(any());
    verify(tagFollowRepository, never()).remove(any());
  }
}
