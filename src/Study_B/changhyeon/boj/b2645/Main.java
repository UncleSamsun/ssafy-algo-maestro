package Study_B.changhyeon.boj.b2645;

// 배운점 - 멀티소스다익스트라
import java.io.*;
import java.util.*;

public class Main {
    static List<List<int[]>> graph;
    static final int INF = Integer.MAX_VALUE; // Infinity

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int V = Integer.parseInt(st.nextToken());
        int E = Integer.parseInt(st.nextToken());

        // init
        graph = new ArrayList<>();
        int[] macDist = new int[V+1];
        int[] starDist = new int[V+1];
        boolean[] isMac = new boolean[V+1];
        boolean[] isStar = new boolean[V+1];
        for (int i = 0; i <= V; i++) {
            graph.add(new ArrayList<>());
            macDist[i] = INF;
            starDist[i] = INF;
        }

        // build
        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());

            graph.get(u).add(new int[] {v, w});
            graph.get(v).add(new int[] {u, w});
        }

        //McDonald's
        st = new StringTokenizer(br.readLine());
        // 맥도날드 매장 수
        int aorehskfem = Integer.parseInt(st.nextToken());
        // 맥세권 판별 상수
        int aortprnjs = Integer.parseInt(st.nextToken());
        int[] mac = new int[aorehskfem];

        // 맥도날드 매장 기록
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < aorehskfem; i++) {
            mac[i] = Integer.parseInt(st.nextToken());
            isMac[mac[i]] = true;
        }
        macDist = dijkstra(mac, new int[V+1]);

        // Starbucks
        st = new StringTokenizer(br.readLine());
        // 스타벅스 매장 수
        int tmxkqjrtm = Integer.parseInt(st.nextToken());
        // 스세권 판별 상수
        int tmtprnjs = Integer.parseInt(st.nextToken());
        int[] star = new int[tmxkqjrtm];

        // 스타벅스 매장 기록
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < tmxkqjrtm; i++) {
            star[i] = Integer.parseInt(st.nextToken());
            isStar[star[i]] = true;
        }
        starDist = dijkstra(star, new int[V+1]);

        // 다 끝나고 합이 min인 곳 찾기
        int min = INF;
        for (int i = 1; i <= V; i++) {
            // 맥세권, 스세권 벗어나면 pass
            if (macDist[i] > aortprnjs || starDist[i] > tmtprnjs) continue;
            // 집이 아닌 매장이라면 pass
            if (isMac[i] || isStar[i]) continue;
            int sum = macDist[i] + starDist[i];
            min = Math.min(min, sum);
        }

        System.out.println((min == INF) ? -1 : min);
    }


    static int[] dijkstra(int[] sources, int[] dist) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        Arrays.fill(dist, INF);

        // 여기 가능한 곳들 전부 dist 0으로 해서 pq에 다 넣기
        for (int s : sources) {
            dist[s] = 0;
            pq.add(new int[] {s, 0});
        }

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int now = cur[0];
            int cost = cur[1];

            if (cost > dist[now]) continue;
            for (int[] edge : graph.get(now)) {
                int next = edge[0];
                int weight = edge[1];

                int newCost = dist[now] + weight;
                if (newCost < dist[next]) {
                    dist[next] = newCost;
                    pq.add(new int[] {next, newCost});
                }
            }
        }

        return dist;
    }
}