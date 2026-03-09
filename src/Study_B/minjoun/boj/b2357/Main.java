package Study_B.minjoun.boj.b2357;

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
    static long[] maxTree;
    static long[] minTree;

    public static void main(String[] args) throws Exception{
        st = new StringTokenizer(br.readLine().trim());
        // 수의 개수
        N = Integer.parseInt(st.nextToken());
        // 수의 변경 개수
        M = Integer.parseInt(st.nextToken());

        numArr = new long[N + 1];
        for (int i = 1; i <= N; i++)
            numArr[i] = Long.parseLong(br.readLine().trim());

        int treeHeight = (int)Math.ceil(Math.log(N) / Math.log(2));
        int treeSize = (1 << (treeHeight+2));
        maxTree = new long[treeSize];
        minTree = new long[treeSize];
        init(1, 1, N, maxTree, 1);
        init(1, 1, N, minTree, 2);

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int left = Integer.parseInt(st.nextToken());
            int right = Integer.parseInt(st.nextToken());
            System.out.print(minQuery(1, 1, N, left, right));
            System.out.print(" ");
            System.out.println(maxQuery(1, 1, N, left, right));
        }
    }

    public static void init(int node, int start, int end, long[] mTree, int mod) {
        if (start == end) {
            mTree[node] = numArr[start];
        } else {
            init(node * 2 , start, (start + end) / 2, mTree, mod);
            init(node * 2 + 1 , ((start + end) / 2) + 1, end, mTree, mod);
            if (mod == 1)
                mTree[node] = Math.max(mTree[node * 2], mTree[(node * 2) + 1]);
            else if (mod == 2)
                mTree[node] = Math.min(mTree[node * 2], mTree[(node * 2) + 1]);
        }
    }

    static long maxQuery(int node, int start, int end, int left, int right) {
        if (left > end || right < start) {
            return 0;
        }
        if (left <= start && end <= right) {
            return maxTree[node];
        }

        long lValue = maxQuery(node*2, start, (start+end)/2, left, right);
        long rValue = maxQuery(node*2+1, (start+end)/2+1, end, left, right);

        return Math.max(lValue, rValue);
    }

    static long minQuery(int node, int start, int end, int left, int right) {
        if (left > end || right < start) {
            return Integer.MAX_VALUE;
        }
        if (left <= start && end <= right) {
            return minTree[node];
        }

        long lValue = minQuery(node*2, start, (start+end)/2, left, right);
        long rValue = minQuery(node*2+1, (start+end)/2+1, end, left, right);

        return Math.min(lValue, rValue);
    }
}