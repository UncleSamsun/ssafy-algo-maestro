package Study_B.yunseok.boj.b23309;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// 23309번 - 철도 공사

public class Main {

    static int MAX_ID = 1_000_001;
    
    static int[] next = new int[MAX_ID];
    static int[] prev = new int[MAX_ID];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        int[] arr = new int[N];

        for (int i = 0; i < N; i++)
            arr[i] = Integer.parseInt(st.nextToken());

        prev[arr[0]] = arr[N - 1];
        next[arr[0]] = arr[1];
        prev[arr[N - 1]] = arr[N - 2];
        next[arr[N - 1]] = arr[0];

        for (int i = 1; i < N - 1; i++) {
            next[arr[i]] = arr[i + 1];
            prev[arr[i]] = arr[i - 1];
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            String cmd = st.nextToken();
            int target = Integer.parseInt(st.nextToken());

            if (cmd.equals("BN")) {
                int bn = Integer.parseInt(st.nextToken());
                sb.append(next[target] + "\n");

                next[bn] = next[target];
                prev[next[target]] = bn;
                prev[bn] = target;
                next[target] = bn;

            } else if (cmd.equals("BP")) {
                int bp = Integer.parseInt(st.nextToken());
                sb.append(prev[target] + "\n");

                next[bp] = target;
                next[prev[target]] = bp;
                prev[bp] = prev[target];
                prev[target] = bp;
                
            } else if (cmd.equals("CP")) {
                sb.append(prev[target] + "\n");

                prev[target] = prev[prev[target]];
                next[prev[target]] = target;
            } else {
                sb.append(next[target]+"\n");

                next[target] = next[next[target]];
                prev[next[target]] = target;
            }
        }

        System.out.println(sb.toString());
    }    

}
