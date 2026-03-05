import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.StringTokenizer;

// 9시 11분 ~ 10시 35분

public class Main {

    // 각종 상수.
    // MACDONALD, STARBUCKS : findResult 메서드 사용 기준.
    // INF : visited 배열 초기화용.
	static int MACDONALD = 0;
	static int STARBUCKS = 1;
	static int INF = Integer.MAX_VALUE;
	
    // 각종 변수.
	static int V, E;
	static Map<Integer, List<int[]>> graph;
	static int macLimit, starLimit;
	static Set<Integer> macdonald, starbucks;
	
    // PrioritoyQueue에서 사용할 Node.
	static class Node {
		
        // idx : 그래프 노드 번호.
        // dist : 현재 노드까지의 거리.
		int idx;
		int dist;
		
		Node(int idx, int dist) {
			this.idx = idx;
			this.dist = dist;
		}
	}
	

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		V = Integer.parseInt(st.nextToken());
		E = Integer.parseInt(st.nextToken());
		
		graph = new HashMap<>();
		
		for (int i = 0; i < E; i++) {
			st = new StringTokenizer(br.readLine());
			int u = Integer.parseInt(st.nextToken());
			int v = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			
			if (graph.containsKey(u)) {
				List<int[]> value = graph.get(u);
				value.add(new int[] {v, w});
			} else {
				List<int[]> value = new ArrayList<>();
				value.add(new int[] {v, w});
				graph.put(u, value);
			}
			
			if (graph.containsKey(v)) {
				List<int[]> value = graph.get(v);
				value.add(new int[] {u, w});
			} else {
				List<int[]> value = new ArrayList<>();
				value.add(new int[] {u, w});
				graph.put(v, value);
			}
		}
		
		st = new StringTokenizer(br.readLine());
		
		int macNum = Integer.parseInt(st.nextToken());
		macLimit = Integer.parseInt(st.nextToken());
		macdonald = new HashSet<>();
		
		st = new StringTokenizer(br.readLine());
		
		for (int i = 0; i < macNum; i++) {
			macdonald.add(Integer.parseInt(st.nextToken()));
		}
		
		st = new StringTokenizer(br.readLine());
		
		int starNum = Integer.parseInt(st.nextToken());			
		starLimit = Integer.parseInt(st.nextToken());
		starbucks = new HashSet<>();
		
		st = new StringTokenizer(br.readLine());
		
		for (int i = 0; i < starNum; i++) {
			starbucks.add(Integer.parseInt(st.nextToken()));
		}
		
        // 위는 모두 입력 부분.
        ////////////////////////////////////////////////

        // mac, star : 맥세권, 스세권 범위 안에 있는 집들의
        //             거리의 값들을 각각 mac, star 배열에 저장.
        // result : 맥도날드, 스타벅스까지의 거리의 합 저장.
		int[] mac = findResult(MACDONALD);
		int[] star = findResult(STARBUCKS);			
		int result = INF;
		
        // 맥세권, 스세권 범위 안에 있는 집들을 탐색.
        // + 맥도날드, 스타벅스 거리의 합 최소값 저장.
		for (int i = 1; i <= V; i++) {
            // 맥도날드, 스타벅스인 노드는 집이 아님.
			if (macdonald.contains(i) || starbucks.contains(i)) continue;
			// 맥세권, 스세권 범위 안에 존재하지 않는다면 스킵.
            if (mac[i] == INF || star[i] == INF) continue;
			
            // 갱신 시도.
			result = Math.min(result, mac[i] + star[i]);
		}
		
        // 갱신이 한번도 안됐다 -> 맥세권, 스세권 만족하는 집이 없다.
		if (result == INF) result = -1;
		
		System.out.println(result);
	}
	
	
	static int[] findResult(int type) {
		
        // 맥세권인지, 스세권인지에 따라 다르게 탐색해야하니,
        // 입력되는 type에 따라 탐색할 Set, limit 다르게 설정.
		Set<Integer> targetSet;
		int limit;
		
		if (type == MACDONALD) {
			targetSet = macdonald;
			limit = macLimit;
		} else {
			targetSet = starbucks;
			limit = starLimit;
		}
		
        // 다익스트라 탐색을 위한, PriorityQueue, distance 배열 초기화.
		PriorityQueue<Node> pq = new PriorityQueue<>((a, b)
				-> a.dist - b.dist);
		
		int[] distance = new int[V + 1];
		Arrays.fill(distance, INF);
		
        // 핵심 - 다익스트라 반복을 줄이기 위해,
        //       한번에 맥도날드 전부 혹은 스타벅스 전부를 시작점으로
        //       다익스트라는 각각 딱 한번만 돌게끔 설정.
		for (int start : targetSet) {
			pq.add(new Node(start, 0));
			distance[start] = 0; 			
		}
		
        // 다익스트라 탐색.
		while(!pq.isEmpty()) {
			
			Node cur = pq.poll();
			int idx = cur.idx;
			int dist = cur.dist;
			List<int[]> next = graph.get(idx);

			for (int[] n : next) {
				int nd = dist + n[1];
				if (nd > limit || nd > distance[n[0]]) continue;
				
				distance[n[0]] = nd;
				pq.add(new Node(n[0], nd));
			}			
		}
		
		return distance;
	}
}
