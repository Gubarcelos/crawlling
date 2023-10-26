package com.crawlling.backend.services;

import com.crawlling.backend.domain.interfaces.services.WebCrawlerService;
import com.crawlling.backend.domain.models.Url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawlerServiceImpl implements WebCrawlerService {

    private static final String URL_REGEX = "(?i)href\\s*=\\s*\"([^\"]*\\.html)\"";
    @Override
    public String fetchPageContent(String url, String keyword) throws IOException {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                    return content.toString();
                }
            } else {
                System.err.println("HTTP Error: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Erro de E/S: " + e.getMessage());
            return null;
        }
    }
    @Override
    public HashSet<Url> extractLinks(String htmlContent, String baseUrl) {
        HashSet<Url> links = new HashSet <>();
        Pattern pattern = Pattern.compile(URL_REGEX);
        Matcher matcher = pattern.matcher(htmlContent);

        while (matcher.find()){
            Url url = new Url();
            String link = matcher.group(1);
            link = link.replaceAll("^[^a-zA-Z0-9]+", "");
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                try {
                    URI baseUri = new URI(baseUrl);
                    URI resolvedUri = baseUri.resolve(link).normalize();
                    link = resolvedUri.toString();
                } catch (URISyntaxException e) {
                    // Tratar exceção, se necessário
                }
            }
            if (isValidUrl(link) && link.contains("http://hiring.axreng.com")) {
                url.setLink(link);
                links.add(url);
            }else {
                System.out.println(link);
            }
        }
        return  links;
    }

    @Override
    public boolean searchKeyword(String htmlContent, String keyword) {
        return htmlContent.toLowerCase().contains(keyword);
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
