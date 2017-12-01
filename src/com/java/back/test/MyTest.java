package com.java.back.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.java.back.redis.RedisSpringProxy;
import com.java.back.service.MatchService;
import com.java.back.service.MemberCardService;
import com.java.back.service.MemberMatchService;
import com.java.back.service.MemberService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.Common;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class MyTest extends RedisSpringProxy {

	public static void main(String[] args) {
		List list = new ArrayList();
		for (int i = 1; i <= 15; i++) {
			list.add(i + "");
		}

		List<List<String>> randomGroup = Common.randomGroup(list);
		for (int i = 0; i < randomGroup.size(); i++) {
			System.out.println(randomGroup.get(i));
		}

		for (int i = 0; i < randomGroup.size(); i++) {
			for (int x = 0; x < randomGroup.get(i).size(); x++) {
				for (int y = 0; y <= x - 1; y++) {
					System.out.print(randomGroup.get(i).get(y) + "--"
							+ randomGroup.get(i).get(x) + "\t");
				}
				System.out.println();
			}
		}

	}

	@Test
	public void redis() {
		// flushDB();
		// flushAll();

		// saveEx("keyEx", 1,100l);

		Set<Serializable> allKeys = getAllKeys();
		for (Serializable serializable : allKeys) {
			System.out.println(serializable);
			// if (serializable.toString().contains("findAllCard~keys")) {
			// Object read = read(serializable.toString());
			// System.out.println(read.toString());
			// }

		}
		// saveEx("keyEx", 1,5L);
		// Long delete = delete("findMenu~keys");
		// System.out.println(delete);
	}

	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MatchService matchService;
	@Autowired
	private MemberMatchService memberMatchService;

	@Test
	public void mds() {
		long startTime = System.currentTimeMillis();// 获取当前时间
		JSONReturn findList = memberCardService.findList(1, "", 0, 0);
		long endTime = System.currentTimeMillis();
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms");

	}

	@Test
	public void ms() {
		long startTime = System.currentTimeMillis();// 获取当前时间
		JSONObject findListByserVal = memberService.findListByserVal("");
		long endTime = System.currentTimeMillis();
		System.out.println(findListByserVal);
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms");

	}

	@Test
	public void mmch() {
		matchService.findMatchList(1, "");

	}

	@Test
	public void mmatch() {
		// memberMatchService.findMeMatchList("1122");

	}

	@Test
	public void mall() {
		memberService.findAll("", 0, "");

	}

}
