package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonNamesMatcher {
	private final String[] commonNames;
	ExecutorService threadPool;

	CommonNamesMatcher(String commonNames) {
		this.commonNames = commonNames.split(",");
		this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	public Future<HashMap<String, ArrayList<WordPosition>>> nameMatch(ArrayList<String> content,
																	  int startLine,
																	   int startChar) {
		return threadPool.submit(() -> checkMatch(content, startLine, startChar));
	}

	private HashMap<String, ArrayList<WordPosition>> checkMatch(ArrayList<String> content,
																	  int startLine,
																	   int startChar) {
		HashMap<String, ArrayList<WordPosition>> res = new HashMap<>();
		for (String name : commonNames) {
			ArrayList<WordPosition> positions = new ArrayList<>();
			findWordPosition(name, content, positions, startLine, startChar);
			if (positions.size() > 0){
				res.put(name, positions);
			}
		}
		return res.size() == 0 ? null : res;
	}

	private void findWordPosition(String name, ArrayList<String> lines, ArrayList<WordPosition> positions,
								  int startLine, int startChar) {
		Pattern pattern = Pattern.compile("\\b" + name + "\\b");
		int line = 0;
		Matcher matcher;
		while(line < lines.size()){
			matcher = pattern.matcher(lines.get(line));
			if(matcher.find()){
				WordPosition position = new WordPosition(startLine + line,
														 startChar + lines.get(line).indexOf(name));
				positions.add(position);
			}
			startChar += lines.get(line).length();
			line++;

		}
	}

	public void shutdown() {
		threadPool.shutdown();
	}


}
