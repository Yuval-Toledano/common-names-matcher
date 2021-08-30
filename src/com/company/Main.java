package com.company;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {

	private static final String TEXT_URL = "http://norvig.com/big.txt";
	private static final int MAX_LINES = 1000;
	private static final String COMMON_NAMES =
			"James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas,Christopher,Daniel,Paul," +
			"Mark,Donald" +
			",George,Kenneth,Steven,Edward,Brian,Ronald,Anthony,Kevin,Jason,Matthew,Gary,Timothy,Jose,Larry," +
			"Jeffrey," +
			"Frank,Scott,Eric,Stephen,Andrew,Raymond,Gregory,Joshua,Jerry,Dennis,Walter,Patrick,Peter," +
			"Harold,Douglas," +
			"Henry,Carl,Arthur,Ryan,Roger";

	// stringBuffer is thread safe
//	private static StringBuffer content = new StringBuffer();
	private static ArrayList<String> lines = new ArrayList<>();
	private static int currLine = 1;
	private static int currChar = 0;
	private static final CommonNamesMatcher matcher = new CommonNamesMatcher(COMMON_NAMES);
	private static CommonNamesAggregator aggregator = new CommonNamesAggregator();

	private static void readText(URL url) throws IOException, ExecutionException, InterruptedException {
		Scanner scanner = new Scanner(url.openStream());
		while (scanner.hasNextLine()) {
			int charCounter = 0;
			int lineCounter = 0;
			// 1000 lines each part
			while (lineCounter < MAX_LINES && scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lines.add(line);
				charCounter += line.length();
				lineCounter++;
			}
			Future<HashMap<String, ArrayList<WordPosition>>> commonNamesMap = matcher.nameMatch(lines,
																							   currLine,
																					  currChar);
			// TODO: fix
			while (!commonNamesMap.isDone()) {
				System.out.println("not done");
			}
			System.out.println(commonNamesMap.get());
			aggregator.add(commonNamesMap);
			System.out.println("AFTER ADD");
			currLine += lineCounter;
			currChar += charCounter;


		}
	}

	public static void main(String[] args) {
		try {
			URL url = new URL(TEXT_URL);
			readText(url);
			aggregator.getResult();
			matcher.shutdown();
		} catch (Exception e) {
			System.err.println("ERROR: " + e.getMessage());
		}

	}
}
