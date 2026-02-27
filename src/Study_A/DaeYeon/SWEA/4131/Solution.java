import java.util.*;
import java.io.*;

public class Solution {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static int[][] cliff;

    public static void main(String[] args) throws IOException {
        int T = Integer.parseInt(br.readLine());
        for (int t = 1; t <= T; t++) {
            st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken());
            int X = Integer.parseInt(st.nextToken());
            cliff = new int[N][N];
            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++) {
                    cliff[i][j] = Integer.parseInt(st.nextToken());
                }
            }
            int result = 0;
            for (int i = 0; i < N; i++) {
                if (canBuildRow(i, N, X)) result++;
                if (canBuildColumn(i, N, X)) result++;
            }
            System.out.printf("#%d %d\n", t, result);
        }
    }

    static boolean canBuildRow(int r, int N, int X) {
        int[] arr = new int[N];
        for (int c = 0; c < N; c++)
            arr[c] = cliff[r][c];
        return canBuild(arr, N, X);
    }

    static boolean canBuildColumn(int c, int N, int X) {
        int[] arr = new int[N];
        for (int r = 0; r < N; r++)
            arr[r] = cliff[r][c];
        return canBuild(arr, N, X);
    }

    static boolean canBuild(int[] arr, int N, int X) {
        boolean[] used = new boolean[N];

        for (int i = 0; i < N - 1; i++) {
            int diff = arr[i + 1] - arr[i];

            if (diff == 0)
                continue;

            if (Math.abs(diff) >= 2) return false;

            if (diff == 1) {
                int h = arr[i];
                for (int j = i; j >= i - (X - 1); j--) {
                    if (j < 0) return false;
                    if (arr[j] != h) return false;
                    if (used[j]) return false;
                }
                for (int j = i; j >= i - (X - 1); j--) {
                    used[j] = true;
                }
            } else {
                int h = arr[i + 1];
                for (int j = i + 1; j <= i + X; j++) {
                    if (j >= N) return false;
                    if (arr[j] != h) return false;
                    if (used[j]) return false;
                }
                for (int j = i + 1; j <= i + X; j++) {
                    used[j] = true;
                }
            }
        }
        return true;
    }
}