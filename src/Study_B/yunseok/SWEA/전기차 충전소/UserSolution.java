package asdfasdf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

// 9시 25분 ~ 1시 32분

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
		
		Node(int cur, int cost, int total) {
			this.cur = cur;
			this.cost = cost;
			this.total = total;
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
		
		return;
	}

	// 1400 회 호출
	public void add(int mId, int sCity, int eCity, int mDistance) {
		
		edgeMap[sCity][eCity] = mDistance;
		edges.put(mId, sCity << 16 | eCity);
		
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
		
		PriorityQueue<Node> pq = new PriorityQueue<>();
		pq.add(new Node(sCity, mCost[sCity], 0));
		// dist[i][0] - i번째 도시까지 도달한 total 비용
		// dist[i][1] - i번째 도시까지 도달했을 때 최소 Cost
		int[][] dist = new int[N][2];
		
		for (int[] x : dist) {
			x[0] = INF;
			x[1] = INF;
		}
		
		int result = 0;
		
		while(!pq.isEmpty()) {
			Node n = pq.poll();
			int cur = n.cur;

			dist[cur][0] = n.total;
			dist[cur][1] = n.cost;

			if (cur == eCity) {
				result = n.total;
				break;
			}
			
			for (int i = 0 ; i < N; i++) {
				
				if (edgeMap[cur][i] != 0) {
					int total = edgeMap[cur][i] * n.cost;
					
					// total 비용 & Cost 모두 범위 해당 안될때만 pq에 넣어서 진행.
					if (dist[i][0] <= n.total + total && dist[i][1] <= n.cost) continue;
					pq.add(new Node(i, Math.min(mCost[i], n.cost), n.total + total));

				}
			}
			
		}
		
		if (result == 0) return -1;
		return result;
	}
}