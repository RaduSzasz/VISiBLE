public class MaxOfFour { 
	

	public static void main(String[] args) {
		int x = -12345;
		int y = 2345;
		int z = 12345;
    int t = 0;
		int max = Integer.MIN_VALUE;

		// Function logic for maxOfThree()
		// PC: true
	  max = symVis(x, y, z, t);	

		System.out.println(max);
	}

  private static int symVis(int x, int y, int z, int t) {

    int max;
    if (x >= y) {
			
			if (x >= z) {

				// PC: x >= y && x >= z
        if (x >= t) {
          // PC: x >= y && x >= z && x >= t
  				max = x;
        }
        else {
          max = 321; 
        } 
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
    return max;
  }
}
