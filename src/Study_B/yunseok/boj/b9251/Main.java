import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        
        // A C A Y K P
        // C A P C A K


        Scanner sc = new Scanner(System.in);
        char[] charArr1 = sc.nextLine().toCharArray();
        char[] charArr2 = sc.nextLine().toCharArray();
        int n1 = charArr1.length;
        int n2 = charArr2.length;
        int[][] dp = new int[n1 + 1][n2 + 1];

        for (int j = 1; j <= n2; j++) {
            for (int i = 1; i <= n1; i++) {

                if (charArr2[j - 1] == charArr1[i - 1]) {
                    dp[i][j] = Math.max(dp[i - 1][j - 1] + 1, dp[i - 1][j]);
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
                }
            }
        }

        System.out.println(dp[n1][n2]);
        // int i = n1, j = n2;
        // char[] answer = new char[dp[i][j]];
        // int idx = 0;
        // int value = dp[i][j];

        // while (j != 0) {

        //     if (dp[i][j - 1] == value) j--;
        //     else if (dp[i - 1][j] == value) i--;
        //     else {
        //         answer[idx++] = charArr1[i - 1];
        //         value = dp[i][--j];
        //     }
        // }

        // StringBuilder sb = new StringBuilder();

        // for (int k = answer.length - 1; k >= 0; k--) {
        //     sb.append(answer[k]);
        // }

        // System.out.println(sb.toString());
    }
}
