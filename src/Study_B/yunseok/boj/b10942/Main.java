import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static int[] srcNums;
    static boolean[][] dp;
    public static void main(String[] args) throws IOException {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());
        srcNums = new int[N + 1];

        for (int i = 1; i <= N; i++)
            srcNums[i] = Integer.parseInt(st.nextToken());

        int M = Integer.parseInt(br.readLine());
        dp = new boolean[N + 1][N + 1];

        setDP(N);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int S = Integer.parseInt(st.nextToken());
            int E = Integer.parseInt(st.nextToken());

            if (dp[S][E]) sb.append("1\n");
            else sb.append("0\n");
        }

        System.out.println(sb.toString());
    }

    static void setDP(int N) {

        for (int i = 1; i <= N; i++) {
            dp[i][i] = true;
            
            if (srcNums[i] == srcNums[i - 1])
                dp[i-1][i] = true;    
        }

        for (int j = 2; j <= N - 1; j++) {
            for (int i = 1; i <= N - j; i++) {
                int end = i + j;
                dp[i][end] = dp[i + 1][end - 1] && srcNums[i] == srcNums[end];
            }
        }
    }

}
