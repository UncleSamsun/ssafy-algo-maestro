import java.util.*;

public class Main {
	private static class Node implements Comparable<Node>{
		int v;
		int cost;
		
		Node(int v, int cost) {
			this.v = v;
			this.cost = cost;
		}

		@Override
		public int compareTo(Node o) {
			return this.cost - o.cost;
		}
	}
	
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		// 그래프 입력 받기
		int v = sc.nextInt();
		int e = sc.nextInt();
		
		List<Node>[] graph= new ArrayList[v+1];
		for(int i=1; i<graph.length; i++) {
			graph[i] = new ArrayList<Node>();
		}
		
		for(int i=0; i<e; i++) {
			int v1 = sc.nextInt();
			int v2= sc.nextInt();
			int cost = sc.nextInt();
			
			graph[v1].add(new Node(v2, cost));
			graph[v2].add(new Node(v1, cost));
		}
		
		// 맥세권 찾기
		int m = sc.nextInt();
		int x = sc.nextInt();
		
		int[] mPlaces = new int[m]; 
		
		for(int i=0; i<m; i++) {
			mPlaces[i]=sc.nextInt();
		}
		List<Node> macSpots = findSpots(graph, mPlaces, x);
		
		// 스세권 찾기
		int s = sc.nextInt();
		int y = sc.nextInt();
		
		int[] sPlaces = new int[s];
		
		for(int i=0; i<s; i++) {
			sPlaces[i] = sc.nextInt();
		}
		List<Node> sbugSpots = findSpots(graph, sPlaces, y);
		
		// 정답 도출
		int answer = getAnswer(macSpots, sbugSpots);
		System.out.println(answer);
	}
	
	
	private static List<Node> findSpots(List<Node>[] graph, int[] places, int maxCost) {
		int n = graph.length;
		int[] dist = new int[n];
		Arrays.fill(dist, Integer.MAX_VALUE);
		PriorityQueue<Node> pq = new PriorityQueue<>();
		for(int spot : places) {
			dist[spot] = 0;
			pq.offer(new Node(spot, 0));
		}
		
		while(!pq.isEmpty()) {
			Node cur = pq.poll();
			if(cur.cost > dist[cur.v]) continue;
			
			for(Node next : graph[cur.v]) {
				int newCost = cur.cost + next.cost;
				if(newCost > maxCost) continue;
				
				if(newCost < dist[next.v]) {
					dist[next.v] = newCost;
					pq.offer(new Node(next.v, newCost));
				}
			}
		}
		
		List<Node> spots = new ArrayList<>();
		
		for(int i=1; i<n; i++) {
			if(dist[i]!=Integer.MAX_VALUE && dist[i]!=0) {
				spots.add(new Node(i, dist[i]));
			}
		}
		
		return spots;
	}
	
	private static int getAnswer(List<Node> macSpots, List<Node> sbugSpots) {
		int answer = Integer.MAX_VALUE;

		Map<Integer, Integer> macMap = new HashMap<>();
		for(Node macPlace : macSpots) {
			macMap.put(macPlace.v, macPlace.cost);
		}
		
		for(Node sbugPlace : sbugSpots) {
			if(macMap.containsKey(sbugPlace.v)) {
				int totalCost = macMap.get(sbugPlace.v) + sbugPlace.cost;
				answer = Math.min(answer, totalCost);
			}
		}
		return answer!=Integer.MAX_VALUE ? answer : -1;
	}
}