import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
    
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[] fibo = new int[N + 1];
        
        fibo[1] = 1;

        for (int i = 2; i <= N; i++)
            fibo[i] = fibo[i - 1] + fibo[i - 2];
            
        System.out.println(fibo[N]);
        
    }
}
