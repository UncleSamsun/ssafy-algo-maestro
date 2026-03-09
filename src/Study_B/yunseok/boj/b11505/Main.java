import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// 11505번, 구간곱 구하기

public class Main {
    
    static final int CHANGE = 1;
    static final long MOD = 1_000_000_007L;

    static int N, M, K;
    static int[] values;
    static long[] segTree;

    // 1 <= N <= 1_000_000
    // 1 <= M <= 10_000
    // 1 <= K <= 10_000
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        values = new int[N + 1];
        segTree = new long[N * 4];

        for (int i = 1; i <= N; i++) values[i] = Integer.parseInt(br.readLine());

        setTree(1, N, 1);

        for (int i = 0; i < M + K; i++) {
            st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());

            if (type == CHANGE) {
                int target = Integer.parseInt(st.nextToken());
                int value = Integer.parseInt(st.nextToken());

                if (values[target] == 0) {
                    rebuildTree(1, N, target, value, 1);
                } else {
                    updateTree(1, N, target, value, 1);
                }

                values[target] = value;
            } else {
                int left = Integer.parseInt(st.nextToken());
                int right = Integer.parseInt(st.nextToken());

                long result = findResult(1, N, left, right, 1);
                System.out.println(result);
            }

            // System.out.println(" - values : " + Arrays.toString(values));
            // System.out.println(" -- segTree : " + Arrays.toString(segTree));
        }
    }

    static void setTree(int start, int end, int idx) {

        if (start == end) {
            segTree[idx] = values[start];
            return;
        }

        int mid = (start + end) / 2;

        setTree(start, mid, idx * 2);
        setTree(mid + 1, end, idx * 2 + 1);

        segTree[idx] = (segTree[idx * 2] * segTree[idx * 2 + 1]) % MOD;
    }

    static void rebuildTree(int start, int end, int target, int value, int idx) {

        if (start > target || end < target) return;
        if (start == end) {
            segTree[idx] = value;
            return;
        }

        int mid = (start + end) / 2;

        rebuildTree(start, mid, target, value, idx * 2);
        rebuildTree(mid + 1, end, target, value, idx * 2 + 1);

        segTree[idx] = (segTree[idx * 2] * segTree[idx * 2 + 1]) % MOD;
    }

    static long updateTree(int start, int end, int target, int value, int idx) {

        if (target < start || target > end) return segTree[idx];
        if (start == end && start == target) {
            segTree[idx] = value;
            return segTree[idx];
        }

        int mid = (start + end) / 2;
        long leftRes = updateTree(start, mid, target, value, idx * 2);
        long rightRes = updateTree(mid + 1, end, target, value, idx * 2 + 1);

        segTree[idx] = (leftRes * rightRes) % MOD;

        return segTree[idx];
    }

    static long findResult(int start, int end, int left, int right, int idx) {

        if (start > right || end < left) return 1;
        if (left <= start && right >= end) return segTree[idx];

        int mid = (start + end) / 2;

        long result = findResult(start, mid, left, right, idx * 2);
        result *= findResult(mid + 1, end, left, right, idx * 2 + 1);
        result %= MOD;

        return result;
    }

}
