package practice;

import java.io.*;
import java.util.*;

public class exe8 {
	static int n;
	static int[][] map;
	static int[] dx = { -1, 1, 0, 0 };
	static int[] dy = { 0, 0, -1, 1 };

	// 꺾인 지점을 저장하기 위해서 road 라는 list를 선언한다
	// 출발지점과 방향이 달라졌을 때 list에 좌표값을 넣는다. 
	// 해당 코드는 N이 50 이하라서 가능한 코드이다.
	public static class Node implements Comparable<Node> {
		int x, y, d, dir; 
		ArrayList<int[]> road; 
		Node(int x, int y, int d,int dir,  ArrayList<int[]> preRoad) {
			this.x = x;
			this.y = y;
			this.d = d;
			this.dir = dir; 
			this.road = new ArrayList<>(preRoad); 
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
		StringBuilder sb= new StringBuilder(); 
		sb.append(result.d).append("\n"); 
		
		
		sb.append(result.road.size()).append(" ");
		for(int[] p : result.road) {
			sb.append(p[0]+ " " +p[1]).append(" ");
		}
		System.out.println(sb);

	}

	static Node func(int startX , int startY, int endX, int endY) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		
	
		int[][] dist = new int[n+1][n+1];
		for(int i=1 ; i<=n ; i++) {
			Arrays.fill(dist[i],Integer.MAX_VALUE);
		}
		
		dist[startX][startY] = 1; 
		ArrayList<int[]> startRoad = new ArrayList<>();
		startRoad.add(new int[] {startX, startY}); 
		pq.offer(new Node(startX, startY , dist[startX][startY], -1, startRoad)); 
		
		
		while(!pq.isEmpty()) {
			Node cur = pq.poll(); 
			
			int curX = cur.x; 
			int curY = cur.y; 
			int curCost = cur.d; 
			int curDir = cur.dir; 
			
			if( curX == endX && curY == endY) {
				cur.road.add(new int[] {endX, endY}); 
				return cur; 
			}
			if(curCost > dist[curX][curY]) continue; 
			
			for(int i=0 ; i< 4 ; i++) {
				int nextX = curX + dx[i];
				int nextY = curY + dy[i]; 
				
				
				if(nextX>=1 && nextX<=n && nextY >=1 && nextY <= n ) {
					
					if(dist[nextX][nextY] > map[nextX][nextY] + curCost) {
						dist[nextX][nextY] =  map[nextX][nextY] + curCost; 
					
						
						ArrayList<int[]> midRoad = new ArrayList<>(cur.road);
						
						if(curDir != -1 && curDir != i) {
							midRoad.add(new int[] {curX, curY}); 
						}
						
						
						 pq.offer(new Node(nextX, nextY, dist[nextX][nextY],i, midRoad));
						
					}
				}
				
			}
			
			
		}
		return null; 
	}

}
