package com.example.demo;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
public class methodTest {

    @Value("${discord.bot.password}")
    private String token;
    @Value("${discord.ch.id}")
    private String channelId;

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
    public void discordBotTest() throws InterruptedException {
        JDA jda = JDABuilder.createDefault(token).build();
        // Discord API 연결이 완료될 때까지 대기
        jda.awaitReady();
        // 메시지를 보낼 채널 ID
        // 채널 가져오기
        TextChannel channel = jda.getTextChannelById(channelId);
        // 메시지 전송
        int cnt = 0;
        try {
            // resources 경로에 있는 파일 읽기
            ClassPathResource resource = new ClassPathResource("채용공고_목록.xlsx");
            InputStream inputStream = resource.getInputStream();

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 첫 행은 헤더이므로 skip

                Cell companyCell = row.getCell(0); // 회사명
                Cell titleCell = row.getCell(1);   // 공고 제목

                String company = companyCell.getStringCellValue();
                String title = titleCell.getStringCellValue();

                String msg =
                        "```" +
                                "✨ 회사명 : " + company + "\n" +
                                "\uD83D\uDCDD 공고명 : " + title + "\n" +                                 // 코드블록 끝
                                "\uD83D\uDD17 자세한 정보는 회사 홈페이지를 참고해주세요!\n"+
                                "```";
                if (channel != null) {
                    channel.sendMessage(msg).queue();
                    System.out.println(++cnt);
                } else {
                    System.out.println("채널을 찾을 수 없습니다.");
                }
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Thread.sleep(10000);
        //jda.shutdown();
    }
}
