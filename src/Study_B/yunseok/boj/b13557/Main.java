import java.io.*;
import java.util.*;

public class Main {
    static final long INF = 1_000_000_000_000_000L; // 충분히 작은 값

    static class Node {
        long total, lmax, rmax, max;

        Node(long total, long lmax, long rmax, long max) {
            this.total = total;
            this.lmax = lmax;
            this.rmax = rmax;
            this.max = max;
        }
    }

    static int[] nums;
    static Node[] tree;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        nums = new int[N + 1];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= N; i++) nums[i] = Integer.parseInt(st.nextToken());

        tree = new Node[4 * N];
        tree[0] = new Node(0, 0, 0, 0);
        build(1, 1, N);

        int M = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();

        while (M-- > 0) {
            st = new StringTokenizer(br.readLine());
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());

            if (y1 < x2) {
                // Case A: 구간이 겹치지 않음 [x1, y1] ... [x2, y2]
                long res = query(1, 1, N, x1, y1).rmax 
                         + query(1, 1, N, y1 + 1, x2 - 1).total 
                         + query(1, 1, N, x2, y2).lmax;
                sb.append(res).append("\n");
            } else {
                // Case B: 구간이 겹침 [x1, [x2, y1], y2]
                Node mid = query(1, 1, N, x2, y1);
                long ans = mid.max; // 1. 겹치는 구간 내에서 해결

                // 2. 왼쪽에서 시작해서 겹치는 구간/오른쪽까지 연장
                Node left = query(1, 1, N, x1, x2 - 1);
                Node right = query(1, 1, N, y1 + 1, y2);
                
                if (left != tree[0]) ans = Math.max(ans, left.rmax + query(1, 1, N, x2, y2).lmax);
                if (right != tree[0]) ans = Math.max(ans, query(1, 1, N, x1, y1).rmax + right.lmax);
                
                sb.append(ans).append("\n");
            }
        }
        System.out.print(sb);
    }

    static Node merge(Node L, Node R) {
        if (L == tree[0]) return R;
        if (R == tree[0]) return L;
        return new Node(
            L.total + R.total,
            Math.max(L.lmax, L.total + R.lmax),
            Math.max(R.rmax, R.total + L.rmax),
            Math.max(Math.max(L.max, R.max), L.rmax + R.lmax)
        );
    }

    static void build(int node, int start, int end) {
        if (start == end) {
            tree[node] = new Node(nums[start], nums[start], nums[start], nums[start]);
            return;
        }
        int mid = (start + end) / 2;
        build(node * 2, start, mid);
        build(node * 2 + 1, mid + 1, end);
        tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
    }

    static Node query(int node, int start, int end, int left, int right) {
        // System.out.println(" - left, right : " + left + " " + right);
        if (left > right || right < start || end < left) return tree[0];
        if (left <= start && end <= right) return tree[node];
        int mid = (start + end) / 2;
        return merge(query(node * 2, start, mid, left, right),
                     query(node * 2 + 1, mid + 1, end, left, right));
    }
}