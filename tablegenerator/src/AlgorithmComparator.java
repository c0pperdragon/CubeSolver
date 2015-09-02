import java.util.Comparator;

class AlgorithmComparator implements Comparator<byte[]>
{
	static final int[] timings = {
		481, 572, 481,
		755, 1390, 755,
		755, 1390, 755,
		755, 1390, 755,
		755, 1390, 755,		
	};
	
	
	public int compare(byte[] a, byte[] b)
	{	
		if (a==null && b==null) return 0;
		if (a==null) return -1;
		if (b==null) return 1;
		if (a.length!=b.length) 
		{	return a.length - b.length;
		}
		int t1 = calctime(a);
		int t2 = calctime(b);
		if (t1!=t2)
		{	return t1-t2;
		}
		for (int i=0; i<a.length; i++)
		{	if (a[i]!=b[i]) 
			{	return a[i]-b[i];
			}
		}
		return 0;
	}
	public boolean equals(Object obj)
	{
		return obj!=null && obj instanceof AlgorithmComparator;
	}
	
	public int calctime(byte[] a)
	{	
		int sum = 0;
		for (int i=0; i<a.length; i++)
		{	sum += timings[a[i]];
		}
		return sum;
	}
	
	
}	
