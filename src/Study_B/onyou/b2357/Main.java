import java.util.*; 
public class Main {
	public static class segmentTree{
		long[] num;
		long[] tree;
		
		segmentTree(long[] arr, boolean isMaxTree){
			this.num = arr; 
			this.tree = new long[arr.length*4]; 
			
			init(1, 0, arr.length-1, isMaxTree); 
		}
		
		public long init(int node, long start , long end, boolean isMaxTree) {
			if(start == end) {
				return tree[node] = num[(int)start]; 
			}
			long mid = (start + end) / 2; 
			// maxtree이면 Math.max 하고 아니면 Math.min 한다 
			return tree[node] = isMaxTree ? Math.max(init(node*2, start , mid,isMaxTree), init(node*2+1, mid+1, end,isMaxTree)) : Math.min(init(node*2, start , mid, isMaxTree), init(node*2+1, mid+1, end,isMaxTree)) ;
		}
		
		
		
		public long search(int node, long start ,long end, long left, long right, boolean isMaxTree) {
			// 구간에서 최소값 찾기 
			if(left >end || right < start) {
				if(isMaxTree) {
					return Long.MIN_VALUE; 
				}else return Long.MAX_VALUE; 
				
			}
			
			//완전히 겹칠 때 
			if(left <= start && end <= right) {
				return tree[node]; 
			}
			
			
			long mid= (start + end)/ 2;
			if(isMaxTree) {
				return Math.max(search(node*2 , start, mid,left, right,isMaxTree),search(node*2+1, mid+1, end, left, right,isMaxTree)); 
			}else {
				return Math.min(search(node*2 , start, mid,left, right, isMaxTree),search(node*2+1, mid+1, end, left, right,isMaxTree)); 
			}
			
			
			}
	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in); 
		
		int n  = sc.nextInt(); // 정수 개수 
		int m = sc.nextInt(); // a,b쌍의 개수 
		
		long[] arr= new long[n]; 
		
		for(int i=0 ; i<n; i++) {
			arr[i] = sc.nextLong();
		}
		
		segmentTree maxSegTree = new segmentTree(arr,true); 
		segmentTree minSegTree = new segmentTree(arr,false); 
		
	
		for(int i=0; i<m ; i++) {
			long left = sc.nextLong() -1;
			long right = sc.nextLong() -1 ; 
			
			long minResult= minSegTree.search(1, 0, n-1, left, right, false);
			long maxResult = maxSegTree.search(1, 0, n-1, left, right, true); 
			System.out.println(minResult + " " + maxResult);
		}

	}

}
