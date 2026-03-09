import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// 2042번, 구간합 구하기

public class Main {
    
    static final int CHANGE = 1;

    static int N, M, K;
    static long[] values;
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

        values = new long[N + 1];
        segTree = new long[N * 4];

        for (int i = 1; i <= N; i++) {
            values[i] = Long.parseLong(br.readLine());
        }

        setTree(1, N, 1);

        for (int i = 0; i < M + K; i++) {
            st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());

            if (type == CHANGE) {
                int target = Integer.parseInt(st.nextToken());
                long num = Long.parseLong(st.nextToken());
                long diff = num - values[target];

                updateTree(1, N, target, diff, 1);
                values[target] = num;
            } else {
                int left = Integer.parseInt(st.nextToken());
                int right = Integer.parseInt(st.nextToken());

                long result = findResult(1, N, left, right, 1);
                System.out.println(result);
            }

            // System.out.println(" - value : " + Arrays.toString(values));
            // System.out.println(" -- debug : "  + Arrays.toString(segTree));
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

        segTree[idx] = segTree[idx * 2] + segTree[idx * 2 + 1];
    }

    static void updateTree(int start, int end, int target, long values, int idx) {

        if (target < start || target > end) return;

        int mid = (start + end) / 2;
        segTree[idx] += values;
        
        if (start == end) return;

        updateTree(start, mid, target, values, idx * 2);
        updateTree(mid + 1, end, target, values, idx * 2 + 1);
    }

    static long findResult(int start, int end, int left, int right, int idx) {

        if (right < start || left > end) return 0;
        if (start >= left && end <= right) return segTree[idx];

        int mid = (start + end) / 2;
        long result = findResult(start, mid, left, right, idx * 2);
        result += findResult(mid + 1, end, left, right, idx * 2 + 1);
        
        return result;
    }
} 
