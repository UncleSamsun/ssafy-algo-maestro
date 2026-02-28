package practice;

import java.io.*;
import java.util.*;

// 역추적 알고리즘을 사용해보자 
// 방향이 바뀌면 부모의 위치를 저장 
public class exe9 {
	static int n;
	static int[][] map;
	static int[] dx = { -1, 1, 0, 0 };
	static int[] dy = { 0, 0, -1, 1 };
	static int[][][] parent; // 부모의 위치를 저장하기 위한 배열

	public static class Node implements Comparable<Node> {
		int x, y, d, dir;
		ArrayList<int[]> road;

		Node(int x, int y, int d, int dir) {
			this.x = x;
			this.y = y;
			this.d = d;
			this.dir = dir;

		}

		@Override
		public int compareTo(Node o) {
			return Integer.compare(this.d, o.d);
		}
	}

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		n = Integer.parseInt(br.readLine());

		map = new int[n + 1][n + 1];
		for (int i = 1; i <= n; i++) {
			Arrays.fill(map[i], 1);
		}

		// 새롭게 배치할 회로의 시작격자와 마지막 격자 받기
		StringTokenizer st = new StringTokenizer(br.readLine());

		int x1 = Integer.parseInt(st.nextToken());
		int y1 = Integer.parseInt(st.nextToken());

		int x2 = Integer.parseInt(st.nextToken());
		int y2 = Integer.parseInt(st.nextToken());

		map[x1][y1] = 0;

		int cost = Integer.parseInt(br.readLine());
		int m = Integer.parseInt(br.readLine());

		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			int k = Integer.parseInt(st.nextToken());

			int r1 = Integer.parseInt(st.nextToken());
			int c1 = Integer.parseInt(st.nextToken());

			for (int j = 1; j < k; j++) {
				int r2 = Integer.parseInt(st.nextToken());
				int c2 = Integer.parseInt(st.nextToken());

				int minR = Math.min(r1, r2);
				int maxR = Math.max(r1, r2);
				int minC = Math.min(c1, c2);
				int maxC = Math.max(c1, c2);

				// 채우기
				for (int a = minR; a <= maxR; a++) {
					for (int b = minC; b <= maxC; b++) {
						map[a][b] = cost;
					}
				}
				r1 = r2;
				c1 = c2;
			}
		} // 입력 끝

		// 다익스트라 함수 호출 구간

		Node result = func(x1, y1, x2, y2);
		System.out.println(result.d);
		findPath(x2, y2, x1, y1);

	}

	static Node func(int startX, int startY, int endX, int endY) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		parent = new int[n + 1][n + 1][2];

		int[][] dist = new int[n + 1][n + 1];
		for (int i = 1; i <= n; i++) {
			Arrays.fill(dist[i], Integer.MAX_VALUE);
		}

		dist[startX][startY] = 1;

		pq.offer(new Node(startX, startY, dist[startX][startY], -1));

		while (!pq.isEmpty()) {
			Node cur = pq.poll();

			int curX = cur.x;
			int curY = cur.y;
			int curCost = cur.d;

			if (curX == endX && curY == endY) {

				return cur;
			}
			if (curCost > dist[curX][curY])
				continue;

			for (int i = 0; i < 4; i++) {
				int nextX = curX + dx[i];
				int nextY = curY + dy[i];

				if (nextX >= 1 && nextX <= n && nextY >= 1 && nextY <= n) {

					if (dist[nextX][nextY] > map[nextX][nextY] + curCost) {
						dist[nextX][nextY] = map[nextX][nextY] + curCost;

						// 부모 위치 저장
						parent[nextX][nextY][0] = curX;
						parent[nextX][nextY][1] = curY;

						pq.offer(new Node(nextX, nextY, dist[nextX][nextY], i));

					}
				}

			}

		}
		return null;
	}

	static void findPath(int endX, int endY, int startX, int startY) {
		List<int[]> path = new ArrayList<>();
		
		
		int curX = endX;
		int curY = endY;

		while (curX != 0) {
			path.add(new int[] { curX, curY });
			int preX = parent[curX][curY][0];
			int preY = parent[curX][curY][1];

			curX = preX;
			curY = preY;
		}
		Collections.reverse(path);

		StringBuilder sb = new StringBuilder(); 
		List<int[]> corners = new ArrayList<>(); 
		
		if(!path.isEmpty()) {
			corners.add(path.get(0)); 
			
		}
		for(int i=1 ; i< path.size() -1 ; i++) {
			int[] prev = path.get(i-1); 
			int[] curr = path.get(i); 
			int[] next = path.get(i+1); 
			
			boolean isSame = false; 
			if(prev[0] == curr[0] && curr[0]== next[0] || prev[1] == curr[1] && curr[1]== next[1]) {
				//같은 라인 
				isSame = true; 
			}
			if(!isSame) {
				corners.add(curr); 
			}
		}
		corners.add(path.get(path.size()-1)); 
		sb.append(corners.size()).append(" ");
		for(int[] p : corners) {
			sb.append(p[0]).append(" ").append(p[1]).append(" "); 
			
		}
		System.out.println(sb);
	}

}
