package Study_B.minjoun.boj.b2042;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;

    static int N;
    static int M;
    static int K;

    static long[] numArr;
    static long[] tree;

    public static void main(String[] args) throws Exception{
        st = new StringTokenizer(br.readLine().trim());
        // 수의 개수
        N = Integer.parseInt(st.nextToken());
        // 수의 변경 개수
        M = Integer.parseInt(st.nextToken());
        // 구간합을 구하는 횟수
        K = Integer.parseInt(st.nextToken());

        numArr = new long[N + 1];
        for (int i = 1; i <= N; i++)
            numArr[i] = Long.parseLong(br.readLine().trim());

        int treeHeight = (int)Math.ceil(Math.log(N) / Math.log(2));
        int treeSize = (1 << (treeHeight+2));
        tree = new long[treeSize];
        init(1, 1, N);

        for (int i = 0; i < M+K; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());
            if (cmd == 1) {
                int idx = Integer.parseInt(st.nextToken());
                long value = Long.parseLong(st.nextToken());
                update(1, 1, N, idx, value);
            }
            else if (cmd == 2) {
                int left = Integer.parseInt(st.nextToken());
                int right = Integer.parseInt(st.nextToken());
                System.out.println(query(1, 1, N, left, right));
            }
        }
    }

    public static void init(int node, int start, int end) {
        if (start == end) {
            tree[node] = numArr[start];
        } else {
            init(node * 2 , start, (start + end) / 2);
            init(node * 2 + 1 , ((start + end) / 2) + 1, end);
            tree[node] = tree[node * 2] + tree[(node * 2) + 1];
        }
    }

    public static void update(int node, int start, int end, int idx, long value) {
        if (idx < start || idx > end) return;
        if (start == end) {
            numArr[idx] = value;
            tree[node] = value;
            return;
        }

        update(node * 2, start, (start + end) / 2, idx, value);
        update((node * 2) + 1, ((start + end) / 2) + 1, end, idx, value);
        tree[node] = tree[node * 2] + tree[(node * 2) + 1];
    }

    static long query(int node, int start, int end, int left, int right) {
        if (left > end || right < start) {
            return 0;
        }
        if (left <= start && end <= right) {
            return tree[node];
        }

        long lValue = query(node*2, start, (start+end)/2, left, right);
        long rValue = query(node*2+1, (start+end)/2+1, end, left, right);

        return lValue + rValue;
    }
}