import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;


public class CubeSolver 
{
	
	// -------------------- basic cube twisting engine ---------------------
	
	// all cubies and slots are named by the curresponding sides they are in.
	// the sides are: U(p), D(own),  L(eft), R(right),  F(ront), B(ack)	
	static final byte CUBIE_ULF = 0;  // 1. cycle
	static final byte CUBIE_URB = 1;
	static final byte CUBIE_DLB = 2;
	static final byte CUBIE_DRF = 3;	
	static final byte CUBIE_ULB = 4;  // 2. cycle
	static final byte CUBIE_URF = 5;
	static final byte CUBIE_DLF = 6;
	static final byte CUBIE_DRB = 7;
	static final byte CUBIE_UL = 0;   // 1. cycle
	static final byte CUBIE_UR = 1;
	static final byte CUBIE_DR = 2;
	static final byte CUBIE_DL = 3;
	static final byte CUBIE_UF = 4;   // 2. cycle
	static final byte CUBIE_UB = 5;
	static final byte CUBIE_DB = 6;
	static final byte CUBIE_DF = 7;
	static final byte CUBIE_LF = 8;   // 3. cycle
	static final byte CUBIE_LB = 9;
	static final byte CUBIE_RB = 10;
	static final byte CUBIE_RF = 11;
	
	
	// enumeration of possible turns 
	static final byte D1  = 0;  // quarter turn clockwise
	static final byte D3  = 1;  // quarter turn anti-clockwise
	static final byte D2  = 2; // half turn
	static final byte L1  = 3;  // quarter turn clockwise
	static final byte L3  = 4;  // quarter turn anti-clockwise
	static final byte R1  = 5;  // quarter turn clockwise
	static final byte R3  = 6;  // quarter turn anti-clockwise
	static final byte F1  = 7;  // quarter turn clockwise
	static final byte F3  = 8;  // quarter turn anti-clockwise
	static final byte B1  = 9; // quarter turn clockwise
	static final byte B3  = 10; // quarter turn anti-clockwise
	static final byte U1  = 11;  // quarter turn clockwise
	static final byte U3  = 12;  // quarter turn anti-clockwise
	static final byte U2  = 13; // half turn
	static final byte L2  = 14; // half turn
	static final byte R2  = 15; // half turn
	static final byte F2  = 16; // half turn
	static final byte B2  = 17; // half turn
	// inverse turn of every turn
	static final byte inverse[] =  { D3,D1,D2, L3,L1, R3,R1, F3,F1, B3,B1, U3,U1,U2,L2,R2,F2,B2 };
	
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
	static byte slot_lf;
	static byte slot_lb;
	static byte slot_rb;
	static byte slot_rf;

	// static table to check if cube is in G3 
	final static String totalcornertwistmatrix = 
			 "012345104523325401543210"
		    +"103254015432234510452301"
			+"240513421305503124315042"
			+"425031243150051342130524"
			+"351402530214412035204153"
			+"534120352041140253021435"
            +"103254015432234510452301"
			+"012345104523325401543210"
            +"351402530214412035204153"
            +"534120352041140253021435"
            +"240513421305503124315042"
            +"425031243150051342130524"
            +"425031243150051342130524"
            +"240513421305503124315042"
            +"534120352041140253021435"
            +"351402530214412035204153"
            +"012345104523325401543210"
            +"103254015432234510452301"
            +"534120352041140253021435"
            +"351402530214412035204153"
            +"425031243150051342130524"
            +"240513421305503124315042"
            +"103254015432234510452301"
            +"012345104523325401543210";
	
	final static byte[] sym0 = { D1,D3,D2, L1,L3,R1,R3,F1,F3,B1,B3,U1,U3,  U2,L2,R2,F2,B2 }; // original orientation
	final static byte[] sym1 = { D1,D3,D2, B1,B3,F1,F3,L1,L3,R1,R3,U1,U3,  U2,B2,F2,L2,R2 }; // turn cube around U-axis clockwise
//	final static byte[] sym2 = { B1,B3,F1,F3,L1,L3,R1,R3,U1,U3,D1,D3,B2,F2,L2,R2,U2,D2 }; // turn cube around L-axis clockwise
	final static byte[] sym3 = { D3,D1,D2, L3,L1,R3,R1,B3,B1,F3,F1,U3,U1,  U2,L2,R2,B2,F2 }; // mirror  from back to forward
	
	static byte[][] symmetric_turns;
	
	
	
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
		// turning up or down faces flips edges and rotates corners
		case U1:
			x = slot_urf;  slot_urf = CW(slot_urb);  slot_urb = CCW(slot_ulb);  slot_ulb = CW(slot_ulf);  slot_ulf = CCW(x);
		    x = slot_uf;  slot_uf = FLIP(slot_ur);  slot_ur = FLIP(slot_ub);  slot_ub = FLIP(slot_ul);  slot_ul = FLIP(x);
            break;
		case U3:
			x = slot_urb;  slot_urb = CCW(slot_urf); slot_urf = CW(slot_ulf);  slot_ulf = CCW(slot_ulb);  slot_ulb = CW(x);  
		    x = slot_uf;  slot_uf = FLIP(slot_ul);  slot_ul = FLIP(slot_ub);  slot_ub = FLIP(slot_ur);  slot_ur = FLIP(x);
            break;
		case D1:
			x = slot_drb;  slot_drb = CW(slot_drf);  slot_drf = CCW(slot_dlf);  slot_dlf = CW(slot_dlb);  slot_dlb = CCW(x);
		    x = slot_df;  slot_df = FLIP(slot_dl);  slot_dl = FLIP(slot_db);  slot_db = FLIP(slot_dr);  slot_dr = FLIP(x);
            break;
		case D3:
			x = slot_drf;  slot_drf = CCW(slot_drb);  slot_drb = CW(slot_dlb);  slot_dlb = CCW(slot_dlf);  slot_dlf = CW(x);
		    x = slot_df;  slot_df = FLIP(slot_dr);  slot_dr = FLIP(slot_db);  slot_db = FLIP(slot_dl);  slot_dl = FLIP(x);
            break;

        // turning left,right rotates corners, but not flips edges 
		case L1:
            x = slot_ulf;  slot_ulf = CW(slot_ulb);  slot_ulb = CCW(slot_dlb);  slot_dlb = CW(slot_dlf);  slot_dlf = CCW(x);
			x = slot_lf;  slot_lf = slot_ul;  slot_ul = slot_lb;  slot_lb = slot_dl;  slot_dl = x;
            break;
		case L3:
            x = slot_ulb;  slot_ulb = CCW(slot_ulf);  slot_ulf = CW(slot_dlf);  slot_dlf = CCW(slot_dlb);  slot_dlb = CW(x);
			x = slot_lf;  slot_lf = slot_dl;  slot_dl = slot_lb;  slot_lb = slot_ul;  slot_ul = x;
            break;
		case R1:
            x = slot_urb;  slot_urb = CW(slot_urf);  slot_urf = CCW(slot_drf);  slot_drf = CW(slot_drb);  slot_drb = CCW(x);
			x = slot_rf;  slot_rf = slot_dr;  slot_dr = slot_rb;  slot_rb = slot_ur;  slot_ur = x;
            break;
		case R3:
            x = slot_urf;  slot_urf = CCW(slot_urb);  slot_urb = CW(slot_drb);  slot_drb = CCW(slot_drf);  slot_drf = CW(x);
			x = slot_rf;  slot_rf = slot_ur;  slot_ur = slot_rb;  slot_rb = slot_dr;  slot_dr = x;
            break;

        // turning front,back faces does not rotate corners and not flip edges
		case F1:
            x = slot_ulf;  slot_ulf = slot_dlf;  slot_dlf = slot_drf;  slot_drf = slot_urf;  slot_urf = x;
            x = slot_lf;  slot_lf = slot_df;  slot_df = slot_rf;  slot_rf = slot_uf;  slot_uf = x;
            break;
		case F3:
            x = slot_ulf;  slot_ulf = slot_urf;  slot_urf = slot_drf;  slot_drf = slot_dlf;  slot_dlf = x;
            x = slot_lf;  slot_lf = slot_uf;  slot_uf = slot_rf;  slot_rf = slot_df;  slot_df = x;
            break;
		case B1:
            x = slot_ulb;  slot_ulb = slot_urb;  slot_urb = slot_drb;  slot_drb = slot_dlb;  slot_dlb = x;
            x = slot_lb;  slot_lb = slot_ub;  slot_ub = slot_rb;  slot_rb = slot_db;  slot_db = x;
            break;
		case B3:
            x = slot_ulb;  slot_ulb = slot_dlb;  slot_dlb = slot_drb;  slot_drb = slot_urb;  slot_urb = x;
            x = slot_lb;  slot_lb = slot_db;  slot_db = slot_rb;  slot_rb = slot_ub;  slot_ub = x;
            break;

		// double turns never change flip or twists
		case U2:
			x = slot_urf;  slot_urf = slot_ulb;  slot_ulb = x;
			x = slot_ulf;  slot_ulf = slot_urb;  slot_urb = x;
            x = slot_ul;  slot_ul = slot_ur;  slot_ur = x;
            x = slot_uf;  slot_uf = slot_ub;  slot_ub = x;
			break;
		case D2:
			x = slot_drf;  slot_drf = slot_dlb;  slot_dlb = x;
			x = slot_dlf;  slot_dlf = slot_drb;  slot_drb = x;
            x = slot_dl;  slot_dl = slot_dr;  slot_dr = x;
            x = slot_df;  slot_df = slot_db;  slot_db = x;
			break;
		case L2:
			x = slot_ulf;  slot_ulf = slot_dlb;  slot_dlb = x;
			x = slot_dlf;  slot_dlf = slot_ulb;  slot_ulb = x;
            x = slot_dl;  slot_dl = slot_ul;  slot_ul = x;
            x = slot_lf;  slot_lf = slot_lb;  slot_lb = x;
            break;
		case R2:
			x = slot_urf;  slot_urf = slot_drb;  slot_drb = x;
			x = slot_drf;  slot_drf = slot_urb;  slot_urb = x;
            x = slot_dr;  slot_dr = slot_ur;  slot_ur = x;
            x = slot_rf;  slot_rf = slot_rb;  slot_rb = x;
            break;
		case F2:
			x = slot_urf;  slot_urf = slot_dlf;  slot_dlf = x;
			x = slot_ulf;  slot_ulf = slot_drf;  slot_drf = x;
            x = slot_rf;  slot_rf = slot_lf;  slot_lf = x;
            x = slot_uf;  slot_uf = slot_df;  slot_df = x;
            break;
		case B2:
			x = slot_urb;  slot_urb = slot_dlb;  slot_dlb = x;
			x = slot_ulb;  slot_ulb = slot_drb;  slot_drb = x;
            x = slot_rb;  slot_rb = slot_lb;  slot_lb = x;
            x = slot_ub;  slot_ub = slot_db;  slot_db = x;
            break;
		}
	}
	
	static int compressturns(int a, int b)
	{	
		int sidea = a<12 ? a/2 : a-12;
		int sideb = b<12 ? b/2 : b-12;
		int twista = a<12 ? 1+(a%2)*2 : 2;
		int twistb = b<12 ? 1+(b%2)*2 : 2;
		if (sidea!=sideb) return -2;  // not same side
		int twist = (twista + twistb) % 4;
		if (twist==0) return -1;	// cancel out
		if (twist==1) return 2*sidea;  // clockwise turn
		if (twist==2) return 12+sidea; // double turn
		if (twist==3) return 2*sidea+1; // counterclockwise turn
		return 0;
	}
		
	static void checkconsistency()
	{
		int flips =  slot_ul ^ slot_ub ^ slot_ur ^ slot_uf ^ slot_lf ^ slot_lb 
			   ^ slot_rb ^ slot_rf ^ slot_dl ^ slot_db ^ slot_dr ^ slot_df; 	
		if ((flips & 0x10) != 0)
		{	System.out.println ("Edge flip violation");
		}
		
	    int twists = (slot_ulf & 0x30)
	    		  +  (slot_ulb & 0x30)
	    		  +  (slot_urf & 0x30) 
	    		  +  (slot_urb & 0x30)
	    		  +  (slot_dlf & 0x30)
	    		  +  (slot_dlb & 0x30)
	    		  +  (slot_drf & 0x30)
	    		  +  (slot_drb & 0x30);
	    if (twists % 0x30 != 0)
	    {	System.out.println ("Corner twist violation");
	    }
	}


	
	// -------------- try possibilities to find a target configuration --------------

	
	static boolean isG4()
	{
		if (slot_ul != CUBIE_UL
		 || slot_ub != CUBIE_UB
		 || slot_ur != CUBIE_UR
		 || slot_uf != CUBIE_UF
		 || slot_lf != CUBIE_LF
		 || slot_lb != CUBIE_LB
		 || slot_rb != CUBIE_RB
		 || slot_rf != CUBIE_RF
		 || slot_dl != CUBIE_DL
	 	 || slot_db != CUBIE_DB
		 || slot_dr != CUBIE_DR
		 || slot_df != CUBIE_DF) return false; 		

		if (slot_ulf != CUBIE_ULF
		 || slot_ulb != CUBIE_ULB
		 || slot_urf != CUBIE_URF 
		 || slot_urb != CUBIE_URB
		 || slot_dlf != CUBIE_DLF
		 || slot_dlb != CUBIE_DLB
		 || slot_drf != CUBIE_DRF
		 || slot_drb != CUBIE_DRB)  return false;

		return true;
	}	
	
	static boolean isG3()
	{	
		// if (!isG2()) return false;
	
		// put all 2. cycle edges into 2. cycle
		if (slot_ub<4 || slot_ub>=8) return false;
		if (slot_uf<4 || slot_uf>=8) return false;
		if (slot_db<4 || slot_db>=8) return false;
		if (slot_df<4 || slot_df>=8) return false;
		
   	     // put all corners into their correct cycle
		if (slot_ulf>=4) return false;
		if (slot_urb>=4) return false;
		if (slot_dlb>=4) return false;
		if (slot_drf>=4) return false;
		
		// check total corner twist (also constraints corner permutation parity)
		int p1 = permutationindex(slot_ulf, slot_urb, slot_dlb, slot_drf);
		int p2 = permutationindex(slot_ulb-4, slot_urf-4, slot_dlf-4, slot_drb-4);
		if (totalcornertwistmatrix.charAt(24*p1+p2)!='0') return false;
		
		return true;
	}
		
	static boolean isG2()
	{
		// if (!isG1()) return false;
		
		// put all 1.cycle edges in 1.cycle 
		if (slot_ul>=4) return false;
		if (slot_dl>=4) return false;
		if (slot_dr>=4) return false;
		if (slot_ur>=4) return false;

		// correct all corner twists
		int corners = slot_ulf | slot_ulb | slot_urf | slot_urb 
		            | slot_dlf | slot_dlb | slot_drf | slot_drb;
		if ((corners & 0x30) !=0) return false;	
		
		return true;
	}

	static boolean isG2a()
	{
		// if (!isG1()) return false;
		
		// put all 1.cycle edges in 1.cycle 
		if (slot_ul>=4) return false;
		if (slot_dl>=4) return false;
		if (slot_dr>=4) return false;
		if (slot_ur>=4) return false;
		
		return true;
	}

	static boolean isG1()
	{
		// correct all edge flips
		int edges = slot_ul | slot_ub | slot_ur | slot_uf | slot_lf | slot_lb 
		         | slot_rb | slot_rf | slot_dl | slot_db | slot_dr | slot_df; 		
		if ((edges & 0x10) != 0) return false;
	    return true;
	}

	
	

	static boolean ispetrusphase0()
	{
		return slot_ul==CUBIE_UL;
	}
	static boolean ispetrusphase1()
	{
		return slot_ul==CUBIE_UL && slot_uf==CUBIE_UF && slot_lf==CUBIE_LF && slot_ulf==CUBIE_ULF;
	}
	static boolean ispetrusphase2()
	{
		return slot_rf==CUBIE_RF && slot_ur==CUBIE_UR && slot_urf==CUBIE_URF && ispetrusphase1();
	}
	static boolean ispetrusphase3()
	{	
		return slot_ulb==CUBIE_ULB && slot_lb==CUBIE_LB && slot_ub==CUBIE_UB && ispetrusphase2();
	}
	static boolean ispetrusphase4()
	{	
		return slot_rb==CUBIE_RB && slot_urb==CUBIE_URB && ispetrusphase3();
	}
	static boolean ispetrusphase5()
	{	
		return ispetrusphase4()
		&& (slot_dr==CUBIE_DR || slot_dr==CUBIE_DL || slot_dr==CUBIE_DF+0x10 || slot_dr==CUBIE_DB+0x10 )
		&& (slot_dl==CUBIE_DR || slot_dl==CUBIE_DL || slot_dl==CUBIE_DF+0x10 || slot_dl==CUBIE_DB+0x10 )
		&& (slot_df==CUBIE_DF || slot_df==CUBIE_DB || slot_df==CUBIE_DR+0x10 || slot_df==CUBIE_DL+0x10 )
		&& (slot_db==CUBIE_DF || slot_db==CUBIE_DB || slot_db==CUBIE_DR+0x10 || slot_db==CUBIE_DL+0x10 )	
		&& (slot_dlf==CUBIE_DLF || slot_dlf==CUBIE_DRB || slot_dlf==CUBIE_DLB+0x10 || slot_dlf==CUBIE_DRF+0x10 )
		&& (slot_drb==CUBIE_DLF || slot_drb==CUBIE_DRB || slot_drb==CUBIE_DLB+0x10 || slot_drb==CUBIE_DRF+0x10 )
		&& (slot_dlb==CUBIE_DLB || slot_dlb==CUBIE_DRF || slot_dlb==CUBIE_DLF+0x20 || slot_dlb==CUBIE_DRB+0x20 )
		&& (slot_drf==CUBIE_DLB || slot_drf==CUBIE_DRF || slot_drf==CUBIE_DLF+0x20 || slot_drf==CUBIE_DRB+0x20 )
		;
	}
	
	static boolean ispetrusphase6()
	{	
		return ispetrusphase4()
		&& slot_dr==CUBIE_DR && slot_dl==CUBIE_DL && slot_df==CUBIE_DF && slot_db==CUBIE_DB
		&& slot_dlf==CUBIE_DLF && slot_dlb==CUBIE_DLB && slot_drf==CUBIE_DRF && slot_drb==CUBIE_DRB;
	}
		
	static boolean istargetreached(int target)
	{
//		switch (target)
//		{
//		case 0:  return isG1();
////		case 1:  return isG2();
//		case 1:  return isG2a();
//		case 2:  return isG2();
//		case 3:  return isG3();
//		default: return isG4();
//		}
		switch (target)
		{	
		case 0: return ispetrusphase0();
		case 1: return ispetrusphase1();
		case 2: return ispetrusphase2();
		case 3: return ispetrusphase3();
		case 4: return ispetrusphase4();
		case 5: return ispetrusphase5();
		case 6: return ispetrusphase6();
		default: return false;
		}
	}
	
	
	static int permutationindex(int a, int b, int c, int d)
	{
		switch (a)
		{
		case 0:
			switch (b) 
			{
			case 1:	 return (c==2) ? 0 : 1;
			case 2:  return (c==1) ? 2 : 3;
			case 3:  return (c==1) ? 4 : 5;
			}
		case 1:
			switch (b) 
			{
			case 0:	 return (c==2) ? 6 : 7;
			case 2:  return (c==0) ? 8 : 9;
			case 3:  return (c==0) ? 10 : 11;
			}
		case 2:
			switch (b) 
			{
			case 0:	 return (c==1) ? 12 : 13;
			case 1:  return (c==0) ? 14 : 15;
			case 3:  return (c==0) ? 16 : 17;
			}
		case 3:
			switch (b) 
			{
			case 0:	 return (c==1) ? 18 : 19;
			case 1:  return (c==0) ? 20 : 21;
			case 2:  return (c==0) ? 22 : 23;
			}
		}
		return 0;
	}

	
	// ---------------------------- main and test --------------------------------------
	
	static Random rnd;
	
	static void mix(byte[] allowedturns)
	{
		reset();
		for (int i=100+rnd.nextInt(10); i>=0; i--) 
		{	turn(allowedturns[rnd.nextInt(allowedturns.length)]);
		}
	}
	

	static void build_symmetrics_turns() 
	{	
		byte[] l = sym1;
		symmetric_turns = new byte[][] {
			sym0,  con(sym0,l), con(sym0,l,l), con(sym0,l,l,l),
			sym3,  con(sym3,l), con(sym3,l,l), con(sym3,l,l,l)
		};
	};
	static byte[] con(byte[] a, byte[] b)
	{
		byte[] c = new byte[18];
		for (int i=0; i<18; i++)
		{	c[i] = a[b[i]];
		}
		return c;
	}
	static byte[] con(byte[] a, byte[] b, byte[] c)
	{	
		return con(con(a,b),c);
	}
	static byte[] con(byte[] a, byte[] b, byte[] c, byte[] d)
	{	
		return con(con(con(a,b),c),d);
	}
	
	public final static void main(String[] args) throws IOException
	{
		build_symmetrics_turns();
		rnd = new Random(3623);
		solvertest();
//		grouptest();
//		consistencytest();
//		tablegeneration();
	}
	
	static void solvertest() throws IOException
	{	
		int phases = 7;

		// load precomputed algorithms
		byte[][][] algorithmtables = new byte[phases][][];
		for (int i=0; i<phases; i++)
		{	algorithmtables[i] = readalgorithmtablebinary("phase"+i+".bin");
		}
		
		Vector<Integer> solution = new Vector<Integer>();
		int sumturns=0;
		
		int tries = 10000;
		
		for (int t=0; t<tries; t++)
		{
			System.out.print (t+"- ");
			
			reset();
			mix(new byte[] { U1,  D1,  L1,  R1,  F1,  B1 } );
			solution.clear();
	
			dophases: for (int target=0; target<phases; target++)
			{
				// try to use one of the algorithms from the table
				for (int i=0; i<algorithmtables[target].length; i++)
				{	
					byte[] a = algorithmtables[target][i];
					int len = a.length;
	//				System.out.println("trying algo: "+i+" of length: "+len);

					for (int j=len-1; j>=0; j--)
					{	turn(inverse[a[j]]); // do the algorithm in forward
					}
					if (istargetreached(target)) 
					{
						System.out.print((len+"!"));
						for (int j=len-1; j>=0; j--) 
						{	solution.addElement(Integer.valueOf(inverse[a[j]]));
						}							
						continue dophases; // when reached the next step do no longer search
					}
					for (int j=0; j<len; j++) 
					{	turn(a[j]); // undo the algorithm
					}
				}
				// if no algorithm found - terminate process with error
				System.out.println ("Error - no algorithm usable");
				System.out.println ("Corners: "+slot_ulf+","+slot_urb+","+slot_dlb+","+slot_drf
						                   +" "+slot_ulb+","+slot_urf+","+slot_dlf+","+slot_drb);
				System.out.println ("Edges: "+slot_ul+","+slot_ur+","+slot_dr+","+slot_dl
						                 +" "+slot_uf+","+slot_ub+","+slot_db+","+slot_df
						                 +" "+slot_lf+","+slot_lb+","+slot_rb+","+slot_rf);					
				return;
			}			

//			System.out.print("="+solution.size());
//			// compress by combining when matching twists are found
//			int before;
//			do 
//			{	before = solution.size();
//				for (int i=0; i<solution.size()-1; i++)			
//				{	int comp = compressturns(solution.elementAt(i).intValue(), solution.elementAt(i+1).intValue());
//					if (comp>=0)	// twist can be joined to one
//					{	solution.setElementAt(new Integer(comp), i);
//						solution.removeElementAt(i+1);
//					}
//					else if (comp==-1)	// both twist cancel themselves out
//					{	solution.removeElementAt(i+1);
//						solution.removeElementAt(i);
//					}
//				}
//			} while (solution.size()<before);
			
			// print full solution and memorize length
			System.out.println("->"+solution.size()+"   "+solution);
			sumturns += solution.size();
		}
		System.out.println ("Average turns: "+ (sumturns*1.0)/tries);
	}
	
	
	static byte[][] readalgorithmtable(String filename)
	{
		Vector<byte[]> table = new Vector<byte[]>();

		try
		{
			BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(filename))); 
			String l = r.readLine();
			while (l!=null)
			{
				l=l.trim();
				if (l.length()>0)
				{
					
					int comment = l.indexOf("//");
					if (comment>=0) l = l.substring(0,comment).trim();
					
					StringTokenizer t = new StringTokenizer(l," ");
					int ct = t.countTokens();
					if (ct>=0)
					{	byte[] a = new byte[ct];
						for (int i=0; i<ct; i++)
						{	a[i] = (byte) Integer.parseInt(t.nextToken());
						}
						table.addElement(a);
					}
				}
				l = r.readLine();
			}	
			r.close();
		} 
		catch (Exception e)
		{	e.printStackTrace();
			System.exit(0);
		}
		
		byte[][] t2 = new byte[table.size()][];
		for (int i=0; i<t2.length; i++)
		{	t2[i] = (byte[]) table.elementAt(i);
		//	for (int j=0; j<t2[i].length; j++) System.out.print(t2[i][j]+" ");
		//	System.out.println();
		}
		System.out.println("table loaded: "+t2.length);
		return t2;
	}

	
	static int bitbuffer;
	static int bitsinbuffer;
	static FileInputStream fin;
	
	static byte[][] readalgorithmtablebinary(String filename) throws IOException
	{
		build_symmetrics_turns();
		
		
		Vector<byte[]> table = new Vector<byte[]>();
		table.addElement(new byte[0]); // non-action path is never stored in file
		
		bitsinbuffer = 0;
		fin = new FileInputStream(filename);
		
		
		for (;;)
		{	int num = (int) readnum();
			if (num<=0) break;
			int len = (int) readnum();
			
			byte[] path = new byte[len];
			for (int i=0; i<len; i++) path[i] = 0;
			
			for (int i=0; i<num; i++)
			{
				int same = readnum();
				for (int j=same; j<len; j++)
				{	path[j] = (byte) readnum();
				}

				// add all symmetric algorithms also
				for (int syms=0; syms<symmetric_turns.length; syms++)
				{	byte[] pcopy = new byte[len];
					for (int j=0; j<len; j++)
					{	pcopy[j] = symmetric_turns[syms][path[j]];
					}
					table.addElement(pcopy);					
				}
			}
		} 

		fin.close();
		
		byte[][] t2 = new byte[table.size()][];
		for (int i=0; i<t2.length; i++)
		{	t2[i] = (byte[]) table.elementAt(i);
		}
		System.out.println("table loaded: "+t2.length);
		return t2;
	}
		
	static int readnum() throws IOException
	{
		int x = readbits(4);
		if (x<15) return x;
		return readbits(32);
	}
	
	static int readbits(int num) throws IOException
	{
		int b=0;
		int didread=0;

		while (didread<num)
		{	if (bitsinbuffer<=0)
			{	bitbuffer = fin.read();
				if (bitbuffer<0) throw new IOException ("unexpected end if file");
				bitbuffer = bitbuffer & 0xff;
				bitsinbuffer=8;
			}
	
			int need = num-didread;
			if (need <= bitsinbuffer)
			{	int mask = ~ (0x00ff << need);
				b |= ((bitbuffer>>(8-bitsinbuffer)) & mask) << didread; 
				bitsinbuffer -= need;
				return b;
			}
			else
			{	b |= (bitbuffer>>(8-bitsinbuffer)) << didread;
				didread += bitsinbuffer;
				bitsinbuffer = 0;				
			}
		}
		return b;
	}
	
	static int readbit() throws IOException
	{	
		if (bitsinbuffer<=0)
		{	bitbuffer = fin.read();
			if (bitbuffer<0) throw new IOException ("unexpected end if file");
			bitbuffer = bitbuffer & 0xff;
			bitsinbuffer=8;
//			System.out.println ("["+bitbuffer+"]");
		}
		int bit = (bitbuffer >> (8-bitsinbuffer)) & 1; 
		bitsinbuffer--;
		return bit;
	}
	
}
	