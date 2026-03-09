package exe;

import java.util.*;

public class Main {
	public static class segmentTree {
		long[] count;
		long[] tree;

		segmentTree(long[] arr) {
			this.count = arr;
			this.tree = new long[arr.length * 4];
			// 처음에는 트리가 비워져있어야 한다.
		}

		public void upDate(int node, long start, long end, long target) {
			if (target < start || end < target)
				return;

			if (start == end) {
				// 같을 때
				tree[node]++; 
				return;  

			}

			long mid = (start + end) / 2;
			upDate(node*2, start, mid, target); 
			upDate(node*2+1,mid+1, end,target); 
			
			tree[node]= tree[node*2] + tree[node*2+1]; 
		}

		public long searchSwap(int node, long start, long end, long left, long right) {
			if (left > end || right < start)
				return 0;

			// 완전 겹치는 경우
			if (left <= start && end <= right) {
				return tree[node];
			}
			long mid = (start + end) / 2;

			return searchSwap(node * 2, start, mid, left, right) + searchSwap(node * 2 + 1, mid + 1, end, left, right);

		}
	}

	public static void main(String[] args) {
		// 어떤 트리를 만들 것인가를 먼저 정하자
		// 10값이 들어오면 트리의 10번 자리에 +1을 해보자
		// 그러면 나보다 뒤에 있는 숫자의 갯수를 알 수 있다.
		// 근데 입력값이 너무 크면 터져서 자료 압축이 필요하다.
		// 세그먼트 트리에는 등장 빈도수를 저장한다.
		// swap 된다는 말은 나보다 큰 숫자가 내 왼쪽에 있다는 뜻이다.
		// tree 1번 노드에는 1-n까지 몇번 등장했는지 sum 을 저장하는 것이다.
		// 리프노드에는 각자 담당하는 인덱스에 +1 하는 것.

		// 자료압축을 해보자
		// 중복 제거 및 정렬 , 순위 매기고 치환
		// 입력을 우선 받고, sort 한다음 중복을 없애고, binarySearch를 통해 해당 값을 압축한다.

		Scanner sc = new Scanner(System.in);

		int n = sc.nextInt();

		long[] arr = new long[n];
		long[] copyArr = new long[n];
		for (int i = 0; i < n; i++) {
			arr[i] = sc.nextLong();
			copyArr[i] = arr[i];
		}
		Arrays.sort(copyArr);

		int idx = 0;
		copyArr[idx++] = copyArr[0];
		for (int i = 1; i < copyArr.length; i++) {
			if (copyArr[i] != copyArr[i - 1]) {
				// 전이랑 다르다면
				copyArr[idx++] = copyArr[i];
			}
		}

		for (int i = 0; i < n; i++) {
			arr[i] = Arrays.binarySearch(copyArr, 0, idx, arr[i]); // copyArr에서 arr[i]가 몇번째 인지 확인

		}
		// 자료압축 끝

		segmentTree segTree = new segmentTree(arr);

		
		long resultSum = 0; 
		for(int i=0 ; i<n ; i++) {
			long curr = arr[i]; 
			resultSum += segTree.searchSwap(1, 0, n-1, curr+1, n-1 ); // 
			segTree.upDate(1, 0, n-1, arr[i]);
		}
		System.out.println(Arrays.toString(segTree.tree));
		System.out.println(resultSum);

	}

}
