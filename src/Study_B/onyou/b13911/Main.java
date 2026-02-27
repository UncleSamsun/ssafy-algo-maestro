import java.io.*;
import java.util.*;

public class exe3 {
	static int v;
	static ArrayList<Node>[] adj;
	static int[][] distMap;

	public static class Node implements Comparable<Node> {
		int idx;
		int distance;

		Node(int next, int distance) {
			this.idx = next;
			this.distance = distance;
		}

		@Override
		public int compareTo(Node o) {
			return this.distance - o.distance;
		}
	}

	public static void main(String[] args) throws IOException {
		// 처음 아이디어 : 그냥 모든 맥날과 모든 스벅에서 집과의 거리를 배열에 넣고 
		// 최소값을 구하려고 했음. 근데 메모리,시간초과 둘다 남
		// 그냥 어떤 맥날이 제일 가깝냐가 아니라 그냥 가까운 맥날의 거리를 저장하면 됨 
		
		
		// (1) 입력 받는 부분

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st = new StringTokenizer(br.readLine());
		v = Integer.parseInt(st.nextToken());
		int e = Integer.parseInt(st.nextToken());

		adj = new ArrayList[v + 1];
		// 그래프 초기화
		for (int i = 1; i <= v; i++) {
			adj[i] = new ArrayList<>();
		}
		// 그래프 입력 받기 
		for (int i = 0; i < e; i++) {

			st = new StringTokenizer(br.readLine());
			int first = Integer.parseInt(st.nextToken());
			int second = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());

			adj[first].add(new Node(second, w));
			adj[second].add(new Node(first, w));

		}

		// 맥날 입력 받기
		st = new StringTokenizer(br.readLine());
		int mCnt = Integer.parseInt(st.nextToken());
		int minMac = Integer.parseInt(st.nextToken());
		List<Integer> macList = new ArrayList<>();

		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < mCnt; i++) {
			macList.add(Integer.parseInt(st.nextToken()));
		}

		// 스벅 입력받기
		st = new StringTokenizer(br.readLine());
		int sCnt = Integer.parseInt(st.nextToken());
		int minStar = Integer.parseInt(st.nextToken());
		List<Integer> starList = new ArrayList<>();

		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < sCnt; i++) {
			starList.add(Integer.parseInt(st.nextToken()));
		}
		
		// (1) 입력 끝 
		
		
		// (2) 각 집에서 가장 가까운 맥날, 스벅 배열 계산하기 
		
		long[] minMacDist = func(macList.get(0), minMac, macList);


		long[] minStarDist = func(starList.get(0), minStar, starList);


		long result = Long.MAX_VALUE;
		
		
		// (3) 스벅+맥날의 합을 구하고 가장 짧은 거리 출력하기
		for (int i = 1; i <= v; i++) {

			if (minMacDist[i] > 0 && minStarDist[i] > 0 && minMacDist[i] + minStarDist[i] < result) {

				result = minMacDist[i] + minStarDist[i];
			}
		}
		result = result == Long.MAX_VALUE ? -1 : result; 
		System.out.println(result);
 

	}
	
	
	static long[] func(int startNode, int max, List<Integer> list) {
		
		long[] dist = new long[v + 1];
		Arrays.fill(dist, Integer.MAX_VALUE);

		PriorityQueue<Node> pq = new PriorityQueue<>();
		
		// 가게는 처음 거리를 0으로 초기화하고 queue에 다 넣고 시작한다.
		for (int i : list) {

			dist[i] = 0;
			pq.add(new Node(i, 0));
		}

		while (!pq.isEmpty()) {
			
			Node curNode = pq.poll();
			int curIdx = curNode.idx;
			int d = curNode.distance;

			if (dist[curIdx] < d)
				continue;
			for (Node nextNode : adj[curIdx]) {
				if (dist[nextNode.idx] > d + nextNode.distance) {
					dist[nextNode.idx] = d + nextNode.distance;
					pq.offer(new Node(nextNode.idx, (int) dist[nextNode.idx]));

				}
			}
		}
		for (int i = 1; i <= v; i++) {
			if (dist[i] > max)
				dist[i] = -1;
		}

		return dist;
	}

}
