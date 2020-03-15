package com.wangsan.study.http;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * created by wangsan on 2020/2/4 in project of example .
 *
 * @author wangsan
 * @date 2020/2/4
 */
public class HelloJsoup {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.baidu.com").get();
        System.out.println(doc.title());
        Elements bottom = doc.select("#cp");
        System.out.println(bottom.text());
        System.out.println(bottom.size());
    }
}
