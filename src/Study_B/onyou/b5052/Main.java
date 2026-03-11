package exe2; 
import java.util.*; 
public class Main {
	public static class Trie{
		static int MAX_NODE = 10000* 10 + 1;
		
		static int[][] trie = new int[MAX_NODE][10];
		
		static boolean[] isEnd = new boolean[MAX_NODE]; 
		
		static int nodeCount; 
		
		public void init() {
			for(int i=0; i<= nodeCount ; i++) {
				for(int j=0 ; j< 10 ; j++) {
					trie[i][j] = 0; 
				}
				isEnd[i] = false; 
			}
			nodeCount = 0; 
		}
		
		public void insert(String str) {
			int current = 0; // 루트에서 시작 
			
			for(int i=0 ; i< str.length() ; i++) {
				int charIdx = str.charAt(i) - '0'; 
				
				// 돌아보고 아직 방문 하지 않았다면 
				if(trie[current][charIdx] ==0) {
					trie[current][charIdx] = ++nodeCount; 
				}
				
				current = trie[current][charIdx]; 
				
			}
			isEnd[current] = true; 
			
		}
		// 있는지 없는지 확인 
		public int search(String str) {
			int current = 0; 
			for(int i=0 ; i< str.length() ;i++) {
				int charIdx = str.charAt(i) -'0'; 
				
				// 내 안에 다른 문자열이 있는지 확인 
				if(isEnd[current] &&  i < str.length()) {
					return 0; 
				}
				
				current = trie[current][charIdx]; 
			}
			// 내 단어가 끝났는데 아직 밑에 더 있다면? 
			for(int j=0 ; j < 10 ; j++) {
				// 이거는 내 마지막 숫자의 자식이 더 있는지 없는지 확인하는 로직. 
				if(trie[current][j] !=0) {
					return 0; 
				}
			}
			
			return 1; 
		}
	}
	// 전역으로 선언해두자 
	static Trie trie = new Trie(); 
	
	public static void main(String[] args) {
	
		Scanner sc = new Scanner(System.in); 
		
		int t = sc.nextInt(); 
		
		for(int tc=1 ; tc<= t; tc++) {
			trie.init(); 	// 트라이 생성 
			int n = sc.nextInt(); 
			sc.nextLine(); // 방지용 
			String[] arr = new String[n]; 
			for(int i=0 ; i<n ; i++) {
				String str = sc.nextLine(); 
				
				arr[i] = str; 
			}
			// 다 넣어보고 중간에  isEnd가 있는지만 확인 
			
			for(int i=0; i<n ; i++) {
				trie.insert(arr[i]); 
			}
			int cnt = 0; 
			for(int i=0; i<n ; i++) {
				cnt += trie.search(arr[i]); 
			}
			
			if(cnt ==n) System.out.println("YES");
			else System.out.println("NO");
			
		}
		
		
		
	}
	
	

}
