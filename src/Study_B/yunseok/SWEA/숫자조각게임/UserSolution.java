package swea;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

class UserSolution {

//	static final int NOT_REACH = -1;
	static final int[] NOT_EXIST = null;
	
	int mRows, mCols;
	int[][] mCells;
	boolean[][] visited;
	Map<Integer, Deque<int[]>>[] mapmap = new HashMap[5];
	
	int[][] dx = {
			{0},
			{0, 0},
			{1, 2},
			{0, 1, 1},
			{1, 1, 1, 2}
	};
	
	int[][] dy = {
			{1},
			{1, 2},
			{0, 0},
			{1, 1, 2},
			{0, 1, 2, 2}
	};
	
	
	// mRows, mCols - 게임판의 행, 열 개수 ( 5 <= mRows <= 40 ,  5 <= mCols <= 30 )
	// mCells - 게임판 내 셀에 적힌 숫자 ( 1 <= mCells[][] <= 5 )
	// 내 의도 - 들어올 수 있는 타입 별로, 패턴을 기록한다.
	// 패턴은 어떻게 기록할 것인가. - 시작점을 기점으로 증감폭을 통해 설정한다.
	public void init(int mRows, int mCols, int mCells[][]) {
		this.mRows = mRows;
		this.mCols = mCols;
		this.mCells = mCells;
		for (int i = 0; i < 5; i++) mapmap[i] = new HashMap<Integer, Deque<int[]>>();
		
		visited = new boolean[mRows][mCols];
		
		initHashing();
		
//		for (int i = 0; i< 5; i++) {
//			System.out.println(" -- type " + " i : " + mapmap[i].keySet().toString());
//		}
	}

	void initHashing() {
		
		for (int i = 0; i < 5; i++) {
			hashing(i, mapmap[i]);
		}
		
	}
	
	void hashing(int i, Map<Integer, Deque<int[]>> map) {
		
		for (int x = 0; x < mRows; x++) {
			loop : for (int y = 0; y < mCols; y++) {
				int start = mCells[x][y];
				
				int pattern = 0;
				for (int d = 0; d < dx[i].length; d++) {
					pattern *= 10;

					int nx = x + dx[i][d];
					int ny = y + dy[i][d];
					if (!validate(nx, ny)) continue loop;
					
					int diff = mCells[nx][ny] - start + 5;
					
					pattern += diff;
				}
				
				if (map.containsKey(pattern)) {
					map.get(pattern).add(new int[] {x, y});
				} else {
					Deque<int[]> q = new ArrayDeque<>();
					q.add(new int[] {x, y});
					map.put(pattern, q);
				}
			}
		}
	}
	
	
	boolean validate(int x, int y) {
		return x >= 0 && x < mRows && y >= 0 && y < mCols;
	}
	
	
	// mPuzzle[3][3] - 조각의 정보 ( 1 <= mPuzzle[][] <= 5 )
	// 주어진 조각의 가장 윗부분 중 가장 왼쪽의 숫자는 [0][0]에 위치한다.
	// ret.row , ret.col - 주어진 조각이 맞게 놓이는 위치의 행, 열 번호.
	// - 없으면 -1 반환
	// 최대 100_000회 호출
	
	// 블록은 총 5가지가 존재한다.
	// 1 1 0   1 1 1   1 0 0   1 1 0   1 0 0
	// 0 0 0   0 0 0   1 0 0   0 1 1   1 1 1
	// 0 0 0   0 0 0   1 0 0   0 0 0   0 0 1
	public Solution.Result putPuzzle(int mPuzzle[][]) {
		Solution.Result ret = new Solution.Result(-1, -1);
		
		int type = findType(mPuzzle);
		int pattern = 0;
		int start = mPuzzle[0][0];
		
		for (int d = 0; d < dx[type].length; d++) {
				
			pattern *= 10;
			
			int nx = dx[type][d];
			int ny = dy[type][d];
			int diff = mPuzzle[nx][ny] - start + 5;
			
			pattern += diff;
		}
		
		Map<Integer, Deque<int[]>> map = mapmap[type];
		
		if (map.containsKey(pattern)) {
			Deque<int[]> list = map.get(pattern);
			
			int[] xy = findResult(list, type);
			if (xy == NOT_EXIST) {
				ret.row = -1;
				ret.col = -1;
			}
			else {
				visitXY(xy[0], xy[1], type);
				ret.row = xy[0];
				ret.col = xy[1];
			}
		}
		
		
		return ret;
	}
	
	void visitXY(int x, int y, int type) {
		
		visited[x][y] = true;
		
		for (int d = 0; d < dx[type].length; d++) {
			int nx = x + dx[type][d];
			int ny = y + dy[type][d];
			
			visited[nx][ny] = true;
		}
	}
	
	int[] findResult(Deque<int[]> list, int type) {
		
		for (int[] xy : list) {
			if (isPossible(xy[0], xy[1], type)) return xy;
		}
		
		return NOT_EXIST;
	}
	
	boolean isPossible(int x, int y, int type) {
		
		if (visited[x][y]) return false;
		
		for (int d = 0; d < dx[type].length; d++) {
			
			int nx = x + dx[type][d];
			int ny = y + dy[type][d];
			
			if (visited[nx][ny]) return false;
		}
		
		return true;
	}

	int findType(int[][] mPuzzle) {
		
        if (mPuzzle[2][0] != 0) {
            return 2;
        }
 
        // 가로 3칸
        if (mPuzzle[0][2] != 0) {
            return 1;
        }
 
        if (mPuzzle[2][2] != 0) {
            return 4;
        }
 
        if (mPuzzle[1][1] != 0) {
            return 3;
        }
 
        return 0;
	}
	
	// block의 타입이 1과 3은 중복 체크될 수 있으므로,
	// 1은 따로 처리해준다.
	boolean isTypeOne(int[][] mPuzzle) {
		
		if (mPuzzle[0][2] != 0) return false;
		if (mPuzzle[1][1] != 0 || mPuzzle[1][0] != 0) return false;
		
		return true;
	}
	
	// 게임판에 놓인 조각들을 모두 없앰.
	// 최대 1000회 호출.
	public void clearPuzzles() {
		for (int i = 0; i < mRows; i++)
			Arrays.fill(visited[i], false);
	}

}