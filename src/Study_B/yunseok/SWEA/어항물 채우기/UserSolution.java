import java.util.*;

class UserSolution {

	static int N, mWidth, mHeight;
	
	// mIDs[] - ������ ���̵��, �ʱ� �Էµ� �ε������� ������ ���� ����.
	static int[] mIDs;
	static int[] rankOfId;
	
	// patterns[���ڵ��� ���� �� �ִ� ������ ���][������ ��ġ]
	// 64 - ���ڰ� ���� �� �ִ� (0 ~ 3)����� ���� �� �ִ� ������ ������.
	// 10000 - ������ �ִ� ���� (20) * ���� �ִ� �ʺ� (500)
	static int[][] patterns;
	
	// patternCnt[���ڵ��� ���� �� �ִ� ������ ���] - �ش� ������ ��Ÿ���� Ƚ��
	static int[] patternCnt = new int[64];
	
	// tanks[������ �ʱ� �ε���][���� �ʺ�] - �ش� ���� ���� ���� + ���� ������ ����.
	static int[][] tanks;

	private int getPattern(int i, int j) {
		return (tanks[i][j] & 3) << 4 | (tanks[i][j + 1] & 3) << 2 | (tanks[i][j + 2] & 3);
	}
	
	private void setPatterns() {
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < mWidth - 2; j++) {
				int p = getPattern(i, j);
				patterns[p][patternCnt[p]] = i << 12 | j ;
				patternCnt[p]++;
			}
		}
	}
	
	public void init(int N, int mWidth, int mHeight, int mIDs[], int mLengths[][], int mUpShapes[][]) {

		this.N = N;
		this.mWidth = mWidth;
		this.mHeight = mHeight;
		this.mIDs = new int[N];
		rankOfId = new int[N];
		this.tanks = new int[N][500];
		patterns = new int[64][N * 500];
		
		for (int i = 0; i < N; i++) {
			this.mIDs[i] = mIDs[i] * 100 + i;
		}
		
		Arrays.sort(this.mIDs);
		
		for (int i = 0; i < N; i++) {
			rankOfId[this.mIDs[i] % 100] = i;
		}
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < mWidth; j++) {
				tanks[i][j] = mLengths[i][j] << 2 | mUpShapes[i][j];
			}
		}
		
		setPatterns();
	}

	public int checkStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {

		int answer = 0;
		int pattern = mDownShapes[0] << 4 | mDownShapes[1] << 2 | mDownShapes[2];
		
		for (int i = 0; i < patternCnt[pattern]; i++) {
			
			int position = patterns[pattern][i];
			int idx = position >> 12;
			int row = position & 0xFFF;
			boolean flag = true;
			int prevHeight = 0;

			for (int k = 0; k < 3; k++) {
				int height = tanks[idx][row + k] >> 2;

				if (
					(k > 0 && !isConnected(height, mLengths[k], prevHeight, mLengths[k-1])) ||
					height + mLengths[k] > mHeight
				) {
					flag = false;
					break;
				}

				prevHeight = height;
			}
			
			if (flag) answer++;
		}
		
		return answer;
	}

	boolean isConnected(int height, int curDiff, int prevHeight, int prevDiff) {

		int prevBottom = prevHeight + 1;
		int prevTop = prevHeight + prevDiff;
		int bottom = height + 1;
		int top = height + curDiff;

		if (prevTop < bottom || prevBottom > top) return false;
		return true;
	}

	public int addStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {

		int pattern = mDownShapes[0] << 4 | mDownShapes[1] << 2 | mDownShapes[2];
		int rank = N + 1;
		int idx = 0, target = 0;
		
		for (int i = 0; i < patternCnt[pattern]; i++) {
			int position = patterns[pattern][i];
			int nidx = position >> 12;
			int row = position & 0x7FF;
			boolean flag = true;
			int prevHeight = 0;
			
			for (int k = 0; k < 3; k++) {

				int height = tanks[nidx][row + k] >> 2;

				if((k > 0 && !isConnected(height, mLengths[k], prevHeight, mLengths[k - 1])) ||
					height + mLengths[k] > mHeight
				) {
					flag = false;
					break;
				}

				prevHeight = height;
			}
			
			if (!flag) continue;
			if (rankOfId[nidx] >= rank) continue;
			
			rank = rankOfId[nidx];
			target = i;
			idx = nidx;
		}

		if (rank == N + 1) return 0;

		int id = mIDs[rank] / 100;
		int row = patterns[pattern][target] & 0x7FF;
		int cidx = mIDs[rank] % 100;
		
		resetPattern(cidx, row, mDownShapes, mUpShapes);

		for (int k = 0; k<3; k++) {
			int height = (tanks[idx][row + k] >> 2) + mLengths[k];
			tanks[idx][row + k] = height << 2 | mUpShapes[k];
		}
		
		return id * 1000 + row + 1;
	}

	void resetPattern(int idx, int row, int[] mDownShapes, int[] mUpShapes) {

		for (int i = -2; i <= 2; i++) {
	
			int start = row + i;
			if (start < 0 || start > mWidth - 3) continue;
	
			// 기존 패턴 계산
			int old1 = tanks[idx][start] & 3;
			int old2 = tanks[idx][start + 1] & 3;
			int old3 = tanks[idx][start + 2] & 3;
	
			int oldPattern = (old1 << 4) | (old2 << 2) | old3;
	
			// 변경 후 패턴 계산
			int new1 = (start >= row && start < row + 3)
					? mUpShapes[start - row]
					: old1;
	
			int new2 = (start + 1 >= row && start + 1 < row + 3)
					? mUpShapes[start + 1 - row]
					: old2;
	
			int new3 = (start + 2 >= row && start + 2 < row + 3)
					? mUpShapes[start + 2 - row]
					: old3;
	
			int newPattern = (new1 << 4) | (new2 << 2) | new3;
	
			int position = (idx << 12) | start;
	
			// 기존 패턴에서 제거
			int removeIdx = -1;
			for (int j = 0; j < patternCnt[oldPattern]; j++) {
				if (patterns[oldPattern][j] == position) {
					removeIdx = j;
					break;
				}
			}
	
			if (removeIdx == -1) continue; // 못 찾으면 스킵 (안전장치)
	
			int lastIdx = patternCnt[oldPattern] - 1;
			int moved = patterns[oldPattern][lastIdx];
	
			patterns[oldPattern][removeIdx] = moved;
			patternCnt[oldPattern]--;
	
			// 새 패턴으로 추가
			patterns[newPattern][patternCnt[newPattern]] = position;
			patternCnt[newPattern]++;
		}
	}

	public Solution.Result pourIn(int mWater) {
		Solution.Result ret = new Solution.Result();
		ret.ID = ret.height = ret.used = 0;
		
		int[][] cpySortDepth = new int[N][mWidth];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j<mWidth; j++) {
				cpySortDepth[i][j] = tanks[mIDs[i] % 100][j] >> 2;
			}
			Arrays.sort(cpySortDepth[i]);
		}
		
		int used = 0;
		
		for (int i = 0; i<N; i++) {
			int[] sortArr = cpySortDepth[i];
			int waterCnt = 0;
			int height = sortArr[0];
			// System.out.println("id : " + (mIDs[i] / 100));
			// System.out.println(" - " + Arrays.toString(sortArr));
			
			while (waterCnt <= mWater && height <= mHeight) {
				used = waterCnt;
				for (int j = 0; j < mWidth; j++) {
					if (sortArr[j] == height) {
						sortArr[j]++;
						waterCnt++;
					}
				}

				height++;
			}

			height -= 1;
			if (used == 0) continue;

			if (ret.height < height) {
				ret.height = height;
				ret.used = used;
				ret.ID = mIDs[i] / 100;
			} else if (ret.height == height) {
				if (ret.used < used) {
					ret.used = used;
					ret.ID = mIDs[i] / 100;
				}
			}

			// System.out.println(" - " + Arrays.toString(sortArr));
			// System.out.println(used + " " + (mIDs[i] / 100) + " " + height);
			// System.out.println(ret.used + " " + ret.ID + " " + ret.height);
			// System.out.println();
		}

		return ret;
	}
}