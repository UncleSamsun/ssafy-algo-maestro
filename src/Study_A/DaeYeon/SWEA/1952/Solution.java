import java.util.*;
import java.io.*;

public class Solution {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static int[] memo = new int[12];
    static int[] plan = new int[12];
    static int day, month, quarter, year;

    public static void main(String[] args) throws IOException {
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            st = new StringTokenizer(br.readLine());
            day = Integer.parseInt(st.nextToken());
            month = Integer.parseInt(st.nextToken());
            quarter = Integer.parseInt(st.nextToken());
            year = Integer.parseInt(st.nextToken());
            Arrays.fill(memo, -1);

            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < 12; i++) {
                plan[i] = Integer.parseInt(st.nextToken());
            }
            int result = Math.min(solve(0), year);
            System.out.printf("#%d %d\n", t, result);
        }
    }

    static int solve(int m) {
        if (m >= 12) return 0;
        if (memo[m] != -1) return memo[m];

        int monthCost = Math.min(day * plan[m], month);
        int best = monthCost + solve(m + 1);

        best = Math.min(best, quarter + solve(m + 3));

        return memo[m] = best;
    }
}