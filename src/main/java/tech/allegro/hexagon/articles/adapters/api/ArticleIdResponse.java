package tech.allegro.hexagon.articles.adapters.api;

import tech.allegro.hexagon.articles.domain.model.ArticleId;
import com.fasterxml.jackson.annotation.JsonProperty;

class ArticleIdResponse {
    private final String id;

    private ArticleIdResponse(final String id) {
        this.id = id;
    }

    @JsonProperty("id")
    public String id() {
        return id;
    }

    public static ArticleIdResponse of(final ArticleId articleId) {
        return new ArticleIdResponse(articleId.value());
    }

}