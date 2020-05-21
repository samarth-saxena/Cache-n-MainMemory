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

	static int mode=0;
	static int ss=1;
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
	/** No. of Block index/ Cache index/ Set index bits */			
	static int bi,ci,si;	
	/** no of Tagbits */		
	static int tb;

	/** Physical address */
	static boolean[] p_adr;
	/** Cache address */
	static boolean[] c_adr;
	
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

	static boolean[] tobin(int n)
	{
		boolean[] res;
		int i=0,temp=n,c=0;
		while(temp!=0)
		{
			temp/=2;
			++c;
		}
		res=new boolean[c];
		while(n!=0)
		{
			res[i]=from01(n%2);
			n/=2;
		}
		return res;
	}

	static void printall()
	{
		System.out.println(ms+"\n"+bs+"\n"+nb);
		System.out.println(pa+"\n"+bo+"\n"+bi);
	}

	static boolean iscachefull()
	{
		boolean f=true;
		for(int i=0; i<nl; i++)
		{
			if(!meta[i])
			{
				f=false;
				break;
			}
		}
		return f;
	}
	
	static int nextemptycache()
	{
		int pos=-1;
		for(int i=0; i<nl; i++)
		{
			if(!meta[i])
			{
				pos=i;
				break;
			}
		}
		return pos;
	}

	static boolean issetfull(int start, int end)
	{
		boolean f=true;
		for(int i=start; i<end; i++)
		{
			if(!meta[i])
			{
				f=false;
				break;
			}
		}
		return f;
	}

	static int nextemptyset(int start, int end)
	{
		int pos=start;
		for(int i=start; i<end; i++)
		{
			if(!meta[i])
			{
				pos=i;
				break;
			}
		}
		return pos;
	}

	static void directmode(boolean[] p_adr)
	{
		tb=bi-ci;
		
		boolean[] tagbits=new boolean[tb];
		boolean[] cacheindex=new boolean[ci];
		boolean[] blockoffset=new boolean[bo];
		boolean[] blockindex=new boolean[tb+ci];

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
		}
		for(int j=0; j<bo; i++, j++)
		{
			blockoffset[j]=p_adr[i];
		}
		
				
		boolean result=false;
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

			System.out.println("\nWord no: " + cache[toint(cacheindex)][toint(blockoffset)]); 
		}
	}

	static void associative(boolean[] p_adr)
	{
		tb=bi;

		boolean[] tagbits=new boolean[tb];
		boolean[] cacheindex=new boolean[ci];
		boolean[] blockoffset=new boolean[bo];
		// boolean[] blockindex=new boolean[tb+ci];

		Random rand = new Random();

		int i;
		for (i=0; i < tb; i++) 
		{
			tagbits[i]=p_adr[i];
		}
		
		for(int j=0; j<bo; i++, j++)
		{
			blockoffset[j]=p_adr[i];
		}
		
				
		boolean result=false;
		
		// System.out.println("\ntagbits");
		// for (int j3 = 0; j3 < tb; j3++) 
		// {
		// 	System.out.print(to01(tagbits[j3]) + " ");
		// }
		
		for(int j=0; j<nl; j++)
		{
			// System.out.println("\ntagarray");
			// for (int j2 = 0; j2 < tb; j2++) 
			// {
			// 	System.out.print(to01(tag[j][j2]) + " ");
			// }
			
			if(meta[j]==true && Arrays.equals(tag[j],tagbits))
			{
				
				result=true;
				// for(int x=0; x<ci; x++)
				// {
				// 	cacheindex[x]=tobin(j)[x];
				// }
				cacheindex=tobin(j);

				break;
			}
		}

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
			int pos;
			
			if(iscachefull())
			{
				pos=rand.nextInt(nl);
			}
			else
			{
				pos=nextemptycache();
			}


			for(int x=0; x<bs; x++)
			{
				cache[pos][x]=memory[toint(tagbits)][x];
			}
			
			meta[pos]=true;

			for(int x=0; x<tb; x++)
			{
				tag[pos][x]=tagbits[x];
			}

			System.out.println("\npos= " + pos);
			System.out.println("\nWord no: " + cache[pos][toint(blockoffset)]); 
		}
	}

	static void setassociative(boolean[] p_adr)
	{
		boolean[] tagbits=new boolean[tb];
		boolean[] setindex=new boolean[si];
		boolean[] blockoffset=new boolean[bo];
		int cacheindex=-1;

		Random rand = new Random();
		
		int i=0;
		for (; i < tb; i++) 
		{
			tagbits[i]=p_adr[i];
		}
		for (int j=0; j < si; i++, j++) 
		{
			setindex[j]=p_adr[i];
		}
		for(int j=0; j<bo; i++, j++)
		{
			blockoffset[j]=p_adr[i];
		}
			
		boolean result=false;

		int start=toint(setindex)*ss;
		int end=(toint(setindex)+1)*ss;
		
		// System.out.println("\ntagbits");
		// for (int j3 = 0; j3 < tb; j3++) 
		// {
		// 	System.out.print(to01(tagbits[j3]) + " ");
		// }
		
		for(int j=toint(setindex)*ss; j<(toint(setindex)+1)*ss; j++)
		{
			// System.out.println("\ntagarray");
			// for (int j2 = 0; j2 < tb; j2++) 
			// {
			// 	System.out.print(to01(tag[j][j2]) + " ");
			// }
			
			if(meta[j]==true && Arrays.equals(tag[j],tagbits))
			{
				
				result=true;
				cacheindex=j;

				break;
			}
		}

		// System.out.println(toint(tagbits));
		// System.out.println(toint(cacheindex));
		// System.out.println(toint(blockindex));
		// System.out.println(toint(blockoffset));
		

		if(result)
		{
			System.out.println("\nCACHE HIT");

			System.out.println("\nWord no: " + cache[cacheindex][toint(blockoffset)]); 
		}
		else
		{
			System.out.println("\nCACHE MISS");
			int pos;
			
			if(issetfull(start, end))
			{
				pos=start+rand.nextInt(end-start);
			}
			else
			{
				pos=nextemptyset(start, end);
			}


			for(int x=0; x<bs; x++)
			{
				cache[pos][x]=memory[toint(tagbits)][x];
			}
			
			meta[pos]=true;

			for(int x=0; x<tb; x++)
			{
				tag[pos][x]=tagbits[x];
			}

			System.out.println("\npos= " + pos);
			System.out.println("\nWord no: " + cache[pos][toint(blockoffset)]); 
		}

	}

	public static void main(String[] args) 
	{
		Scanner input=new Scanner(System.in);

		ms=input.nextInt();
		cs=input.nextInt();
		bs=input.nextInt();
		mode=input.nextInt();
		
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

		switch (mode) 
		{
			case 1:
				tb=bi-ci;
				break;
			
			case 2:
				tb=bi;
				break;
			
			case 3:
				ss=input.nextInt();
				int ns=nl/ss;
				si= (int) (Math.log(ns)/Math.log(2));
				tb=bi-si;
				break;

			default:
				System.out.println("Invalid mode.");
				break;
		}
		
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

		outer:
		for (int k = 0; k < 6; k++) 
		{
			for (int i = 0; i < pa; i++) 
			{
				p_adr[i]=from01(input.nextInt());
			}

			switch (mode) 
			{
				case 1:
					directmode(p_adr);
					break;
				
				case 2:
					associative(p_adr);
					break;
				
				case 3:
					setassociative(p_adr);
					break;

				default:
					System.out.println("An error ocurred.");
					break outer;
			}
			
			// System.out.println("\nTag / Meta");
			// for(int j=0; j<nl; j++)
			// {
			// 	for (int j2 = 0; j2 < tb; j2++) 
			// 	{
			// 		System.out.print(to01(tag[j][j2]) + " ");
			// 	}
			// 	System.out.println("  " + to01(meta[j]));
			// }
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

}