public class TestTree {
	

	public static void main(String[] args) {
		int x = -1;
		int y = 0;
		int z = 1;
		int max = Integer.MIN_VALUE;

		// Function logic for maxOfThree()
		// PC: true
		if (x >= y) {
			
			if (x >= z) {
				// PC: x > y && x >= z
				max = x;
			}

			else {
				// PC: z > x >=
				max = z;
			}
		}

		// PC: y >= x
		else {
		
			if (y >= z) {
				// PC: y >= x && y > z
				max = y;
			}

			else {
				// PC: z > y > x 
				max = z;
			}
		}

		System.out.println(max);
	}
}