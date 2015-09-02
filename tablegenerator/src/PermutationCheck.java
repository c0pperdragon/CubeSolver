
import java.util.Arrays;
import java.util.Vector;

// compute all possible corner permutation when 
// applying turns of G3 only

public class PermutationCheck 
{
	static String[] perms4567 =  {
		"4567", "4576", "4657", "4675", "4756", "4765",
		"5467", "5476", "5647", "5674", "5746", "5764",
		"6457", "6475", "6547", "6574", "6745", "6754",
		"7456", "7465", "7546", "7564", "7645", "7654"		
		};
//	static String[] perms4567 =  {
//	  //  567     576     657     756     675     765
//		"4567", "4576", "4657", "4756", "4675", "4765",
//		"5467", "5476", "6457", "7456", "6475", "7465",
//		"5647", "5746", "6547", "7546", "6745", "7645",
//		"5674", "5764", "6574", "7564", "6754", "7654"		
//		};
	
	static int indexofperm4567(String s)
	{	for (int i=0; i<perms4567.length; i++)
		{	if (perms4567[i].equals(s)) return i;
		}
		return -1;
	}
	static int indexofperm0123(String s)
	{	return indexofperm4567(s.replace('0','4').replace('1','5').replace('2','6').replace('3','7'));
	}
	
	public static void main(String[] args)
	{
//		 checkdoubleturns();
		 generatecornerpermutations();
	}
	
	static void checkdoubleturns()
	{
	  int[][] twistmatrix = new int[24][24];
	  for (int i=0; i<24; i++ ) for (int j=0; j<24; j++) twistmatrix[i][j]=-1;
	  
	  for (int twist=0; twist<6; twist++) 
	  {			
		Vector<String> perms = new Vector<String>();
		Vector<String> newfound = new Vector<String>();
		
		newfound.addElement(
			new String[]{ "01234567", "01234576", "01234657", "01234675", "01234756", "01234765" } 
			[twist]
		);
		
		while (newfound.size()>0)
		{	
			String n = (String) newfound.remove(0);
			if (!perms.contains(n))
			{	
				perms.addElement(n);
				newfound.addElement ( permute2(n, 1,2, 5,6));   // U
				newfound.addElement ( permute2(n, 0,3, 7,4));   // D
				newfound.addElement ( permute2(n, 2,0, 5,7));   // L
				newfound.addElement ( permute2(n, 1,3, 6,4));   // R
				newfound.addElement ( permute2(n, 5,4, 1,0));   // F
				newfound.addElement ( permute2(n, 2,3, 6,7));   // B
			}
		}
		
		boolean[][] allowmatrix = new boolean[24][24]; 
		
		Object[] o = perms.toArray();
		Arrays.sort(o);
		for (int x=0; x<o.length; x++)
		{	String p = (String) o[x];
			int i = indexofperm0123(p.substring(0,4));
			int j = indexofperm4567(p.substring(4));
			System.out.println(p+" "+i+" "+j);
			allowmatrix[i][j] = true;
		}
		
		System.out.println("Twist: "+twist);
		for (int i=0; i<24; i++)
		{	System.out.print(i + (i<10?"   ":"  "));
			for (int j=0; j<24; j++)
			{	System.out.print(allowmatrix[i][j] ? "X ":"  ");
				if (allowmatrix[i][j]) 
				{	twistmatrix[i][j]=twist;
				}
			}
			System.out.println();
		}
	  }
	  System.out.println ("Total corner twist:");
		for (int i=0; i<24; i++)
		{	System.out.print(i + (i<10?"   ":"  "));
			for (int j=0; j<24; j++)
			{	System.out.print(twistmatrix[i][j]);
			}
			System.out.println();
		}
	}
	
	static String permute2(String s, int a1, int a2, int b1, int b2)
	{
		char[] c = s.toCharArray();
		char x = c[a1];
		c[a1] = c[a2];
		c[a2] = x;
		x = c[b1];
		c[b1] = c[b2];
		c[b2] = x;
		return new String(c);
	}
	static String rotate4(String s, int a1, int a2, int a3, int a4)
	{
		char[] c = s.toCharArray();
		char x = c[a1];
		c[a1] = c[a2];
		c[a2] = c[a3];
		c[a3] = c[a4];
		c[a4] = x;
		return new String(c);		
	}
	
	static int perm2index(String p)
	{
		return ((p.charAt(0)-'0') << 21)
			|  ((p.charAt(1)-'0') << 18)
			|  ((p.charAt(2)-'0') << 15)
			|  ((p.charAt(3)-'0') << 12)
			|  ((p.charAt(4)-'0') << 9)
			|  ((p.charAt(5)-'0') << 6)
			|  ((p.charAt(6)-'0') << 3)
			|  ((p.charAt(7)-'0') << 0);
	}
	static String index2perm(int i)
	{
		return "" + ((i>>21) & 7)
			+  "" + ((i>>18) & 7)
			+  "" + ((i>>15) & 7)
			+  "" + ((i>>12) & 7)
			+  "" + ((i>>9) & 7)
			+  "" + ((i>>6) & 7)
			+  "" + ((i>>3) & 7)
			+  "" + ((i>>0) & 7);
	}

	static short[] generatecornerpermutations()
	{			
		short[] allcycles = new short[8*8*8*8*8*8*8*8];
		for (int i=0; i<allcycles.length; i++) allcycles[i]=-1;
		
		Vector<String> perms = new Vector<String>();
		Vector<String> newfound = new Vector<String>();
		newfound.addElement( "01234567" );

		gencycles: for (int cycle=0; ;cycle++)
		{
			// generate all by 2-turns reachable permutations		
			while (newfound.size()>0)
			{	
				String n = (String) newfound.remove(0);
				if (!perms.contains(n))
				{	
					perms.addElement(n);
					allcycles[perm2index(n)] = (short) cycle;

					newfound.addElement ( permute2(n, 0,1, 4,5));   // U
					newfound.addElement ( permute2(n, 2,3, 6,7));   // D
					newfound.addElement ( permute2(n, 0,2, 4,6));   // L
					newfound.addElement ( permute2(n, 1,3, 5,7));   // R
					newfound.addElement ( permute2(n, 0,3, 5,6));   // F
					newfound.addElement ( permute2(n, 4,7, 1,2));   // B
				}
			}
			
			System.out.println ("cycle: "+cycle+" of size: "+perms.size());
			
			// try to reach a new cycle by applying a single-F-turn to one of the permutations
			for (int i=0; i<allcycles.length; i++)
			{
				if (allcycles[i]<0) continue;
				String n = rotate4(index2perm(i), 0,5,3,6);
				if (allcycles[perm2index(n)]<0)
				{
					newfound.setSize(0);
					perms.setSize(0);
					newfound.addElement(n);
					continue gencycles;
				}
				
			}
			break; // no fresh cycle found
		}
		
		return allcycles;
	}
	
	
}
