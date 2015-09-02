import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;


public class AlgorithmGenerator 
{

	
	// -------------------- basic cube twisting engine ---------------------
	
	// all cubies and slots are named by the curresponding sides they are in.
	// the sides are: U(p), D(own),  L(eft), R(right),  F(ront), B(ack)	
	// for easier computation, they are numbered in the inverse order in which 
	// they are solved
	
	static final byte CUBIE_ULF = 7;
	static final byte CUBIE_URF = 6;
	static final byte CUBIE_URB = 5;
	static final byte CUBIE_ULB = 4; 
	static final byte CUBIE_DLB = 3;
	static final byte CUBIE_DRB = 2;
	static final byte CUBIE_DRF = 1;	
	static final byte CUBIE_DLF = 0;
	
	static final byte CUBIE_UL = 11;  
	static final byte CUBIE_UF = 10;  
	static final byte CUBIE_LF = 9;
	static final byte CUBIE_RF = 8;
	static final byte CUBIE_UR = 7;
	static final byte CUBIE_RB = 6;
	static final byte CUBIE_UB = 5;
	static final byte CUBIE_LB = 4;
	static final byte CUBIE_DB = 3;
	static final byte CUBIE_DR = 2;
	static final byte CUBIE_DF = 1;
	static final byte CUBIE_DL = 0;
	
	// enumeration of possible turns 
	static final byte D1  = 0;   // quarter turn clockwise
	static final byte D2  = 1;   // half turn
	static final byte D3  = 2;   // quarter turn anti-clockwise
	static final byte L1  = 3;   // quarter turn clockwise
	static final byte L2  = 4;   // half turn
	static final byte L3  = 5;   // quarter turn anti-clockwise
	static final byte R1  = 6;   // quarter turn clockwise
	static final byte R2  = 7;   // half turn
	static final byte R3  = 8;   // quarter turn anti-clockwise
	static final byte F1  = 9;   // quarter turn clockwise
	static final byte F2  = 10;  // half turn
	static final byte F3  = 11;  // quarter turn anti-clockwise
	static final byte B1  = 12;  // quarter turn clockwise
	static final byte B2  = 13;  // half turn
	static final byte B3  = 14;  // quarter turn anti-clockwise

	// inverse turn of every turn
	static final byte inverse[] =  { D3,D2,D1, L3,L2,L1, R3,R2,R1, F3,F2,F1, B3,B2,B1 };
	
	// static data holding the current state of the cube
	// each of this slots holds the identifier of the cubie located there plus the rotation info
	// (in one byte)
	static byte slot_ulf;  // 1. cycle
	static byte slot_urb;
	static byte slot_dlb;
	static byte slot_drf;
	static byte slot_ulb;  // 2. cycle
	static byte slot_urf;
	static byte slot_dlf;
	static byte slot_drb;
	static byte slot_ul;   // 1. cycle
	static byte slot_ur;
	static byte slot_dl;
	static byte slot_dr;
	static byte slot_uf;   // 2. cylce
	static byte slot_ub;
	static byte slot_db;
	static byte slot_df;
	static byte slot_lf;   // 3. cycle
	static byte slot_lb;
	static byte slot_rb;
	static byte slot_rf;
	
	
	static int[][] ll_perm_groups;
	static int[][] ll_perm_initialtwist;
	static int[][] ll_perm_sequencesym;
	
	static void reset()
	{
		slot_ulf = CUBIE_ULF;
		slot_ulb = CUBIE_ULB;
		slot_urf = CUBIE_URF; 
		slot_urb = CUBIE_URB;
		slot_dlf = CUBIE_DLF;
		slot_dlb = CUBIE_DLB;
		slot_drf = CUBIE_DRF;
		slot_drb = CUBIE_DRB;
		slot_ul = CUBIE_UL;
		slot_ub = CUBIE_UB;
		slot_ur = CUBIE_UR;
		slot_uf = CUBIE_UF;
		slot_lf = CUBIE_LF;
		slot_lb = CUBIE_LB;
		slot_rb = CUBIE_RB;
		slot_rf = CUBIE_RF;
		slot_dl = CUBIE_DL;
		slot_db = CUBIE_DB;
		slot_dr = CUBIE_DR;
		slot_df = CUBIE_DF;
	}

	static byte FLIP(byte edgecubie)
	{
		return (byte) (edgecubie ^ 0x10);
	}
	static byte CW(byte cornercubie)
	{
		return (byte) ((cornercubie +  0x10) % 0x30);
	}
	static byte CCW(byte cornercubie)
	{
		return (byte) ((cornercubie +  0x20) % 0x30);
	}
	
	static void turn(byte t)
	{
		byte x;
		
		switch (t)
		{
		// turning down face does flip edges, but no rotate corners
		case D1:
			x = slot_drb;  slot_drb = slot_drf;  slot_drf = slot_dlf;  slot_dlf = slot_dlb;  slot_dlb = x;
		    x = slot_df;  slot_df = FLIP(slot_dl);  slot_dl = FLIP(slot_db);  slot_db = FLIP(slot_dr);  slot_dr = FLIP(x);
            break;
		case D2:
			x = slot_drf;  slot_drf = slot_dlb;  slot_dlb = x;
			x = slot_dlf;  slot_dlf = slot_drb;  slot_drb = x;
            x = slot_dl;  slot_dl = slot_dr;  slot_dr = x;
            x = slot_df;  slot_df = slot_db;  slot_db = x;
			break;
		case D3:
			x = slot_drf;  slot_drf = slot_drb;  slot_drb = slot_dlb;  slot_dlb = slot_dlf;  slot_dlf = x;
		    x = slot_df;  slot_df = FLIP(slot_dr);  slot_dr = FLIP(slot_db);  slot_db = FLIP(slot_dl);  slot_dl = FLIP(x);
            break;

        // turning left,right face rotates corners, but not flips edges 
		case L1:
            x = slot_ulf;  slot_ulf = CW(slot_ulb);  slot_ulb = CCW(slot_dlb);  slot_dlb = CW(slot_dlf);  slot_dlf = CCW(x);
			x = slot_lf;  slot_lf = slot_ul;  slot_ul = slot_lb;  slot_lb = slot_dl;  slot_dl = x;
            break;
		case L2:
			x = slot_dlf;  slot_dlf = slot_ulb;  slot_ulb = x;
			x = slot_dlb;  slot_dlb = slot_ulf;  slot_ulf = x;
            x = slot_dl;  slot_dl = slot_ul;  slot_ul = x;
            x = slot_lf;  slot_lf = slot_lb;  slot_lb = x;
			break;
		case L3:
            x = slot_ulb;  slot_ulb = CCW(slot_ulf);  slot_ulf = CW(slot_dlf);  slot_dlf = CCW(slot_dlb);  slot_dlb = CW(x);
			x = slot_lf;  slot_lf = slot_dl;  slot_dl = slot_lb;  slot_lb = slot_ul;  slot_ul = x;
            break;
		case R1:
            x = slot_urb;  slot_urb = CW(slot_urf);  slot_urf = CCW(slot_drf);  slot_drf = CW(slot_drb);  slot_drb = CCW(x);
			x = slot_rf;  slot_rf = slot_dr;  slot_dr = slot_rb;  slot_rb = slot_ur;  slot_ur = x;
            break;
		case R2:
			x = slot_drf;  slot_drf = slot_urb;  slot_urb = x;
			x = slot_drb;  slot_drb = slot_urf;  slot_urf = x;
            x = slot_dr;  slot_dr = slot_ur;  slot_ur = x;
            x = slot_rf;  slot_rf = slot_rb;  slot_rb = x;
			break;
		case R3:
            x = slot_urf;  slot_urf = CCW(slot_urb);  slot_urb = CW(slot_drb);  slot_drb = CCW(slot_drf);  slot_drf = CW(x);
			x = slot_rf;  slot_rf = slot_ur;  slot_ur = slot_rb;  slot_rb = slot_dr;  slot_dr = x;
            break;

        // turning front,back faces rotates corners, but not flip edges
		case F1:
            x = slot_urf;  slot_urf = CW(slot_ulf); slot_ulf = CCW(slot_dlf);  slot_dlf = CW(slot_drf);  slot_drf = CCW(x);
            x = slot_lf;  slot_lf = slot_df;  slot_df = slot_rf;  slot_rf = slot_uf;  slot_uf = x;
            break;
		case F2:
			x = slot_dlf;  slot_dlf = slot_urf;  slot_urf = x;
			x = slot_ulf;  slot_ulf = slot_drf;  slot_drf = x;
            x = slot_df;  slot_df = slot_uf;  slot_uf = x;
            x = slot_rf;  slot_rf = slot_lf;  slot_lf = x;
			break;
		case F3:
            x = slot_ulf;  slot_ulf = CCW(slot_urf);  slot_urf = CW(slot_drf);  slot_drf = CCW(slot_dlf);  slot_dlf = CW(x);
            x = slot_lf;  slot_lf = slot_uf;  slot_uf = slot_rf;  slot_rf = slot_df;  slot_df = x;
            break;
		case B1:
            x = slot_ulb;  slot_ulb = CW(slot_urb);  slot_urb = CCW(slot_drb);  slot_drb = CW(slot_dlb);  slot_dlb = CCW(x);
            x = slot_lb;  slot_lb = slot_ub;  slot_ub = slot_rb;  slot_rb = slot_db;  slot_db = x;
            break;
		case B2:
			x = slot_dlb;  slot_dlb = slot_urb;  slot_urb = x;
			x = slot_ulb;  slot_ulb = slot_drb;  slot_drb = x;
            x = slot_db;  slot_db = slot_ub;  slot_ub = x;
            x = slot_rb;  slot_rb = slot_lb;  slot_lb = x;
			break;
		case B3:
            x = slot_urb;  slot_urb = CCW(slot_ulb); slot_ulb = CW(slot_dlb);  slot_dlb = CCW(slot_drb);  slot_drb = CW(x);
            x = slot_lb;  slot_lb = slot_db;  slot_db = slot_rb;  slot_rb = slot_ub;  slot_ub = x;
            break;
		}
	}

	// position of the cubies normalized in a way to allow direct 
	// computation of situation fingerprint
	
	static int edgecubieposition(int cubie)
	{
		return edgecubiepositionandflip(cubie)/2;
	}

	static int edgecubiepositionandflip(int cubie)
	{
		// half cross
		if ((slot_ul&0x0f) == cubie) return 22 + (slot_ul>>4);
		if ((slot_uf&0x0f) == cubie) return 20 + (slot_uf>>4);
		// 2x2x2
		if ((slot_lf&0x0f) == cubie) return 18 + (slot_lf>>4);
		// 2x2x3
		if ((slot_rf&0x0f) == cubie) return 16 + (slot_rf>>4);
		if ((slot_ur&0x0f) == cubie) return 14 + (slot_ur>>4);
		// + 3 cubies
		if ((slot_rb&0x0f) == cubie) return 12 + (slot_rb>>4);
		if ((slot_ub&0x0f) == cubie) return 10 + (slot_ub>>4);
		// f2l
		if ((slot_lb&0x0f) == cubie) return 8 + (slot_lb>>4);
		// 3. layer
		if ((slot_db&0x0f) == cubie) return 6 + (slot_db>>4);
		if ((slot_dr&0x0f) == cubie) return 4 + (slot_dr>>4);
		if ((slot_df&0x0f) == cubie) return 2 + (slot_df>>4);
		if ((slot_dl&0x0f) == cubie) return 0 + (slot_dl>>4);
		return 0;
	}

	static int cornercubieposition(int cubie)
	{	
		return cornercubiepositionandtwist(cubie) / 3;
	}
	
	static int cornercubiepositionandtwist(int cubie)
	{
		// half cross - no corner
		// 2x2x2
		if ((slot_ulf&0x0f) == cubie) return 21 + (slot_ulf>>4);
		// 2x2x3
		if ((slot_urf&0x0f) == cubie) return 18 + (slot_urf>>4);
		// + 3 cubies
		if ((slot_urb&0x0f) == cubie) return 15 + (slot_urb>>4);
		// f2l
		if ((slot_ulb&0x0f) == cubie) return 12 + (slot_ulb>>4);
		// 2. layer
		if ((slot_dlb&0x0f) == cubie) return 9 + (slot_dlb>>4);
		if ((slot_drb&0x0f) == cubie) return 6 + (slot_drb>>4);
		if ((slot_drf&0x0f) == cubie) return 3 + (slot_drf>>4);
		if ((slot_dlf&0x0f) == cubie) return 0 + (slot_dlf>>4);
		return 0;
	}
	
	static int avoid(int block, int posandflip)
	{	
		if (posandflip>=block) return posandflip-2;
		return posandflip;
	}

	static int perm2index(int i0, int i1)
	{
		if (i1==1) return 0;
		else       return 1;
	}
	
	static int perm2index(int i0, int i1, int i2)
	{
		if (i2==2)
		{	return perm2index(i0,i1);
		}
		else if (i1==2)
		{	return 3-perm2index(i0,i2);
		}
		else  // i0==2
		{   return 4+perm2index(i1,i2);
		}		
	}
	
	static int perm2index(int i0, int i1, int i2, int i3)
	{
		if (i3==3)
		{	return perm2index(i0,i1,i2);
		}
		else if (i2==3)
		{   return 11-perm2index(i0,i1,i3);
		}
		else if (i1==3)
		{   return 12+perm2index(i0,i2,i3);
		}
		else   // i0==3
		{	return 23-perm2index(i1,i2,i3);
		}
	}
	
	static int[] index2perm(int i)
	{
		for (int a=0; a<4; a++)
		{	for (int b=0; b<4; b++)
			{	if (b==a) continue;
				for (int c=0; c<4; c++)
				{	if (c==a || c==b) continue;
					for (int d=0; d<4; d++)
					{	if (d==a || d==b || d==c) continue;
						if (perm2index(a,b,c,d)==i) 
						{	return new int[]{a,b,c,d};
						}
					}
				}
			}
		}
		return null;
	}
	
	static int compute_fingerprint(int phase, int rot)
	{
		int epos;
    // ----- PETRUS SOLUTION ----
		switch (phase)
		{
		case 0:
			// half cross
			epos = edgecubiepositionandflip(CUBIE_UL);
			return epos                                                   // E: 24 positions
				 + avoid(epos,edgecubiepositionandflip(CUBIE_UF))*24;     // E: 22 positions
		case 1:
			// check if half cross is compete
			if (slot_ul!=CUBIE_UL || slot_uf!=CUBIE_UF)
			{	return -1;
			}
			// complete the 2x2x2
			return edgecubiepositionandflip(CUBIE_LF)                    // E: 20 positions
				 + cornercubiepositionandtwist(CUBIE_ULF)*20;            // C: 24 positions
		case 2:
			// check if 2x2x2 is complete
			if (slot_ul!=CUBIE_UL || slot_uf!=CUBIE_UF || slot_lf!=CUBIE_LF || slot_ulf != CUBIE_ULF)
			{	return -1;
			}
			// extend to 2x2x3
			epos = edgecubiepositionandflip(CUBIE_RF);
			return  epos                                                 // E: 18 positions
				  + avoid(epos,edgecubiepositionandflip(CUBIE_UR))*18    // E: 16 positions
				  + cornercubiepositionandtwist(CUBIE_URF)*18*16;        // C: 21 positions
		case 3:
			// check if 2x2x3 is complete
			if (slot_ul!=CUBIE_UL || slot_uf!=CUBIE_UF || slot_lf!=CUBIE_LF || slot_ulf != CUBIE_ULF
			||  slot_rf!=CUBIE_RF || slot_ur!=CUBIE_UR || slot_urf!=CUBIE_URF)
			{	return -1;
			}
			// collect 3 more cubies for back side
			epos = edgecubiepositionandflip(CUBIE_RB);    
			return  epos                                                 // E: 14 positions
				  + avoid(epos,edgecubiepositionandflip(CUBIE_UB))*14    // E: 12 positions
				  + cornercubiepositionandtwist(CUBIE_URB)*14*12;        // C: 18 positions
		case 4:
			// check if 2x2x3 + 3 cubies is complete
			if (slot_ul!=CUBIE_UL || slot_uf!=CUBIE_UF || slot_lf!=CUBIE_LF || slot_ulf != CUBIE_ULF
			||  slot_rf!=CUBIE_RF || slot_ur!=CUBIE_UR || slot_urf!=CUBIE_URF
			||  slot_rb!=CUBIE_RB || slot_ub!=CUBIE_UB || slot_urb!=CUBIE_URB)
			{	return -1;
			}
			// collect the missing 2 cubies for the first 2 layers
			return edgecubiepositionandflip(CUBIE_LB)                    // E: 10 positions
				 + cornercubiepositionandtwist(CUBIE_ULB)*10;            // C: 15 positions
				
		case 5:
			// check if first 2 layers are intact
			if (slot_ul!=CUBIE_UL || slot_uf!=CUBIE_UF || slot_lf!=CUBIE_LF || slot_ulf != CUBIE_ULF
			||  slot_rf!=CUBIE_RF || slot_ur!=CUBIE_UR || slot_urf!=CUBIE_URF
			||  slot_rb!=CUBIE_RB || slot_ub!=CUBIE_UB || slot_urb!=CUBIE_URB
			||  slot_lb!=CUBIE_LB || slot_ulb!=CUBIE_ULB)
			{	return -1;
			}
			// prepare the last layer full solve by correctly positioning down face
			{
				int eidx = perm2index(slot_dl&0xf,slot_df&0xf,slot_dr&0xf,slot_db&0xf);
				int cidx = perm2index(slot_dlf&0xf,slot_drf&0xf,slot_drb&0xf,slot_dlb&0xf);
				return ll_perm_initialtwist[eidx][cidx];
			}
		case 6:
			// check if first 2 layers are intact
			if (slot_ul!=CUBIE_UL || slot_uf!=CUBIE_UF || slot_lf!=CUBIE_LF || slot_ulf != CUBIE_ULF
			||  slot_rf!=CUBIE_RF || slot_ur!=CUBIE_UR || slot_urf!=CUBIE_URF
			||  slot_rb!=CUBIE_RB || slot_ub!=CUBIE_UB || slot_urb!=CUBIE_URB
			||  slot_lb!=CUBIE_LB || slot_ulb!=CUBIE_ULB)
			{	return -1;
			}
			
			{
				// check if this is is a solvable member of the last layer group
				int eidx = perm2index(slot_dl&0xf,slot_df&0xf,slot_dr&0xf,slot_db&0xf);
				int cidx = perm2index(slot_dlf&0xf,slot_drf&0xf,slot_drb&0xf,slot_dlb&0xf);
				if (ll_perm_initialtwist[eidx][cidx]!=0) return -1;
				if (ll_perm_sequencesym[eidx][cidx]!=0) return -1;
				
				int g = ll_perm_groups[eidx][cidx];
				if (g<0) return -1;
				
				eidx = (slot_dl>>4) + (slot_df>>4)*2 + (slot_dr>>4)*4; // + (slot_db>>4);
				cidx = (slot_dlf>>4) + (slot_drf>>4)*3 + (slot_drb>>4)*9; // + (slot_dlb>>4);
				
				return g + eidx*15 + cidx*15*8;
			}
		}
		
		return -1;
	}
	
	
	static byte[] patternfound;
	static int patterncounter;
	
	static String check_algorithm(int phase, byte[] combination)
	{	
		int idx = compute_fingerprint(phase,0);
		if (idx<0) return "algorithm not reaching target";

		// for any step, check if new pattern is reached
		boolean havealready = patternfound[idx]>=0;
		if (havealready && patternfound[idx]<combination.length) return "pattern "+idx+" already found with shorter solution";
		
			StringBuffer b = new StringBuffer();			
			b.append ("[");
			b.append (idx);
			b.append ("]");
			
			// write algorithm in the order that is necessary to bring cube back to solved state
			for (int i=combination.length-1; i>=0; i--)
			{	b.append(" ");
				b.append(inverse[combination[i]]);
			}
			
			try 
			{
				PrintStream ps = new PrintStream(new FileOutputStream(
						"petrus"+phase+".txt", patterncounter!=0
				));
				ps.println(b.toString());
				ps.close();
			} catch (IOException e) {}
			
//			if (patterncounter==7776) System.out.print("-finished");
			if (!havealready)
			{	patternfound[idx] = (byte) combination.length;
				patterncounter++;
			}

			System.out.println((patterncounter)+": "+b.toString() +  (havealready?" +":"") );
			
		return null;
	}	

	
	static boolean nextcombination(byte[] combination)
	{
		// find furthest point in combination that can still be increased
		for (int i=combination.length-1; i>=0; i--)
		{	
			if (combination[i]<14)
			{	
				// undo the turns up to the change point
				for (int j=combination.length-1; j>=i; j--)
				{	turn (inverse[combination[j]]);
				}
				
				// increase change point and clear all afterwards
				combination[i]++;
				for (int j=i+1; j<combination.length; j++) 
				{	combination[j]=0;
				}
									
				// redo the turns from the change point	
				for (int j=i; j<combination.length; j++)
				{	turn (combination[j]);
				}
					
				return true;  // it was possible to switch to next combination
			}
		}
		return false;   // no more combinations left
	}
	
	static void findshortestcombinations(int phase, int totalpatterns)
	{
		patternfound = new byte[1000000];
		for (int i=0; i<patternfound.length; i++)
		{	patternfound[i]=-1;
		}
		patterncounter = 0;
		
		// search for increasing combination length
		for (int len = 0;  len<1000 && patterncounter<totalpatterns; len++)
		{	System.out.println("phase:"+phase+ " length: "+len);
			reset();
			
			// start with first combination of given length
			byte[] combination = new byte[len];
			// move cube to this state
			for (int i=0; i<len; i++) 
			{	turn (combination[i]);
			}
			
			// try all possible combinations of this size
			do
			{
				// check if have reached a new unique position of the start possibilities
				check_algorithm(phase, combination);
				// continue as long as a new combination can be found
			} while (nextcombination(combination));
		}
	}	
	
	
	static void recomputealgorithms(int phase, Vector<byte[]> forwardalgos)
	{
		AlgorithmComparator comp = new AlgorithmComparator();
		Collections.sort(forwardalgos, comp);
//for (int i=0; i<forwardalgos.size() && i<1000; i++)
//{	System.out.println("IN: "+parray(forwardalgos.elementAt(i))+"  "+comp.calctime(forwardalgos.elementAt(i)));
//}
		patternfound = new byte[1000000];
		for (int i=0; i<patternfound.length; i++)
		{	patternfound[i]=-1;
		}
		patterncounter = 0;
		
		for (int i=0; i<forwardalgos.size(); i++)
		{	
			byte[] forward = forwardalgos.elementAt(i);
			byte[] combination = new byte[forward.length];
			
			reset();
			for (int t=0; t<combination.length; t++)
			{	combination[t] = inverse[forward[forward.length-1-t]];
				turn (combination[t]);
			}
			String err = check_algorithm(phase, combination);
	
			if (err!=null)
			{	if (err.indexOf("already")>=0) continue;
				if (err.indexOf("target")>=0) continue; // System.exit(1);
				System.out.print (err);
				System.out.println(parray(forward));
			}
		}
		System.out.println("imported "+forwardalgos.size()+" sequences");
	}

	
	private static int rot(int permindex, int rotations)
	{
		int[] p = index2perm(permindex);
		return perm2index( p[(4-rotations)%4],
				           p[(5-rotations)%4],
				           p[(6-rotations)%4],
				           p[(7-rotations)%4]);
	}
	private static int sym(int permindex, int rotations)
	{
		if (rotations<=0)
		{   return permindex;
		}
		int[] p = index2perm(permindex);
		return perm2index( (p[(4-rotations)%4]+rotations)%4,
				           (p[(5-rotations)%4]+rotations)%4,
				           (p[(6-rotations)%4]+rotations)%4,
				           (p[(7-rotations)%4]+rotations)%4);
	}
	private static int mirroredges(int permindex)
	{	
		int[] x = { 2,1,0,3 };
		int[] p = index2perm(permindex);		
		return perm2index(x[p[2]], x[p[1]], x[p[0]], x[p[3]]);
	}
	private static int mirrorcorners(int permindex)
	{
		int[] p = index2perm(permindex);
		return perm2index(p[1]^1, p[0]^1, p[3]^1, p[2]^1);
	}
	private static void build_ll_perm_groups()
	{
		ll_perm_groups = new int[24][24];
		for (int i=0; i<24; i++)
		{	for (int j=0; j<24; j++)
			{	ll_perm_groups[i][j] = -1;
			}
		}
		ll_perm_initialtwist = new int[24][24];
		ll_perm_sequencesym = new int[24][24];
		
		int groupnumber=0;		
		for (int ep=0; ep<24; ep++)
		{	l1: for (int cp=0; cp<24; cp++)
			{	if (ep%2 != cp%2) continue;		// permutation parity must match
				
				// check if there is a symmetric permutation already 
				for (int i=0; i<4; i++)	
				{	for (int j=0; j<8; j++)
					{	int sep = rot(ep,i);
						int scp = rot(cp,i);
						if (j>=4)
						{	sep = mirroredges(sep);
							scp = mirrorcorners(scp);
						}
						sep = sym(sep,j%4);
						scp = sym(scp,j%4);
						
						if (ll_perm_groups[sep][scp]>=0
						&&  ll_perm_initialtwist[sep][scp]==0
						&&  ll_perm_sequencesym[sep][scp]==0)
						{	ll_perm_groups[ep][cp] = ll_perm_groups[sep][scp];
							ll_perm_initialtwist[ep][cp] = i;
							ll_perm_sequencesym[ep][cp] = j;
							continue l1;
						}
					}
				}
				
				// no symmetric pattern found - must add this one
				ll_perm_groups[ep][cp] = groupnumber;
				ll_perm_initialtwist[ep][cp] = 0;
				ll_perm_sequencesym[ep][cp] = 0;
				groupnumber++;
			}
		}
	
/*		
		for (int ep=0; ep<24; ep++)
		{	for (int cp=0; cp<24; cp++)
			{	if (ll_perm_groups[ep][cp]<0) continue;
				System.out.print(ep+" "+cp+" "+parray(index2perm(ep))+" "+parray(index2perm(cp))+"  "
			       + ll_perm_groups[ep][cp] + " " + ll_perm_initialtwist[ep][cp] + " "+ ll_perm_sequencesym[ep][cp]
			    );
				int tw = ll_perm_initialtwist[ep][cp];
				System.out.println ("  init-twist -> "+ll_perm_initialtwist[rot(ep,tw)][rot(cp,tw)]);
			}
		}
		System.out.println ("number of groups: "+groupnumber);
*/
	}
	
	// -- some extra toolbox methods --
	
	private static String parray(int a[])
	{
		StringBuffer b = new StringBuffer();
		for (int i=0; i<a.length; i++)
		{	b.append(a[i]);
	    }
		return b.toString();
	}

	private static String parray(byte a[])
	{
		StringBuffer b = new StringBuffer();
		for (int i=0; i<a.length; i++)
		{	if (i!=0) b.append(" ");
			b.append(a[i]);
	    }
		return b.toString();
	}
	
	// ---------------------------- main and test --------------------------------------

	static String index2str(int permindex)
	{
		return parray(index2perm(permindex));
	}
	
	
	
	public static void main(String[] args)
	{
 		build_ll_perm_groups();

//  		for (int ep=0; ep<24; ep++)
//		{	for (int cp=0; cp<24; cp++)
//			{	if (cp%2 != ep%2) continue;
//				System.out.print(ll_perm_sequencesym[ep][cp]+",");
//			}
//			System.out.println();
//		}
		
		
//		findshortestcombinations(0, 528);
//		findshortestcombinations(1, 480);
//		findshortestcombinations(2, 6048);
//		findshortestcombinations(3, 3024);
//		findshortestcombinations(4, 150);

// 		findshortestcombinations(5, 4);

		Vector<byte[]> alg = new Vector<byte[]>(); 
		alg.addAll( AlgorithmTranslator.readExternal("lastlayerfullsove.txt") );
		alg.addAll( AlgorithmTranslator.genOLLPLLCombinations() );
		recomputealgorithms(6, alg);
		
	}
}

