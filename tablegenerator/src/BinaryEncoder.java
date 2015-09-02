import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.StringTokenizer;
import java.util.Vector;


public class BinaryEncoder 
{
	public static void main(String[] args) throws IOException
	{
		for (int i=0; i<7; i++)
		{	encode("petrus"+i+".txt",  "petrus"+i+".bin");
		}
	}
	
	public static void encode(String source, String dest) throws IOException
	{
		// read algorithms
		AlgorithmComparator comp = new AlgorithmComparator();
		Vector<byte[]> list = new Vector<byte[]>();
		
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(source))); 
		String l = r.readLine();
		while (l!=null)
		{
			StringTokenizer t = new StringTokenizer(l," ");
			int ct = t.countTokens();
			if (ct>=1) 
			{			
				String algtag = t.nextToken();
				int algnr = Integer.parseInt(algtag.substring(1,algtag.length()-1));
	
				byte[] a = new byte[ct-1];
				for (int i=0; i<ct-1; i++)
				{	a[i] = (byte) Integer.parseInt(t.nextToken());
				}
				
				if (list.size()<=algnr)
				{	list.setSize(algnr+1);
				}
				
				byte[] current = list.elementAt(algnr);
				if (current==null || comp.compare(a, current)<0) 
				{	list.setElementAt(a, algnr);
				}
			}
			l=r.readLine();
		}
		r.close();
		
		ByteArrayOutputStream best = new ByteArrayOutputStream();  // the whole file beginning with the index
		ByteArrayOutputStream algos = new ByteArrayOutputStream();  // the algorithm data
		int alglensum=0;
		int algtimesum=0;
		// write all algorithms and delimit with 15
		for (int i=0; i<list.size(); i++)
		{	// add an index entry for every 4th element
			if (i%4==0)
			{	int indexentries = (list.size()+3)/4;
				int target = indexentries*4 + algos.size();
				best.write( (target>>12) & 0xf);
				best.write( (target>>8) & 0xf);
				best.write( (target>>4) & 0xf);
				best.write( (target>>0) & 0xf);
			}
			
			// write algorithm to data block
			byte[] a = list.elementAt(i);
			for (int j=0; j<a.length; j++)
			{	algos.write(a[j]);
			}
			algos.write(15);
			algos.flush();
			alglensum += a.length;
			algtimesum += comp.calctime(a);
		}
		// add algorithm data after the index
		best.write(algos.toByteArray());
		
		// write binary data
		byte[] bin = best.toByteArray();
		FileOutputStream fo = new FileOutputStream(dest);
		for (int i=0; i<bin.length; i+=2)
		{	if (i+1>=bin.length)
			{	fo.write (bin[i]*16);
			}
			else
			{ 	fo.write (bin[i]*16 + bin[i+1]);
			}
		}
		fo.close();
		System.out.println ("generated: "+dest+" for "+list.size()+" algorithms with "+best.size()+" nibbles. "
				+" av.turns: "  + (((double)alglensum)/list.size())
				+" av.time: " + (((double)algtimesum)/list.size())
				);
	}
			
}
