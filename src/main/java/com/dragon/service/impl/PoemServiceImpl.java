package com.dragon.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dragon.entity.Poem;
import com.dragon.mapper.PoemMapper;
import com.dragon.service.PoemService;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

@Service
public class PoemServiceImpl extends ServiceImpl<PoemMapper, Poem> implements PoemService {
	
	@Override
	@Transactional
	public void process(ResultItems resultItems, Task task) {
		Map<String, Object> map = resultItems.getAll();
		// 抑制unchecked警告
		@SuppressWarnings("unchecked")
		List<Poem> poemList = (List<Poem>) map.get("poemList");	
		this.saveBatch(poemList);
	}
}
