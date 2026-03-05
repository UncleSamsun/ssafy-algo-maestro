import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] srcNums = new int[N];

        for (int i = 0; i<N; i++) srcNums[i] = sc.nextInt();

        int[] dp = new int[N];
        int dpIdx = 0;

        for (int num : srcNums) {
            boolean flag = true;
            
            for (int i = 0; i < dpIdx; i++) {
                
                if (dp[i] >= num) {
                    flag = false;
                    dp[i] = num;
                    break;
                }
            }

            if (flag) {
                dp[dpIdx++] = num;
            }
        }

        System.out.println(dpIdx);
    }
}
