package com.vk.tottenham.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component("wikiTranslator")
public class WikiTranslator {
    private static final String WIKI_EN = "https://en.wikipedia.org/wiki/{{term}}";
    private static final String WIKI_RU = "https://ru.wikipedia.org/wiki/";

    public String translate(String term) {
        try {
            Document content = Jsoup.parse(getWikiUrl(term), 10000);
            Elements russianLinks = content.getElementsByAttributeValueContaining("href", WIKI_RU);
            if (russianLinks.isEmpty()) {
                return null;
            } else {
                Element russianLink = russianLinks.get(0);
                String decodedString = URLDecoder.decode(russianLink.attr("href"), "UTF-8").replace(WIKI_RU, "").replace("_", " ");
                if (decodedString.contains(",")) {
                    String[] splitDecodedString = decodedString.split(",");
                    decodedString = splitDecodedString[1].trim() + " " + splitDecodedString[0].trim();
                } 
                return decodedString;
            }
        } catch (IOException e) {
            // swallow
        }
        return null;
    }

    private URL getWikiUrl(String term) throws MalformedURLException {
        String termWithUnderScores = String.join("_", term.split("\\s"));
        return new URL(WIKI_EN.replace("{{term}}", termWithUnderScores));
    }

    public static void main(String[] args) {
        WikiTranslator translator = new WikiTranslator();
        System.out.println(translator.translate("Wesley Sneijder"));
    }
}
