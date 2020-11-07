package com.dragon.controller;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dragon.entity.Poem;
import com.dragon.service.PoemService;

@RestController
@RequestMapping("/poem")
public class PoemController {
	
	@Autowired
	private PoemService poemService;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@GetMapping("/{id}")
	public Poem queryPoemById(@PathVariable String id) {
		// 先尝试从redis缓存中读取
		String value = redisTemplate.opsForValue().get(id);
		Poem poem = null;
		// 缓存中读取不到，再从数据库中读取
		if (Objects.isNull(value)) {
			
			System.out.println("redis缓存未命中，从mysql中读取。。。");
			
			poem = poemService.getById(id);
			if (Objects.isNull(poem)) {
				poem = new Poem();
				poem.setTitle("404 Not Found!");
				poem.setEra("unknown");
				poem.setAuthor("佚名");
				poem.setContent("路漫漫其修远兮，吾将上下而求索。");
			}
			
			// 添加无法访问到的key，注意这里容易导致缓存穿透，所以设置了过期时间
			value = JSONObject.toJSONString(poem);
			redisTemplate.opsForValue().set(id, value, 60, TimeUnit.SECONDS);
			System.out.println("将数据添加到redis中");  // 包括根本不存在的数据，不过这样会导致刚更新的数据无法访问，
			                                        // 所以过期时间不能太长
		} else {
			poem = JSONObject.parseObject(value, Poem.class);
			System.out.println("直接从redis中读取数据。。。");
		}
		return poem;
	}
}
