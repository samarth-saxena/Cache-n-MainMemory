import java.util.*;

/**
 * Cache
 */
public class ESP 
{
	/** Main memory */
	static int[][] memory;		

	/** Cache memory */
	static int[][] cache;		
	/** Tag bits */
	static boolean[][] tag;		
	/** Meta bits (valid bit) */
	static boolean[] meta;	

	/** Memory size, cache size */
	static int ms, cs;		
	/** Block size */
	static int bs;
	/** No. of Block lines, Cache lines */
	static int nb, nl;			

	/** No. of Physical address, Cache address bits*/
	static int pa, ca;			
	/** No. of Block Offset bits*/
	static int bo;	
	/** No. of Block index, Cache index bits */			
	static int bi,ci;	
	/** no of Tagbits */		
	static int tb;

	/** Physical address */
	static boolean[] p_adr;
	/** Cache address */
	static boolean[] c_adr;
	
	public static void main(String[] args) 
	{
		Scanner input=new Scanner(System.in);

		ms=input.nextInt();
		cs=input.nextInt();
		bs=input.nextInt();

		nb=ms/bs;
		nl=cs/bs;

		memory=new int[nb][bs];
		cache=new int[nl][bs];

		pa = (int) (Math.log(ms)/Math.log(2));
		ca = (int) (Math.log(cs)/Math.log(2));
		p_adr=new boolean[pa];
		c_adr=new boolean[ca];

		bo= (int) (Math.log(bs)/Math.log(2));

		bi= (int) (Math.log(nb)/Math.log(2));
		ci= (int) (Math.log(nl)/Math.log(2));
		tb=bi-ci;

		tag=new boolean[nl][tb];
		meta=new boolean[nl];

		for (int i = 0, k=0; i < nb; i++) 
		{
			for (int j = 0; j < bs; j++, k++) 
			{
				memory[i][j]=k;
			}
		}

		for (int i = 0; i < nl; i++) 
		{
			meta[i]=false;
		}

		// printall();
		for (int k = 0; k < 4; k++) 
		{
			for (int i = 0; i < pa; i++) 
			{
				p_adr[i]=from01(input.nextInt());
			}
			for (int i = tb, j=0; i < pa; i++, j++) 
			{
				c_adr[j]=p_adr[i];
			}

			directmode(p_adr);
		}
		
		//Printing
		// System.out.println("Physical address");
		// for (int i=0; i<pa; i++) 
		// {
		// 	System.out.print(to01(p_adr[i]) + " ");
		// }
		// System.out.println("\nCache address");
		// for (int i=0; i<ca; i++) 
		// {
		// 	System.out.print(to01(c_adr[i]) + " ");
		// }
		// System.out.println();
		
		
		
		input.close();
		
	}
	
	static boolean from01(int n)
	{
		return n==1?true:false;
	}

	static int to01(boolean n)
	{
		return n==true?1:0;
	}

	static int toint(boolean[] n)
	{
		int res=0;
		for(int i=0; i<n.length; i++)
		{
			res+=to01(n[i])*Math.pow(2, n.length-i-1);
		}
		return res;
	}

	static void printall()
	{
		System.out.println(ms+"\n"+bs+"\n"+nb);
		System.out.println(pa+"\n"+bo+"\n"+bi);
	}


	static void directmode(boolean[] p_adr)
	{
		boolean[] tagbits=new boolean[tb];
		boolean[] cacheindex=new boolean[ci];
		boolean[] blockoffset=new boolean[bo];
		boolean[] blockindex=new boolean[tb+ci];
		// String ci_str="";
		// String bo_str="";
		int i=0;
		for (; i < tb; i++) 
		{
			tagbits[i]=p_adr[i];
			blockindex[i]=p_adr[i];
		}
		for (int j=0; j < ci; i++, j++) 
		{
			cacheindex[j]=p_adr[i];
			blockindex[i]=p_adr[i];
			// ci_str+=to01(cacheindex[j]);
			// System.out.print(to01(cacheindex[j]));
		}
		for(int j=0; j<bo; i++, j++)
		{
			blockoffset[j]=p_adr[i];
		}
		
		// System.out.println(Integer.parseInt(tagbits,2)+" "+Integer.parseInt(cacheindex,2));
				
		boolean result=false;
		// System.out.println(meta[toint(cacheindex)]);
		if(meta[toint(cacheindex)])
		{
			if(Arrays.equals(tag[toint(cacheindex)],tagbits))
			{
				result=true;
			}
		}

		// if(meta[i]==true && tag[i]==tagbits)
		// {
		// 	result=true;
		// 	break;
		// }
		// for(int i=0; i<nl; i++)
		// {
		// 	if(meta[i]==true && tag[i]==tagbits)
		// 	{
		// 		result=true;
		// 		break;
		// 	}
		// }

		// System.out.println(toint(tagbits));
		// System.out.println(toint(cacheindex));
		// System.out.println(toint(blockindex));
		// System.out.println(toint(blockoffset));
		

		if(result)
		{
			System.out.println("\nCACHE HIT");

			System.out.println("\nWord no: " + cache[toint(cacheindex)][toint(blockoffset)]); 
		}
		else
		{
			System.out.println("\nCACHE MISS");

			cache[toint(cacheindex)]=memory[toint(blockindex)];
			meta[toint(cacheindex)]=true;
			tag[toint(cacheindex)]=tagbits;
			// System.out.println(toint(tag[toint(cacheindex)]));
			System.out.println("\nWord no: " + cache[toint(cacheindex)][toint(blockoffset)]); 
		}
	}
}