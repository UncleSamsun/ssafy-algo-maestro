package Study_B.minjoun.boj.b13308;

import java.io.*;
import java.util.*;

class Load {
    // 다음 도시 넘버
    int city;
    // 도로의 거리
    int dist;

    public Load(int city, int dist) {
        this.city = city;
        this.dist = dist;
    }
}
class City {
    // 도시 넘버
    int num;
    // 현재까지 쓴 비용
    long cost;
    // 최소 충전 비용인 도시 번호
    int minChargeIdx;

    public City(int num, long cost, int minChargeIdx) {
        this.num = num;
        this.cost = cost;
        this.minChargeIdx = minChargeIdx;
    }
}

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;

    // 총 도시의 개수
    static int N;
    // 도로의 개수
    static int M;
    // 각 도시 number의 충전 비용
    static int[] chargeCost;
    // 도시 그래프
    static List<Load>[] cityGraph;

    public static void main(String[] args) throws Exception{
        // 도시와 도로의 개수 입력
        st = new StringTokenizer(br.readLine().trim());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        // 도시 별 충전 비용 입력
        st = new StringTokenizer(br.readLine().trim());
        chargeCost = new int[N+1];
        for (int i = 1; i <= N; i++) chargeCost[i] = Integer.parseInt(st.nextToken());
        // 간선그래프 초기화
        cityGraph = new ArrayList[N+1];
        for (int i = 0; i <= N; i++) {
            cityGraph[i] = new ArrayList<>();
        }
        // 노드별 간선 추가
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int num1 = Integer.parseInt(st.nextToken());
            int num2 = Integer.parseInt(st.nextToken());
            int dist = Integer.parseInt(st.nextToken());
            // 양방향 간선 그래프
            cityGraph[num1].add(new Load(num2, dist));
            cityGraph[num2].add(new Load(num1, dist));
        }

        System.out.println(dijkstra(1, N));
    }

    // 비용 게산
    public static long dijkstra(int sCity, int eCity) {
        // 큐는 해당 도시의 비용을 기준으로 정렬
        PriorityQueue<City> pq = new PriorityQueue<>((c1, c2) -> Long.compare(c1.cost, c2.cost));
        // 시작 도시 추가
        pq.offer(new City(sCity, 0, sCity));
        // 거리 배열 초기화 -> 모든 값을 MAX_VALUE로
        long[][] dist = new long[N+1][N+1];
        for (int i = 0; i <= N; i++) {
            Arrays.fill(dist[i], Long.MAX_VALUE);
        }
        // 시작 도시 0으로 초기화
        dist[sCity][sCity] = 0;
        // 마지막 도시 도착했을 때 결과값 리턴용
        long result = Long.MAX_VALUE;
        // 큐 내용물 반복
        while (!pq.isEmpty()) {
            // 도시정보 가져옴
            City nowCity = pq.poll();
            int cityNum = nowCity.num;
            long cost = nowCity.cost;
            int minChargeIdx = nowCity.minChargeIdx;

            // 다익스트라 가지치기
            if (cost != dist[cityNum][minChargeIdx]) continue;
            // 만약 현재 도시가 도착도시라면 최소비용 저장 후 브레이크
            if (cityNum == eCity) {
                result = cost;
                break;
            }

            // 해당 도시에서 연결된 다음 도시 순회
            for (Load list : cityGraph[cityNum]) {
                // 다음 도시 넘버
                int nextCityNum = list.city;
                // 다음 도시로 가기위한 비용
                long nextCost = cost + (long) chargeCost[minChargeIdx] * (long) list.dist;
                // 최소비용을 가진 도시번호 비교
                int nextMinChargeIdx = minChargeIdx;
                // 만약 다음 도시의 비용이 최소비용이라면 최소비용도시넘버를 다음 도시넘버로 변경
                if (chargeCost[minChargeIdx] > chargeCost[nextCityNum]) {
                    nextMinChargeIdx = nextCityNum;
                }
                // 만약 다음 도시 비용이 갱신된다면 큐에 추가
                if (nextCost < dist[nextCityNum][nextMinChargeIdx]) {
                    dist[nextCityNum][nextMinChargeIdx] = nextCost;
                    pq.offer(new City(nextCityNum, nextCost, nextMinChargeIdx));
                }
            }
        }

        // 결과 반환
        return result;
    }
}