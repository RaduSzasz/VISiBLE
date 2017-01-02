public class MaxOfFour { 
	

	public static void main(String[] args) {
		int x = 123;
		int y = 234;
		int z = 345;
   	int t = 0;

		// Function logic for maxOfFour()
		// PC: true
		System.out.println(symVis(x, y, z, t));
	}

  private static String symVis(int x, int y, int z, int t) {

    String max;
    if (x >= y) {
			
		if (x >= z) {

				// PC: x >= y && x >= z
        	if (x >= t) {
          		// PC: x >= y && x >= z && x >= t
  				max = "000";
        	} 
        	else {
        		max = "001";
        	}
        
		}

		else {
			// PC: z > x >=
			max = "01";
		}
	}

	// PC: y >= x
	else {
		
		if (y >= z) {
			// PC: y >= x && y > z
			max = "10";
		}

		else {
			// PC: z > y > x 
			max = "11";
		}
	}

    return max;
  }
}
