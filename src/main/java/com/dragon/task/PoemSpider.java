package com.dragon.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dragon.service.PoemService;

import us.codecraft.webmagic.Spider;

@Component
public class PoemSpider implements CommandLineRunner {
	
	@Autowired
	private PoemPageProcessor poemPageProcessor;
	
	@Autowired
	private PoemService poemService;
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("暂停爬取！");
		Spider.create(poemPageProcessor)
			  .addUrl("https://so.gushiwen.cn/shiwen/default_4A222222222222A1.aspx")
			  .addPipeline(poemService)
			  .thread(2)
			  .start();
	}

}
