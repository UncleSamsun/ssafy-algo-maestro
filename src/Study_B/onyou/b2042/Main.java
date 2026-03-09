package exe;
import java.util.*;

public class Main{
	public static class segmentTree {
		long[] num;
		long[] tree;

		public segmentTree(long[] arr) {
			this.num = arr;
			this.tree = new long[arr.length * 4];

			// 여기서 init 하기
			init(1, 0, arr.length - 1); 
		}

		public long init(int node, long start, long end) {
			if (start == end)
				return tree[node] = num[(int)start]; // 리프 노드일 때

			long mid = (start + end) / 2;
			
			return tree[node] = init(node * 2, start, mid) + init(node * 2 + 1, mid + 1, end); // 리프 노드가 아닐 때

		}

		public void update(int node, long start, long end, long index, long newValue) {
			if (index < start || index > end)
				return;

			if (start == end) {
				this.num[(int)index] = newValue;
				this.tree[node] = newValue;
				return; 
			} // 리프에서만 업데이트

			long mid = (start + end) / 2;

			update(node * 2, start, mid, index, newValue);
			update(node * 2 + 1, mid + 1, end, index, newValue);

			tree[node] = tree[node * 2] + tree[node * 2 + 1];

			// 계산한 값을 더해서 간다

		}

		public long sum(int node, long start, long end, long left, long right) {
			if (left > end || right < start)
				return 0;

			// left-right가 더 큰 범위 일때
			if (left <= start && end <= right)
				return tree[node];

			long mid = (start + end) / 2;
			return sum(node * 2, start, mid, left, right) + sum(node * 2 + 1, mid + 1, end, left, right);
		}
	}

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		
		int n = (int)sc.nextLong(); // 수의 개수
		long m = sc.nextLong(); // 변경 횟수
		long k = sc.nextLong(); // 구간의 합 횟수

		long[] arr = new long[n];
		for (int i = 0; i < n; i++) {
			arr[i] = sc.nextLong();
		}

		// 세그먼트 트리 생성
		segmentTree segTree = new segmentTree(arr);
		StringBuilder sb = new StringBuilder(); 
		for (int i = 0; i < k + m; i++) {
			int number = sc.nextInt();

			if (number == 1) {
				// 값 변경
				long idx = sc.nextLong() -1 ;
				long value = sc.nextLong();

				segTree.update(1, 0, n - 1, idx, value);
			} else {
				// 구간 합 구하기
				long start = sc.nextLong() -1; 
				long end = sc.nextLong() -1 ;

				long result = segTree.sum(1, 0, n - 1, start, end);
				sb.append(result).append("\n"); 
			}
		}
		System.out.print(sb);
	}

}