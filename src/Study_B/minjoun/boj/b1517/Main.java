package Study_B.minjoun.boj.b1517;

import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[] arr;
    static int[] sorted;
    static long[] tree;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        arr = new int[N+1];
        sorted = new int[N+1];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
            sorted[i] = arr[i];
        }

        Arrays.sort(sorted);

        // 좌표압축
        Map<Integer, Integer> rankMap = new HashMap<>();
        int rank = 0;
        for (int i = 1; i <= N; i++) {
            if (!rankMap.containsKey(sorted[i])) {
                rankMap.put(sorted[i], ++rank);
            }
        }

        tree = new long[4 * N];

        long answer = 0;

        for (int i = 1; i <= N; i++) {
            int curRank = rankMap.get(arr[i]);

            // 현재 값보다 큰 값의 개수
            if (curRank < rank) {
                answer += query(1, 1, rank, curRank + 1, rank);
            }

            // 현재 값 등장 처리
            update(1, 1, rank, curRank, 1);
        }

        System.out.println(answer);
    }

    static void update(int node, int start, int end, int idx, int diff) {
        if (idx < start || idx > end) return;

        tree[node] += diff;

        if (start == end) return;

        int mid = (start + end) / 2;
        update(node * 2, start, mid, idx, diff);
        update(node * 2 + 1, mid + 1, end, idx, diff);
    }

    static long query(int node, int start, int end, int left, int right) {
        if (right < start || end < left) return 0;
        if (left <= start && end <= right) return tree[node];

        long lValue = query(node * 2, start, (start + end) / 2, left, right);
        long rValue = query(node * 2 + 1, (start + end) / 2 + 1, end, left, right);
        return lValue + rValue;
    }
}