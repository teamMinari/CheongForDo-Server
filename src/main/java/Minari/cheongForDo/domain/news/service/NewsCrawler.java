package Minari.cheongForDo.domain.news.service;

import Minari.cheongForDo.domain.news.dto.CrawlingResult;
import Minari.cheongForDo.global.exception.CustomErrorCode;
import Minari.cheongForDo.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NewsCrawler {
    private final HttpClient client;

    public List<CrawlingResult> crawl(String url, String category) {
        try {
            var m = client.send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create(url))
                            .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            List<CrawlingResult> res = new ArrayList<>();

            if ("hotnews".equals(category)) {
                // 핫뉴스일 때의 크롤링 처리
                Elements hotNewsItems = Jsoup.parse(m.body())
                        .body()
                        .select("div.sa_item_flex._LAZY_LOADING_WRAP");

                for (Element div : hotNewsItems) {
                    Element thumbnailElement = div.selectFirst("div.sa_thumb img");
                    String thumbnail = thumbnailElement != null ? thumbnailElement.attr("data-src") : null;

                    Element titleElement = div.selectFirst("a.sa_text_title");
                    String title = titleElement != null && titleElement.selectFirst("strong.sa_text_strong") != null
                            ? titleElement.selectFirst("strong.sa_text_strong").text().strip()
                            : null;
                    String postUrl = titleElement != null ? titleElement.attr("abs:href") : null;

                    Element companyElement = div.selectFirst("div.sa_text_press");
                    String company = companyElement != null ? companyElement.text().strip() : null;

                    String uploadTime = null;

                    res.add(new CrawlingResult(title, postUrl, company, thumbnail, uploadTime));
                }
            } else {
                // 핫뉴스가 아닌 다른 카테고리의 크롤링 처리
                Elements divItems = Jsoup.parse(m.body())
                        .body()
                        .select("li.sa_item._LAZY_LOADING_WRAP");

                for (Element div : divItems) {
                    var sel = div.selectFirst("a.sa_text_title");
                    String title = sel != null && sel.selectFirst("strong.sa_text_strong") != null
                            ? sel.selectFirst("strong.sa_text_strong").text().strip()
                            : null;
                    String postUrl = sel != null ? sel.attr("abs:href") : null;

                    sel = div.selectFirst("div.sa_text_press");
                    String company = sel != null ? sel.text().strip() : null;

                    sel = div.selectFirst("div.sa_thumb img");
                    String thumbnail = sel != null ? sel.attr("data-src") : null;

                    if (thumbnail == null || thumbnail.isEmpty()) {
                        continue;
                    }

                    sel = div.selectFirst("div.sa_text_datetime.is_recent");
                    String uploadTime = sel != null ? sel.text().strip() : null;

                    res.add(new CrawlingResult(title, postUrl, company, thumbnail, uploadTime));
                }
            }
            return res;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new CustomException(CustomErrorCode.SERVER_ERROR);
        }
    }
}
