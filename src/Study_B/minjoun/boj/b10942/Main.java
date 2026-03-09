package Study_B.minjoun.boj.b10942;

import java.io.*;
import java.util.*;

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;

    public static void main(String[] args) throws Exception{
        int N = Integer.parseInt(br.readLine().trim());
        int[] arr = new int[N+1];
        int[][] dp = new int[N+1][N+1];
        st = new StringTokenizer(br.readLine().trim());
        for (int i = 1; i <= N; i++) arr[i] = Integer.parseInt(st.nextToken());

        for (int i = N; i >= 1; i--) {
            for (int j = N; j >= 1; j--) {
                // 처음과 끝이 다르면 그냥 넘기기!
                if (arr[i] != arr[j]) continue;

                // 길이가 1일때
                if (i - j == 0) dp[i][j] = 1;
                    // 길이가 2일때
                else if (i - j == 1) dp[i][j] = 1;
                    // 길이가 3이상일때
                else {
                    if (i >= N || j == 1) continue;
                    dp[i][j] = dp[i+1][j-1] == 1 ? 1 : 0;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        int M = Integer.parseInt(br.readLine().trim());
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int s = Integer.parseInt(st.nextToken());
            int e = Integer.parseInt(st.nextToken());
            sb.append(dp[s][e]).append("\n");
        }
        System.out.println(sb.toString());
    }
}