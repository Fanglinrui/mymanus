package org.releaf.mymanus.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class HtmlParserTool {

    public static void main(String[] args) {
        String url = "https://docs.dify.ai/zh-hans";
        System.out.println(parseHtml(url));
    }

    public static String parseHtml(String url) {
        InputStream inputStream = downloadWebPage(url);
        return parseHtmlWithTika(inputStream);
    }

    public static InputStream downloadWebPage(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return connection.getInputStream();
            } else {
                throw new RuntimeException("Error downloading web page: %s".formatted(urlString));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String parseHtmlWithTika(InputStream inputStream) {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        HtmlParser parser = new HtmlParser();
        try {
            parser.parse(inputStream, handler, metadata, context);
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException("Failed to parse the html content ", e);
        }
        return handler.toString();
    }
}
