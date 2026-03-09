import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// 2357번, 최솟값과 최댓값

public class Main {
    
    static final long INF = 1_000_000_001L, MINUS_INF = -1;
    static int N, M;
    static long[] values, minValue, maxValue;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        values = new long[N + 1];
        minValue = new long[N * 4];
        maxValue = new long[N * 4];

        for (int i = 1; i <= N; i++) values[i] = Long.parseLong(br.readLine());

        setMinTree(1, N, 1);
        setMaxTree(1, N, 1);

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            
            int left = Integer.parseInt(st.nextToken());
            int right = Integer.parseInt(st.nextToken());

            long min = findMinResult(1, N, left, right, 1);
            long max = findMaxResult(1, N, left, right, 1);

            System.out.println(min + " " + max);
        }
    }

    static void setMinTree(int start, int end, int idx) {

        if (start == end) {
            minValue[idx] = values[start];
            return;
        }

        int mid = (start + end) / 2;

        setMinTree(start, mid, idx * 2);
        setMinTree(mid + 1, end, idx * 2 + 1);

        minValue[idx] = Math.min(minValue[idx * 2], minValue[idx * 2 + 1]);
    }

    static void setMaxTree(int start, int end, int idx) {

        if (start == end) {
            maxValue[idx] = values[start];
            return;
        }

        int mid = (start + end) / 2;

        setMaxTree(start, mid, idx * 2);
        setMaxTree(mid + 1, end, idx * 2 + 1);

        maxValue[idx] = Math.max(maxValue[idx * 2], maxValue[idx * 2 + 1]);
    }

    static long findMinResult(int start, int end, int left, int right, int idx) {
    
        if (start > right || end < left) return INF;
        if (left <= start && right >= end) return minValue[idx];

        int mid = (start + end) / 2;

        long leftRes = findMinResult(start, mid, left, right, idx * 2);
        long rightRes = findMinResult(mid + 1, end, left, right, idx * 2 + 1);

        return Math.min(leftRes, rightRes);
    }

    static long findMaxResult(int start, int end, int left, int right, int idx) {
    
        if (start > right || end < left) return MINUS_INF;
        if (left <= start && right >= end) return maxValue[idx];

        int mid = (start + end) / 2;

        long leftRes = findMaxResult(start, mid, left, right, idx * 2);
        long rightRes = findMaxResult(mid + 1, end, left, right, idx * 2 + 1);

        return Math.max(leftRes, rightRes);
    }
}
