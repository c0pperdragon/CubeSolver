import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;


public class AlgorithmTranslator 
{
	static final byte D1  = 0;  // quarter turn clockwise
	static final byte D2  = 1;  // half turn
	static final byte D3  = 2;  // quarter turn anti-clockwise
	static final byte L1  = 3;  // quarter turn clockwise
	static final byte L2  = 4;  // half turn
	static final byte L3  = 5;  // quarter turn anti-clockwise
	static final byte R1  = 6;  // quarter turn clockwise
	static final byte R2  = 7;  // half turn
	static final byte R3  = 8;  // quarter turn anti-clockwise
	static final byte F1  = 9;  // quarter turn clockwise
	static final byte F2  = 10;  // half turn
	static final byte F3  = 11;  // quarter turn anti-clockwise
	static final byte B1  = 12; // quarter turn clockwise
	static final byte B2  = 13;  // half turn
	static final byte B3  = 14; // quarter turn anti-clockwise
	
	static Vector<byte[]> generated;
	
	
	public static Vector<byte[]> genOLL()
	{
		generated = new Vector<byte[]>();
		gen("", 1);
		gen("LF'LF2R'FRF2L2", 4);
		gen("FURU'R'F'",4);
		gen("F'U'L'ULF",4);
		gen("LUF'U'L'ULFL'",4);
		gen("R'U'FURU'R'F'R",4);
		gen("LU'L'U2LUFU'F'U'L'",4);
		gen("R'URU2R'U'F'UFUR",4);
		gen("FURU'BR'F'RB'R'",4);
		gen("F'U'L'UB'LFL'BL",4);
		gen("FRUR2U'F'UFRF'",4);
		gen("F'L'U'L2UFU'F'L'F",4);
		gen("L'U2L2F'L'FL'U2L",4);
		gen("FR'F'RURU'R'",4);
		gen("R'F2LFL'FR",4);
		gen("LFR'FRF2L'",4);
		gen("FUF'R'FRU'R'F'R",4);
		gen("R'L2FL'FLF2L'FRL'",4);
		gen("R'F'LF'L'F2R",4);
		gen("F'U'FLF'L'ULFL'",4);
		gen("LR2F'RF'R'F2RF'L'R",4);
		gen("LF2R'F'RF'L'",4);
		gen("FR'F'RU2F2LFL'F",4);
		gen("F'LFL'U2F2R'F'RF'",4);
		gen("F'LF2L'U2L'U2LF'",4);
		gen("FL'U2LU2LF2L'F",4);
		gen("FRUR'U'RUR'U'F'",4);
		gen("F'L'U'LUL'U'LUF",4);
		gen("RL'UR'U'R'LFRF'",2);
		gen("R'U'RUFRB'R'BF'",4);
		gen("FUFR'F'RU'F'",4);
		gen("RUR'U'R'FRF'",4);
		gen("FRUR'U'F'",4);
		gen("LF'L'U'LUFU'L'",4);
		gen("R'FRUR'U'F'UR",4);
		gen("R'FRUR'F'RFU'F'",4);
		gen("RBR'LUL'U'RB'R'",4);
		gen("LF'L'U'LFL'F'UF",4);
		gen("L'B'LR'U'RUL'BL",4);
		gen("FURU'R'URU'R'F'",4);
		gen("R'U'RU'R'UF'UFR",4);
		gen("LFRF'L2FUR'U'F'L",2);
		gen("LFU'RUR2F'L'FRF'",2);
		gen("RL'U'B'UBUBUB'U'LR'",1);
		gen("FURU'R'F'RBUB'U'R'",4);
		gen("FURU'R'F'LFUF'U'L'",4);
		gen("LF'L'FU2FU'RU'R'F'",4);
		gen("BL2UF'U'L'UFL'U'B'",4);
		gen("FU2F'L'B'R'U'RU'BL",4);
		gen("LF'L'FUF2R'F'RU'F'",4);
		gen("FUR'FRF2U'F'LFL'",2);
		gen("F'U2FLU'F'U'FUL'",4);
		gen("R'F'LFRF'L'F",4);
		gen("R'F'L'FRF'LF",4);
		gen("RU'L'UR'U'L",4);
		gen("F'UBU'FUB'",4);
		gen("R'ULU'RU'L'U'LU'L'",4);
		gen("LUL'ULUR'UL'U'R",2);
		System.out.println ("created "+generated.size()+" sequences");
		return generated;
	}

	public static Vector<byte[]> genPLL()
	{
		generated = new Vector<byte[]>();
		genwithfinaltwist("", 1);
		genwithfinaltwist("F'LF'R2FL'F'R2F2", 4);
		genwithfinaltwist("FR'FL2F'RFL2F2", 4);	
		genwithfinaltwist("FR'F'LFRF'L2B'RBLB'R'B", 2);	
		genwithfinaltwist("RB'R'BFR'B'FR'BRF2", 2);	
		genwithfinaltwist("L'R'U2LRFBU2F'B'", 1);	
		genwithfinaltwist("R2U'FB'R2F'BU'R2", 4);	
		genwithfinaltwist("R2UFB'R2F'BUR2", 4);	
		genwithfinaltwist("R'L'U2LUL'U2RU'L", 4);	
		genwithfinaltwist("LRU2R'U'RU2L'UR'", 4);	
		genwithfinaltwist("RUR'U'R'FR2U'R'U'RUR'F'", 4);
		genwithfinaltwist("R'U2RU'F'LFR'F'L'FU'R", 4);	
		genwithfinaltwist("LU2L'UFR'F'LFRF'UL'", 4);	
		genwithfinaltwist("R'URU'R2F'U'FURFR'F'R2", 4);	
		genwithfinaltwist("L'R'U2LRFU'BU2F'UB'", 4);	
		genwithfinaltwist("LRU2L'R'F'UB'U2FU'B", 4);	
		genwithfinaltwist("R'UL'U2RU'LFBU2F'B'", 4);	
		genwithfinaltwist("LU'RU2L'UR'F'B'U2FB", 4);	
		genwithfinaltwist("L'URU'LUL'UR'U'LU2RU2R'", 4);	
		genwithfinaltwist("LU'RU2L'UR'LU'RU2L'UR'", 2);	
		genwithfinaltwist("R'UL'U2RU'LR'UL'U2RU'L", 2);	
		genwithfinaltwist("RUR'B2RU'R'U'B2UB2UB2", 4);	
		return generated;
	}
	
	public static Vector<byte[]> genOLLPLLCombinations()
	{
		Vector<byte[]> a = genOLL();
		Vector<byte[]> b = genPLL();
		Vector<byte[]> c = new Vector<byte[]>();
		for (int i=0; i<a.size(); i++)
		{	for (int j=0; j<b.size(); j++)
			{	c.addElement ( conarray(a.elementAt(i), b.elementAt(j)) );
			}
		}
		return c;
	}
	
	public static void genwithinitialtwist(String algo, int cases)
	{
		int start = generated.size();
		gen(algo, cases);
		int len = generated.size() - start;
		for (int i=0; i<len; i++)
		{	generated.addElement ( conarray(new byte[]{D1}, (byte[])generated.elementAt(start+i)) );
			generated.addElement ( conarray(new byte[]{D2}, (byte[])generated.elementAt(start+i)) );
			generated.addElement ( conarray(new byte[]{D3}, (byte[])generated.elementAt(start+i)) );
		}
	}
	
	public static void genwithfinaltwist(String algo, int cases)
	{
		int start = generated.size();
		gen(algo, cases);
		int len = generated.size() - start;
		for (int i=0; i<len; i++)
		{	generated.addElement ( conarray((byte[])generated.elementAt(start+i), new byte[]{D1}) );
			generated.addElement ( conarray((byte[])generated.elementAt(start+i), new byte[]{D2}) );
			generated.addElement ( conarray((byte[])generated.elementAt(start+i), new byte[]{D3}) );
		}
	}	
	
	
	public static void gen(String algo, int cases)
	{
      int[] buffer = new int[20];
      
      for (int cas=0; cas<cases; cas++)
	  {
     	 boolean reverse = cas>=8;
    	 int rot = cas%8;
    			 
        int blen = 0;
		for (int i=0; i<algo.length(); i++)
		{
			char c = algo.charAt(i);
			char x = (i+1<algo.length()) ? algo.charAt(i+1) : '-';
			if (x=='\'') 
			{	i++;
				if (c=='U') buffer[blen++] = sym(D3,rot);
				if (c=='L') buffer[blen++] = sym(L3,rot);
				if (c=='R') buffer[blen++] = sym(R3,rot);
				if (c=='F') buffer[blen++] = sym(B3,rot);
				if (c=='B') buffer[blen++] = sym(F3,rot);
			}
			else if (x=='2')
			{   i++;
				if (c=='U') buffer[blen++] = sym(D2,rot);
				if (c=='L') buffer[blen++] = sym(L2,rot);
				if (c=='R') buffer[blen++] = sym(R2,rot);
				if (c=='F') buffer[blen++] = sym(B2,rot);
				if (c=='B') buffer[blen++] = sym(F2,rot);
			}
			else 
			{	if (c=='U') buffer[blen++] = sym(D1,rot);
				if (c=='L') buffer[blen++] = sym(L1,rot);
				if (c=='R') buffer[blen++] = sym(R1,rot);
				if (c=='F') buffer[blen++] = sym(B1,rot);
				if (c=='B') buffer[blen++] = sym(F1,rot);
			}
		}
		byte[] a = new byte[blen];
		if (reverse)
		{	for (int i=0; i<blen; i++) 
			{	a[i] = (byte) reverse(buffer[blen-i-1]);
			}
		}
		else 
		{	for (int i=0; i<blen; i++) 
			{	a[i] = (byte) buffer[i];
			}
		}
		generated.addElement(a);
	  }
	}
	
	static int mirror(int turn)
	{
		switch (turn){
		case D1:  return D3;
		case D2:  return D2;
		case D3:  return D1;
		case L1:  return R3;
		case L2:  return R2;
		case L3:  return R1;
		case R1:  return L3;
		case R2:  return L2;
		case R3:  return L1;
		case F1:  return F3;
		case F2:  return F2;
		case F3:  return F1;
		case B1:  return B3;
		case B2:  return B2;
		case B3:  return B1;
		}
		return turn;		
	}
	
	static int sym(int turn, int rot)
	{	if (rot==0) return turn;
		if (rot>=4) return mirror(sym(turn,rot-4));
		if (rot>1) return sym(sym(turn,rot-1),1);
		switch (turn){
		case L1:  return B1;
		case L2:  return B2;
		case L3:  return B3;
		case R1:  return F1;
		case R2:  return F2;
		case R3:  return F3;
		case F1:  return L1;
		case F2:  return L2;
		case F3:  return L3;
		case B1:  return R1;
		case B2:  return R2;
		case B3:  return R3;
		}
		return turn;
	}

	static int reverse(int turn)
	{
		switch (turn){
		case D1:  return D3;
		case D2:  return D2;
		case D3:  return D1;
		case L1:  return L3;
		case L2:  return L2;
		case L3:  return L1;
		case R1:  return R3;
		case R2:  return R2;
		case R3:  return R1;
		case F1:  return F3;
		case F2:  return F2;
		case F3:  return F1;
		case B1:  return B3;
		case B2:  return B2;
		case B3:  return B1;
		}
		return turn;		
	}

	static byte[] conarray(byte[] a, byte[] b)
	{
		byte[] x = new byte[a.length+b.length];
		System.arraycopy (a,0,x,0,a.length);
		System.arraycopy (b,0,x,a.length,b.length);
		return x;
	}
	
	static Vector<byte[]> read(String filename)
	{
		generated = new Vector<byte[]>();
		
		try 
		{
			BufferedReader r = new BufferedReader(new InputStreamReader(
				 new FileInputStream(filename) ));
			String l = r.readLine();
			while (l!=null)
			{
				StringTokenizer t = new StringTokenizer(l);
				int numt = t.countTokens();
				if (numt>=1)
				{	byte[] m = new byte[numt-1];
					t.nextToken();
//					System.out.print ("reading: ");
					for (int i=0; i<m.length; i++)
					{	m[i] = (byte) Integer.parseInt(t.nextToken());
//						System.out.print (" "+m[i]);
					}
//					System.out.println();
					generated.addElement(m);
				}
				
				l = r.readLine();
			}
			r.close();
		}
		catch (IOException e)
		{}

		return generated;
	}

	static Vector<byte[]> readExternal(String filename)
	{
		generated = new Vector<byte[]>();
		gen("", 1);
		
		try 
		{
			BufferedReader r = new BufferedReader(new InputStreamReader(
				 new FileInputStream(filename) ));
			
			String lorig;
			while ((lorig=r.readLine())!=null)
			{
				String l = lorig;
				if (l.indexOf('[')>=0) continue;
				l = l.replaceAll("Slices ","Slices");
				l = l.replace('*',' ');
				l = l.replace('&',' ');
				l = l.replace('²','2');
				
				StringTokenizer t = new StringTokenizer(l.trim());
				int numt = t.countTokens();
				if (numt<5) continue;
				t.nextToken();
				t.nextToken();
				t.nextToken();
				t.nextToken();
				l = t.nextToken("@").trim();
				
				if (l.startsWith("(U')") || l.startsWith("(U2)"))
				{	l = l.substring(4);
				}
				else if (l.startsWith("(U)"))
				{	l = l.substring(3);
				}
				
								
				for (;;)
				{	int idx = l.indexOf(" - ");
					if (idx<0) break;
					l = l.substring(0,idx) + l.substring(idx+3);
				}
				
				for (;;)
				{	int idx = l.indexOf(" {");
					if (idx<0) break;
					int idx2 = l.indexOf(" as ", idx);
					int idx3 = l.indexOf("} ", idx);
					l = l.substring(0, idx) + l.substring(idx+2,idx2) + l.substring(idx3+2);
				}
				
				for (;;)
				{	int idx = l.indexOf(" (");
					if (idx<0) break;
					int idx2 = l.indexOf(") ",idx);
					l = l.substring(0,idx) + l.substring(idx2+2);
				}

				for (;;)
				{	int idx = l.indexOf("(");
					if (idx<0) break;
					l = l.substring(0,idx) + l.substring(idx+1);
				}
				for (;;)
				{	int idx = l.indexOf(")");
					if (idx<0) break;
					l = l.substring(0,idx) + l.substring(idx+1);
				}
				
				if (l.indexOf('D')>=0) continue;
				
				genwithinitialtwist(l,16);
			
				for (int i=generated.size()-4; i>=0 && i<generated.size(); i++)
				{	byte[] b = (byte[]) generated.elementAt(i);
					if (b.length==6 && b[0]==3 && b[1]==4)
					{	System.out.println ("-> "+lorig+"  :  "+l);
					}
				}
				
//				System.out.println(l);
			}				
			r.close();
		}
		catch (IOException e)
		{}

		return generated;
	}

}
