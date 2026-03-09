package Study_B.changhyeon.boj.b9251;

import java.util.*;
import java.io.*;

class Main{
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String s1 = br.readLine();
        String s2 = br.readLine();

        int n = s1.length();
        int m = s2.length();

        int[][] dp = new int[n+1][m+1];

        int max = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i-1) == s2.charAt(j-1)) dp[i][j] = dp[i-1][j-1] + 1;
                else dp[i][j] = Math.max(dp[i][j-1], dp[i-1][j]);
                max = Math.max(max, dp[i][j]);
            }
        }
        System.out.println(max);
    }
}

// dp[i] << 정의
// dp[i][j] s1 = i번째 ~ s2 = j번째 까지의 최장 공통 수열