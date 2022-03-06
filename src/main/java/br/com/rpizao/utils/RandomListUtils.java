package br.com.rpizao.utils;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;

public final class RandomListUtils {
	private static final List<String> WORDS = 
			Arrays.asList("love", "work", "money", "sad", "terror", "animal", "island", "secure", "football", 
					"king", "last", "wall", "smell", "terrible", "shadow", "hello", "bye bye", "mom dad", "back",
					"sexy", "another", "war game", "razor", "contraints");

	public static String randomWord() {
		return WORDS.get(RandomUtils.nextInt(0, WORDS.size()));
	}
	
	public static <T> T randomOnList(List<T> list) {
		if(list.isEmpty()) return null;
		return list.get(RandomUtils.nextInt(0, list.size()));
	}
	
}
