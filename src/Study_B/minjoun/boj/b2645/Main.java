package Study_B.minjoun.boj.b2645;

import java.io.*;
import java.util.*;

class Node implements Comparable<Node>{
    int[] loc;
    int dist;
    int dir;
    List<int[]> turnLoc;

    public Node(int[] loc, int dist, int dir, List<int[]> turnLoc) {
        this.loc = loc;
        this.dist = dist;
        this.dir = dir;
        this.turnLoc = turnLoc;
    }

    @Override
    public int compareTo(Node n) {
        return this.dist - n.dist;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(turnLoc.size());
        for (int[] num : turnLoc) sb.append(" ").append(num[0]).append(" ").append(num[1]);
        return sb.toString();
    }
}

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static int[] startLoc;
    static int[] endLoc;
    static int K;
    static int[][] map;
    static int N;
    // 상우하좌
    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, 1, 0, -1};

    static int[][] dist;


    public static void main(String[] args) throws Exception{
        // 격자판의 크기 N
        N = Integer.parseInt(br.readLine().trim());
        map = new int[N+1][N+1];
        dist = new int[N+1][N+1];
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= N; j++) {
                dist[i][j] = Integer.MAX_VALUE;
            }
        }

        // 시작좌표 / 종료좌표
        st = new StringTokenizer(br.readLine().trim());
        startLoc = new int[]{Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())};
        endLoc = new int[]{Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())};

        // 배치된 격자를 지나는데 드는 비용
        K = Integer.parseInt(br.readLine().trim());

        // 이미 배치된 회로의 개수
        int M = Integer.parseInt(br.readLine().trim());
        List<int[]>[] patterns = new ArrayList[M];
        for (int i = 0; i < M; i++) {
            patterns[i] = new ArrayList<>();
            st = new StringTokenizer(br.readLine().trim());
            int num = Integer.parseInt(st.nextToken());
            for (int j = 0; j < num; j++) {
                patterns[i].add(new int[]{Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())});
            }
        }

        // 배치된 회로를 맵에 1로 표시
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < patterns[i].size() - 1; j++) {
                int[] loc1 = {patterns[i].get(j)[0], patterns[i].get(j)[1]};
                int[] loc2 = {patterns[i].get(j+1)[0], patterns[i].get(j+1)[1]};
                map[loc1[0]][loc1[1]] = 1;
                int locDist = getDist(loc1, loc2);
                for (int x = 0; x < locDist; x++) {
                    if (loc1[0] - loc2[0] < 0) map[++loc1[0]][loc1[1]] = 1;
                    else if (loc1[0] - loc2[0] > 0) map[--loc1[0]][loc1[1]] = 1;
                    else if (loc1[1] - loc2[1] < 0) map[loc1[0]][++loc1[1]] = 1;
                    else if (loc1[1] - loc2[1] > 0) map[loc1[0]][--loc1[1]] = 1;
                }
            }
        }
        // 입력 끝-----------------------------------
        Node result = dijkstra();

        System.out.println(dist[endLoc[0]][endLoc[1]]);
        System.out.println(result.toString());
    }

    public static Node dijkstra() {
        PriorityQueue<Node> pq = new PriorityQueue<>((n1, n2) -> n1.dist - n2.dist);
        List<int[]> tmpLoc = new ArrayList<>();
        tmpLoc.add(startLoc);
        pq.offer((new Node(startLoc, 1, -1, tmpLoc)));
        dist[startLoc[0]][startLoc[1]] = 1;

        Node result = new Node(new int[]{endLoc[0], endLoc[1]}, Integer.MAX_VALUE, -1, new ArrayList<>());

        while (!pq.isEmpty()) {
            Node node = pq.poll();

            if (node.loc[0] == endLoc[0] && node.loc[1] == endLoc[1]) {
                if (node.dist < result.dist) {
                    node.turnLoc.add(endLoc);
                    result = node;
                }
            }
            if (node.dist != dist[node.loc[0]][node.loc[1]]) continue;

            for (int i = 0; i < 4; i ++) {
                // 다음 좌표
                int nr = node.loc[0] + dr[i];
                int nc = node.loc[1] + dc[i];
                // 다음 좌표 유효성 검사
                if (!validation(nr, nc)) continue;
                // 방향 꺾이는지 체크
                // 만약 기존 방향이 없었다면 현재 방향으로 설정
                int dir = node.dir == -1 ? i : node.dir;
                // 만약 방향이 꺾였다면 꺾인 좌표 추가
                List<int[]> turnLoc = new ArrayList<>(node.turnLoc);
                if (dir != i) turnLoc.add(new int[]{node.loc[0], node.loc[1]});
                // 거리 추가
                int tmpDist = 1;
                if (map[nr][nc] == 1) tmpDist = K;
                int nextDist = tmpDist + node.dist;
                if (nextDist < dist[nr][nc]) {
                    dist[nr][nc] = nextDist;
                    pq.offer(new Node(new int[]{nr, nc}, nextDist, i, turnLoc));
                }
            }
        }
        return result;
    }

    // 좌표간의 거리를 구하는 메서드
    public static int getDist(int[] loc1, int[] loc2) {
        return Math.abs(loc1[0] - loc2[0]) + Math.abs(loc1[1] - loc2[1]);
    }

    public static boolean validation(int x, int y) {
        return x > 0 && x <= N && y > 0 && y <= N;
    }
}