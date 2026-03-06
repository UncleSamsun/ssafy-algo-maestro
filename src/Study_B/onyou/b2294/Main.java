
import java.util.*;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int n = sc.nextInt(); // 동전 종류
		int k = sc.nextInt(); // 목표 금액

		int[] coins = new int[n];
		for (int i = 0; i < n; i++) {
			coins[i] = sc.nextInt();
		}

		int[] dp = new int[k + 1];

		Arrays.fill(dp, 100000);
		dp[0] = 0;

		for (int i = 0; i < n; i++) {
			for (int j = coins[i]; j <= k; j++) {

				dp[j] = Math.min(dp[j - coins[i]] + 1, dp[j]);

			}
		}

		int result = (dp[k] == 100000) ? -1 : dp[k];
		System.out.println(result);
	}

}
