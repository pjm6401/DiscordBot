package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;


public class methodTest {

    @Test
    public void crawlingTest() throws IOException {
        Document doc = Jsoup.connect("https://www.saramin.co.kr/zf_user/jobs/list/job-category?cat_kewd=84&panel_type=&search_optional_item=n&search_done=y&panel_count=y&preview=y/")
                .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36")
                .timeout(10000) // 10초 타임아웃
                .get();

        Elements elements1= doc.getElementsByClass("corp");
        Elements elements2 = doc.getElementsByClass("tit");

        for(int i =3; i < elements1.size(); i++) {
            Element element1 = elements1.get(i);
            Element element2 = elements2.get(i);

            String companyName = element1.text().trim();
            String jobTitle = element2.text().trim();

            System.out.println("회사명: " + companyName);
            System.out.println("공고 제목: " + jobTitle);
            System.out.println("-----------------------------------");
        }

    }

    @Test
    public void discordBotTest() throws IOException {

    }
}
