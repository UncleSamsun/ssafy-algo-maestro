import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

// 11시 12분 ~ 2시 50분

public class Main {

    // 상수.
	static int INF = Integer.MAX_VALUE;
	
	static int[] dx = {1, -1, 0, 0};
	static int[] dy = {0, 0, 1, -1};
	
    // 각종 변수.
	static int N, K, M;
	static int[] start, end;
	static int[][] board;
	static int result;
	static String resultPath;

    // PriorityQueue에서 사용할 Node.
	static class Node {
		
        // x, y : 위치
        // value : 시작지점부터 x, y 까지 도달한데 걸린 비용.
        // vector : 현재 진행중인 방향.
		int x;
		int y;
		int value;
		int vector;
		
		Node(int x, int y, int value, int vector) {
			this.x = x;
			this.y = y;
			this.value = value;
			this.vector = vector;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine());
		start = new int[2];
		end = new int[2];
		board = new int[N + 1][N + 1];
		for (int i = 1; i <= N; i++) {
			Arrays.fill(board[i], 1);
		}
		
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		for (int i = 0; i < 2; i++) {
			start[i] = Integer.parseInt(st.nextToken());
		}
		for (int i = 0; i < 2; i++) {
			end[i] = Integer.parseInt(st.nextToken());
		}
		
		K = Integer.parseInt(br.readLine());
		M = Integer.parseInt(br.readLine());
		
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			
			for (int j = 1; j < num; j++) {
				int nx = Integer.parseInt(st.nextToken());
				int ny = Integer.parseInt(st.nextToken());
				
				boardSet(x, y, nx, ny);
				
				x = nx;
				y = ny;
			}
		}
		
        // 위는 입력 부분.
        ////////////////////////////////////////////////////////////

        // size : 최단 경로의 총 꺾은 횟수.
		int size = findResult();
		
        // result : 최단 경로의 총 비용.
        // resultPath : 최단 경로의 경로.
		System.out.println(result);
		System.out.println(size + " " + resultPath);
	}
	
    // x ~ nx, y ~ ny 까지 board에 K로 채워주는 함수.
	static void boardSet(int x, int y, int nx, int ny) {
		
        // y ~ ny 까지 x가 고정된 선을 K로 채워주는 함수.
        if (x == nx) setLineY(Math.min(y, ny), Math.max(y, ny), x);

        // x ~ nx 까지 y가 고정된 선을 K로 채워주는 함수.
		else setLineX(Math.min(x, nx), Math.max(x, nx), y);
	}
	
	static void setLineY(int start, int end, int x) {
		for (int i = start; i <= end; i++) board[x][i] = K;
	}
	
	static void setLineX(int start, int end, int y) {
		for (int i = start; i <= end; i++) board[i][y] = K;	
	}
	
    // 반환값 : 최단 경로의 총 꺾은 횟수.
    // result, resultPath : 전역으로 관리하는 최단 경로의 총 비용, 최단 경로의 경로 업데이트.
	static int findResult() {
		
        // 다익스트라 탐색 가중치 기록.
		int[][] distance = new int[N + 1][N + 1];
		
		for (int i = 1; i <= N; i++) Arrays.fill(distance[i], INF);
		distance[start[0]][start[1]] = 1;
		
        // 다익스트라 탐색용 PriorityQueue.
		PriorityQueue<Node> pq = new PriorityQueue<>(
            (a, b) -> a.value - b.value);		
        pq.add(new Node(start[0], start[1], 1, -1));
            
        // 해당 위치에서 꺾인 위치 기록.
        int[][][] rotate = new int[N + 1][N + 1][2];
        rotate[start[0]][start[1]][0] = start[0];
        rotate[start[0]][start[1]][1] = start[1];
	
        // 다익스트라 탐색 시작.
		while(!pq.isEmpty()) {
			Node cur = pq.poll();
			
            // 도착한 경우 - 종료
			if (cur.x == end[0] && cur.y == end[1]) break;
			
            // 사방 탐색
			for (int i = 0; i< 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				boolean isChange = cur.vector != i;
				
				if (!validate(nx, ny)) continue;
				int ndist = cur.value + board[nx][ny];
				
				if (ndist >= distance[nx][ny]) continue;
				distance[nx][ny] = ndist;
				
                // 만약 진행 방향이 바뀐다? = 꺾인 위치
				if (isChange) {
					pq.add(new Node(nx, ny, ndist, i));
					rotate[nx][ny][0] = cur.x;
					rotate[nx][ny][1] = cur.y;
				}
				// 꺾이지 않았다면,
				else {
					pq.add(new Node(nx, ny, ndist, i));
					rotate[nx][ny][0] = rotate[cur.x][cur.y][0];
					rotate[nx][ny][1] = rotate[cur.x][cur.y][1];
				}
			}
		}
		
        // 혹시나 rotate 배열 구성이 궁금하면 보셈.

		// System.out.print("  ");
		// for (int i = 1; i <= N; i++) {
		// 	System.out.print("   " + i + "   ");
		// }
		// System.out.println();
		// for (int i = 1; i <= N; i++) {
		// 	System.out.print(i + " ");
		// 	for (int j = 1; j <= N; j++) {
		// 		System.out.print(" " + Arrays.toString(rotate[i][j]));
		// 	}
		// 	System.out.println();
		// }


        // 최단 경로 가중치 갱신.
		result = distance[end[0]][end[1]];

        // 최단 경로의 꺾인 위치 문자열 만드는 부분.
        // end 부터 꺾인 위치를 역추적해가며 스택에 저장.
		StringBuilder sb = new StringBuilder();
		int x = end[0];
		int y = end[1];
		Deque<Integer> stack = new ArrayDeque<>();
		
		while (x != start[0] || y != start[1]) {
			stack.add(y);
			stack.add(x);
			
			int nx = rotate[x][y][0];
			int ny = rotate[x][y][1];
			x = nx;
			y = ny;
		}
		
        // 총 꺾인 횟수 저장
		int size = stack.size() / 2 + 1;
		sb.append(start[0] + " " + start[1]);
		
        // 꺽인 위치들 문자열로 만들기.
		while (!stack.isEmpty()) {
			sb.append(" " + stack.pollLast());
		}
		resultPath = sb.toString();
		
		return size;
	}
	
	static boolean validate(int x, int y) {
		return x > 0 && x <= N && y > 0 && y <= N;
	}
}
