package exe2;

import java.util.*;

public class Main {
	public static class Trie {
		static int MAX_NODE = 10000001;

		static int[][] trie = new int[MAX_NODE][26];

		static boolean[] isEnd = new boolean[MAX_NODE];

		static int nodeCount;

		public void init() {
			for (int i = 0; i <= nodeCount; i++) {
				for (int j = 0; j < 26; j++) {
					trie[i][j] = 0;
				}
				isEnd[i] = false;
			}
			nodeCount = 0;
		}

		public void insert(String str) {
			int current = 0;
			for (int i = 0; i < str.length(); i++) {
				int charIdx = str.charAt(i) - 'a';

				if (trie[current][charIdx] == 0) {
					trie[current][charIdx] = ++nodeCount;
				}
				current = trie[current][charIdx];
			}
			isEnd[current] = true;
		}

		public double cal(String[] str) {
			double totalCnt = 0;

			for (String curr : str) {
				// 모든 글자 찾아보기
				int click = 1; // 처음에는 무조건 누르기

				int current = 0;

				current = trie[current][curr.charAt(0) - 'a'];

				for (int i = 1; i < curr.length(); i++) {
					int branch = 0;
					// 해당 문자에서 나있는 가지의 수 체크
					for (int j = 0; j < 26; j++) {
						if (trie[current][j] != 0)
							branch++;
					}
					if (branch > 1 || isEnd[current])
						click++;

					current = trie[current][curr.charAt(i) - 'a'];
				}
				totalCnt += click;

			}
			return totalCnt / str.length; 

		}
	}
	static Trie trie = new Trie(); 
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in); 
		
		while(sc.hasNextInt()) {
			int n = sc.nextInt(); 
			String[] words = new String[n];
			for(int i=0 ; i<n ; i++) {
				words[i] = sc.next(); 
			}
			
			trie.init(); 
			for(int i=0 ; i< n ; i++) {
				trie.insert(words[i]); 
			}
			double result = trie.cal(words);
			System.out.printf("%.2f\n", result );
			
			
		}
	}

}
