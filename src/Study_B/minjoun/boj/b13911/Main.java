package Study_B.minjoun.boj.b13911;

import java.io.*;
import java.util.*;

class node {
    int num;
    int dist;

    public node(int num, int dist) {
        this.num = num;
        this.dist = dist;
    }
}

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;

    public static void main(String[] args) throws Exception{
        // 정점의 개수 V, 도로의 개수 E
        st = new StringTokenizer(br.readLine().trim());
        int V = Integer.parseInt(st.nextToken());
        int E = Integer.parseInt(st.nextToken());
        // 그래프 초기화
        List<node>[] graph = new ArrayList[V+1];
        for (int i = 0; i <= V; i++) {
            graph[i] = new ArrayList<>();
        }
        // 그래프 정보 입력
        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());

            // 양방향 그래프
            graph[u].add(new node(v, w));
            graph[v].add(new node(u, w));
        }
        // 맥도날드 정보 입력
        // 맥도날드의 수 M, 맥세권 조건 X
        st = new StringTokenizer(br.readLine().trim());
        int M = Integer.parseInt(st.nextToken());
        int X = Integer.parseInt(st.nextToken());
        // 맥도날드 정점 리스트 저장
        st = new StringTokenizer(br.readLine().trim());
        List<Integer> mcList = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            mcList.add(Integer.parseInt(st.nextToken()));
        }
        // 맥도날드 정점에 대한 다른 노드의 거리 배열 초기화
        int[] mcDist = new int[V+1];
        for (int i = 0; i <= V; i++) {
            mcDist[i] = Integer.MAX_VALUE;
        }
        //  스타벅스 정보 입력
        // 스타벅스의 수 S, 스세권 조건 Y
        st = new StringTokenizer(br.readLine().trim());
        int S = Integer.parseInt(st.nextToken());
        int Y = Integer.parseInt(st.nextToken());
        // 스타벅스 정점 리스트 저장
        st = new StringTokenizer(br.readLine().trim());
        List<Integer> sbList = new ArrayList<>();
        for (int i = 0; i < S; i++) {
            sbList.add(Integer.parseInt(st.nextToken()));
        }
        // 스타벅스 정점에 대한 다른 노드의 거리 배열 초기화
        int[] sbDist = new int[V+1];
        for (int i = 0; i <= V; i++) {
            sbDist[i] = Integer.MAX_VALUE;
        }
        // -------------------------------------입력 끝---------------------------------------------
        // 다익스트라 용 우선순위 큐
        PriorityQueue<node> pq = new PriorityQueue<>((n1, n2) -> n1.dist - n2.dist);
        // 맥세권 다익스트라
        for (Integer value : mcList) {
            // 맥도날드 정점 입력
            pq.offer(new node(value, 0));
            mcDist[value] = 0;
        }
        // 우선순위 큐 순회
        while (!pq.isEmpty()) {
            int nodeNum = pq.peek().num;
            int nodeDist = pq.peek().dist;
            pq.poll();

            if (mcDist[nodeNum] != nodeDist) continue;

            for (int j = 0; j < graph[nodeNum].size(); j++) {
                node nextNode = graph[nodeNum].get(j);
                int nextDist = nextNode.dist + nodeDist;
                if (mcDist[nextNode.num] > nextDist) {
                    mcDist[nextNode.num] = nextDist;
                    pq.offer(new node(nextNode.num, nextDist));
                }
            }
        }

        // 스타벅스 다익스트라
        for (Integer integer : sbList) {
            // 스타벅스 정점 입력
            pq.offer(new node(integer, 0));
            sbDist[integer] = 0;
        }
        // 우선순위 큐 순회
        while (!pq.isEmpty()) {
            int nodeNum = pq.peek().num;
            int nodeDist = pq.peek().dist;
            pq.poll();

            if (sbDist[nodeNum] != nodeDist) continue;

            for (int j = 0; j < graph[nodeNum].size(); j++) {
                node nextNode = graph[nodeNum].get(j);
                int nextDist = nextNode.dist + nodeDist;
                if (sbDist[nextNode.num] > nextDist) {
                    sbDist[nextNode.num] = nextDist;
                    pq.offer(new node(nextNode.num, nextDist));
                }
            }
        }

        int homeDist = Integer.MAX_VALUE;
        for (int i = 0; i < sbDist.length; i++) {
            // 둘 중의 하나가 맥도날드나 스타벅스면 스킵
            if (mcDist[i] == 0 || sbDist[i] == 0) continue;
            // 둘의 값이 기준치 이하면 더하고 기존값과 비교
            if (mcDist[i] <= X && sbDist[i] <= Y) {
                homeDist = Math.min(homeDist, mcDist[i] + sbDist[i]);
            }
        }

        System.out.println(homeDist == Integer.MAX_VALUE ? -1 : homeDist);
    }
}