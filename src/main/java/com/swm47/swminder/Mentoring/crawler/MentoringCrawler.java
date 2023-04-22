package com.swm47.swminder.Mentoring.crawler;

import com.swm47.swminder.Mentoring.entity.Mentoring;
import com.swm47.swminder.Mentoring.service.MentoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@PropertySource("classpath:local.properties")
@Slf4j
@RequiredArgsConstructor
public class MentoringCrawler {

    private final MentoringService mentoringService;

    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${encryptedPassword}")
    private String encryptedPassword;

    private Map<String, String> loginCookie = null;

    private final int MAX_PAGE = 60;

    @PostConstruct
    private void postConstruct() throws NoSuchAlgorithmException, KeyManagementException {
        setSSL();
    }

    @Scheduled(fixedDelay = 10 * 1000) // 10s
    private void crawlOnePage() {
        log.info("MentoringCrawler.crawlOnePage start");
        if (loginCookie == null) getLoginCookie();

        long startTime = System.currentTimeMillis();
        crawlPage(1, 2);
        long endTime = System.currentTimeMillis();
        log.info("MentoringCrawler.crawlOnePage ended: " + ((endTime - startTime) / 1000 )+ " s");
    }
    @Scheduled(fixedDelay = 10 * 60 * 1000) // 10min
    private void crawlEntirePage() {
        if (loginCookie == null) getLoginCookie();
        log.info("MentoringCrawler.crawlEntirePage start");
        long startTime = System.currentTimeMillis();
        crawlPage(1, MAX_PAGE);
        long endTime = System.currentTimeMillis();
        log.info("MentoringCrawler.crawlEntirePage ended: " + ((endTime - startTime) / 1000 )+ " s");
    }
    private void setSSL() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                        // TODO Auto-generated method stub

                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                        // TODO Auto-generated method stub
                    }
                }
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    private void getLoginCookie() {
        log.info("Crawler.getLoginCookie");

        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36";
        // 로그인(POST)
        try {
            Connection.Response loginPageResponse = Jsoup.connect("https://www.swmaestro.org/sw/member/user/forLogin.do?menuNo=200025")
                    .timeout(3000)
                    .method(Connection.Method.GET)
                    .execute();

            // 로그인 페이지에서 얻은 쿠키
            Map<String, String> loginTryCookie = loginPageResponse.cookies();

            // 로그인 페이지에서 로그인에 함께 전송하는 토큰 얻어내기
            Document loginPageDocument = loginPageResponse.parse();
            String csrfToken = loginPageDocument.select("input#csrfToken").val();

            // 전송할 폼 데이터
            Map<String, String> data = new HashMap<>();
            data.put("username", username);
            data.put("password", password);
            data.put("csrfToken", csrfToken);
            data.put("menuNo", "200025");
            data.put("loginFlag", "");

            Connection.Response response = Jsoup.connect("https://www.swmaestro.org/sw/member/user/toLogin.do")
                    .userAgent(userAgent)
                    .timeout(3000)
                    .header("Host", "www.swmaestro.org")
                    .header("Origin", "https://www.swmaestro.org")
                    .header("Referer", "https://www.swmaestro.org/sw/member/user/forLogin.do?menuNo=200025")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "ko,en;q=0.9,ko-KR;q=0.8,en-US;q=0.7,ja;q=0.6,nl;q=0.5,da;q=0.4")
                    .header("Connection", "keep-alive")
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                    .cookie("JSESSIONID", loginTryCookie.get("JSESSIONID"))
                    .data(data)
                    .method(Connection.Method.POST)
                    .execute();

            loginCookie = response.cookies();
            data.clear();
            data.put("username", username);
            data.put("password", encryptedPassword);

            Connection.Response response2 = Jsoup.connect("https://www.swmaestro.org/sw/login.do")
                    .userAgent(userAgent)
                    .timeout(3000)
                    .header("Host", "www.swmaestro.org")
                    .header("Origin", "https://www.swmaestro.org")
                    .header("Referer", "https://www.swmaestro.org/sw/member/user/toLogin.do")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "ko,en;q=0.9,ko-KR;q=0.8,en-US;q=0.7,ja;q=0.6,nl;q=0.5,da;q=0.4")
                    .header("Connection", "keep-alive")
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                    .cookie("JSESSIONID", loginTryCookie.get("JSESSIONID"))
                    .data(data)
                    .method(Connection.Method.POST)
                    .execute();

            log.info("response2 = " + response2);
            loginCookie = response2.cookies();
            log.info("loginCookie = " + loginCookie);
            log.info("get login cookie success");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void crawlPage(int start, int end) {
        log.info("MentoringCrawler.crawlPage(" + start + ", " + end + ")");

        try {
            for (int cur = start; cur < end; ++cur) {
                Document doc = Jsoup.connect("https://www.swmaestro.org/sw/mypage/mentoLec/list.do?menuNo=200046" + "&pageIndex=" + cur)
                        .cookies(loginCookie)
                        .get();
                Elements tables = doc.getElementsByTag("table");
                int i = 0;
                for (Element table : tables) {
                    if (table.getElementsContainingText("제목").size() > 0) {
                        Elements trs = table.select("tr");
                        trs.remove(0); // remove header
                        for (Element tr : trs) {
                            Elements tds = tr.getElementsByTag("td");
                            String title = tds.get(1).select("a").text();
                            String url = tds.get(1).select("a").attr("href");
//                            String content = getContent(url); // no content
                            Matcher matcher = Pattern.compile("qustnrSn=(\\d+)&").matcher(url);
                            matcher.find();
                            int qustnrSn = Integer.parseInt(matcher.group(1));
                            String applyDuration = tds.get(2).text();
                            String startEnd = tds.get(3).text();
                            String applyInfo = tds.get(4).text();
                            String[] applyDurationSplit = applyDuration.split("~ ");
                            LocalDateTime applyStartTime = LocalDate.parse(applyDurationSplit[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                            LocalDateTime applyEndTime = LocalDate.parse(applyDurationSplit[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                            //                        5 : 상태 [대기] / [접수중] / [마감]
                            String author = tds.get(6).text();
                            String createdDateStr = tds.get(7).text();
                            LocalDateTime createdDate = LocalDate.parse(createdDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                            String[] split = startEnd.split("&nbsp");
                            String[] split2 = split[1].split("~");
                            String startTimeStr = split[0].trim() + " " + (split2[0].trim().length() == 4 ? "0" + split2[0].trim() : split2[0].trim());
                            String endTimeStr = split[0].trim() + " " + (split2[1].trim().length() == 4 ? "0" + split2[1].trim() : split2[1].trim());
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, dateTimeFormatter);
                            if (startTime.isBefore(LocalDateTime.now())) {
                                continue;
                            }
                            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, dateTimeFormatter);
                            String[] applyInfoSplit = applyInfo.split("/");
                            int joinCount = Integer.parseInt(applyInfoSplit[0].trim());
                            int limitCount = Integer.parseInt(applyInfoSplit[1].trim());

                            // category is what?
                            // content
                            Mentoring mentoring = Mentoring.builder()
                                    .title(title)
                                    .author(author)
                                    .createdDate(createdDate)
                                    .startTime(startTime)
                                    .endTime(endTime)
                                    .joinCount(joinCount)
                                    .limitCount(limitCount)
                                    .qustnrSn(qustnrSn)
                                    .applyStartTime(applyStartTime)
                                    .applyEndTime(applyEndTime)
                                    .build();
                            mentoringService.upsert(mentoring);
                        }
                    }
                }
                String href = doc.select("a[title=\"마지막 목록\"]").attr("href");
                Matcher matcher = Pattern.compile("pageIndex=(\\d+)").matcher(href);
                matcher.find();
                int lastIndex = Integer.parseInt(matcher.group(1));
                if (cur == lastIndex) break; // if this was last page, stop
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getContent(String url) {
        String ret = null;
        try {
            Document doc = Jsoup.connect("https://www.swmaestro.org/" + url)
                    .cookies(loginCookie)
                    .get();
            Elements ps = doc.select("div.cont p");
            ret = "";
            for (Element p : ps) {
                ret += p.text() + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return ret;
        }

    }

}
