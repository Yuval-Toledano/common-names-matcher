package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CommonNamesAggregator {
	ArrayList<Future<HashMap<String, ArrayList<WordPosition>>>> doneFutures = new ArrayList<>();
	public static Map<String, ArrayList<WordPosition>> master = new ConcurrentHashMap<>();

	public void add(Future<HashMap<String, ArrayList<WordPosition>>> commonNamesPartMap)
			throws ExecutionException, InterruptedException {
		boolean isFutureDone = false;
		while (!isFutureDone) {
			if (commonNamesPartMap.isDone()) {
				addToMaster(commonNamesPartMap.get());
				isFutureDone = true;
			}
		}
		System.out.println("ADDING");
	}

	private void addToMaster(Map<String, ArrayList<WordPosition>> commonNamesPartMap) {
		for (String name : commonNamesPartMap.keySet()) {
			if (master.containsKey(name)) {
				ArrayList<WordPosition> wordPositionList = master.get(name);
				wordPositionList.addAll(commonNamesPartMap.get(name));
				master.put(name, wordPositionList);
			} else {
				master.put(name, commonNamesPartMap.get(name));
			}
		}

	}

	public void getResult(){
		String result = prettyResult();
		System.out.println(result);

	}

	private String prettyResult(){
		return master.toString().replace("{", "[").replace("}", "]").replace("=[", "-->");
	}

}
