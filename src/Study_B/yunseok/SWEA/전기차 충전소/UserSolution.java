package asdfasdf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

// 9시 25분 ~ 

class UserSolution {
	
	int INF = Integer.MAX_VALUE;
	
	int N;
	int[] mCost;
	int[][] edgeMap;
	Map<Integer, Integer> edges;
	
	class Node implements Comparable<Node>{
		int cur;
		int cost;
		int total;
		long[] visited;
		
		Node(int cur, int cost, int total, long[] visited) {
			this.cur = cur;
			this.cost = cost;
			this.total = total;
			this.visited = visited;
		}
		
		@Override
		public int compareTo(Node n) {
			return this.total - n.total;
		}
	}
	
	public void init(int N, int mCost[], int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
		
		this.N = N;
		this.mCost = mCost;
		
		edgeMap = new int[N][N];
		edges = new HashMap<>();
		
		for (int i = 0; i < K; i++) {
			
			int start = sCity[i];
			int end = eCity[i];
			
			edgeMap[start][end] = mDistance[i];
			edges.put(mId[i], (start << 16 | end));
		}
		
		for (int[] x : edgeMap) {
		System.out.println("  --  " + Arrays.toString(x));
	}
		
		return;
	}

	// 1400 회 호출
	public void add(int mId, int sCity, int eCity, int mDistance) {
		
		edgeMap[sCity][eCity] = mDistance;
		edges.put(mId, sCity << 16 | eCity);
		
//		for (int[] x : edgeMap) {
//			System.out.println("  --  " + Arrays.toString(x));
//		}
		return;
	}

	// 500 회 호출
	public void remove(int mId) {
		int startAndEnd = edges.get(mId);
		int start = startAndEnd >> 16;
		int end = startAndEnd & 0xFFFF;
		
		edgeMap[start][end] = 0;
		edges.remove(mId);
		
		return;
	}

	// 100 회 호출
	public int cost(int sCity, int eCity) {
		
//		System.out.println(" sCity, eCity : " + sCity + " " + eCity);
		PriorityQueue<Node> pq = new PriorityQueue<>();
		long[] visited = new long[N / 60 + 1];
		visited[sCity / 60] |= 1 << sCity % 60;
		pq.add(new Node(sCity, mCost[sCity], 0, visited));
		int[][] dist = new int[N][2];
		
		for (int[] x : dist) {
			x[0] = INF;
			x[1] = INF;
		}
		
		dist[sCity][0] = 0;
		dist[sCity][1] = mCost[sCity];
		
		
		int result = 0;
		
		while(!pq.isEmpty()) {
			Node n = pq.poll();
			int cur = n.cur;

			if (cur == eCity) {
				result = n.total;
				break;
			}
			
			long[] curVisited = n.visited;
			long[] v = new long[curVisited.length];
			
			for (int i = 0; i < curVisited.length; i++) {
				v[i] = curVisited[i];
			}
			
			v[n.cur / 60] |= 1 << n.cur % 60;
			
			for (int i = 0 ; i < N; i++) {
				
				if ((v[i/60] & 1 << (i % 60)) > 0) continue;
				
				if (edgeMap[cur][i] != 0) {
					int total = edgeMap[cur][i] * n.cost;
					
					if (dist[i][0] <= n.total + total && dist[i][1] <= n.cost) continue;
					pq.add(new Node(i, Math.min(mCost[i], n.cost), n.total + total, v));

				}
			}
			
		}
		
		if (result == 0) return -1;
		return result;
	}
}