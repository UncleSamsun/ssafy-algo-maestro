import java.util.*; 
public class Main {
	static class Node{
		TreeMap<String, Node> child = new TreeMap<>(); 
		
	}
	static Node root = new Node(); 
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in); 
		
		int t = sc.nextInt();
		
		for(int tc=1 ; tc<=t ; tc++) {
			int k = sc.nextInt(); // 들어올 string 개수 
			
			 Node current = root; 
			 String origin = sc.nextLine();
			 String[] arr = origin.split(" "); 
			 for(int j=1 ; j<= k ; j++) {
				 
				 String str = arr[j]; 
				 
				 if(!current.child.containsKey(str)) {
					 // 지금 자식으로 해당 str이 없다면 
					 current.child.put(str, new Node()); 
				 }
				 current = current.child.get(str); 
				 // 계속해서 아래로 넣어준다. 
			 }
			 
		
			 
		} // 테케 끝 
		print(root, 0); 

	}
	static void print(Node node, int depth) {
		for(String key : node.child.keySet()) {
			// 1층에 있는 string 종류를 알 수 있음 
			// 1층이니까 아무것도 출력안해도된다. 그럼 depth가 0으로 시작 
			for(int i=0 ; i< depth ; i++) {
				System.out.print("--");
			}
			System.out.println(key);
			print(node.child.get(key),depth+1); 
		}
	}
}
