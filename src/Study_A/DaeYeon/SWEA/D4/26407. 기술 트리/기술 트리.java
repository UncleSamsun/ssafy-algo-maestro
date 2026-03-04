import java.util.*;
import java.io.*;

public class Solution {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static boolean[] studied;
    static ArrayList<Integer>[] graph;
    static ArrayList<Integer> plan = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        int T = Integer.parseInt(br.readLine());
        for (int t = 1; t <= T; t++) {
            int N = Integer.parseInt(br.readLine());
            studied = new boolean[N + 1];
            graph = new ArrayList[N + 1];
            for (int i = 1; i <= N; i++) graph[i] = new ArrayList<>();

            for (int i = 1; i <= N; i++) {
                st = new StringTokenizer(br.readLine());
                int many = Integer.parseInt(st.nextToken());
                for (int j = 0; j < many; j++) {
                    graph[i].add(Integer.parseInt(st.nextToken()));
                }
            }
            System.out.printf("#%d %d\n", t, solve(N));
        }
    }

    static int solve(int N) {
        int sem = 0;
        int studiedCount = 0;
        while (studiedCount < N) {
            sem++;
            plan.clear();
            for (int i = 1; i <= N; i++) {
                if (studied[i]) continue;
                boolean canStudy = true;
                for (int j : graph[i]) {
                    if (!studied[j]) {
                        canStudy = false;
                        break;
                    }
                }
                if (canStudy) plan.add(i);
            }
            if (plan.isEmpty()) return -1;
            for (int i : plan) {
                studied[i] = true;
                studiedCount++;
            }
        }
        return sem;
    }
}