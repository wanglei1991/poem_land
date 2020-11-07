package com.dragon.task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import com.dragon.entity.Poem;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

@Component
public class PoemPageProcessor implements PageProcessor {

	private Site site = Site.me().addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
			+ " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3100.0 Safari/537.36");
	
	@Override
	public void process(Page page) {
		
		String currentUrl = page.getUrl().toString();
		
		// webmagic的css选择器对于文本处理有点鸡肋了，还是使用JSoup方便一点。
		String html = page.getHtml().toString();
		Document doc = Jsoup.parse(html);
		
		List<String> titleList = doc.select("body > div.main3 > div.left div.cont > p:nth-child(2)")
								    .stream()
								    .map(Element::text)         
								    .collect(Collectors.toList());
		
		
		List<String> eraList = doc.select("body > div.main3 > div.left  div.cont > p.source > a:nth-child(1)")
								  .stream()
								  .map(Element::text)
								  .collect(Collectors.toList());
		
		List<String> authorList = doc.select("body > div.main3 > div.left  div.cont > p.source > a:nth-child(3)")
									 .stream()
									 .map(Element::text)
									 .collect(Collectors.toList());
		
		List<String> contentList = doc.select("body > div.main3 > div.left  div.cont > div.contson")
									  .stream()
									  .map(Element::text)
									  .collect(Collectors.toList());
		
		String nextUrl = page.getHtml().$("#FromPage > div > a.amore").links().get();
		
		// 还是要加一个日志，才行，不然报错了看不到，它居然没处理！
		int len = titleList.size();
		List<Poem> poemList = new ArrayList<>();
		for (int i = 0; i < len; i++) {
			Poem poem = new Poem();
			poem.setTitle(titleList.get(i));
			poem.setEra(eraList.get(i));
			poem.setAuthor(authorList.get(i));
			poem.setContent(contentList.get(i));
			poemList.add(poem);
		}
		
		page.putField("poemList", poemList);
		
		// 如果当前页是第10页，说明爬取结束，不再添加url
		if (currentUrl.contains("default_4A222222222222A10")) {
			return ; // 爬取到第十页直接返回
		} else {
			page.addTargetRequest(nextUrl); // 服务器限制，不登陆只能爬取10页
		}
		
	}

	@Override
	public Site getSite() {
		return site;
	}
}
