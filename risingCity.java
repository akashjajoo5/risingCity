import java.io.*;
import java.util.*;

//Creates a constructor to store the building number, executed time and total time of the buildings.
class Building{
	public int bNo;
	public int etime;
	public int ttime;
	public Building(int bNo,int etime,int ttime){
		this.bNo=bNo;
		this.etime=etime;
		this.ttime=ttime;
	}
}

//This class has a constructor of rising city which calls the buildings constructor
//and assigns a specific position to every building structure and stores it a Building array called Heap.
//It also, defines the maximum size of the heap and the current size of the heap.
//The top of the heap is always the 1st index of the array,
//the 0th index of the array stores a garbage value as this makes the calling of other functions of minheap easier.
public class risingCity {

	Building[] Heap;
	int size;
	int maximum;
	final int FRONT = 1;

	risingCity(int maximum)
	{
		this.maximum = maximum;
		this.size = 0;
		Heap = new Building[maximum+1];
		Heap[0] = new Building(Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE);
	}

	//RBT implementation
	int R = 0;
	int B = 1;

	//RBT class is a constructor which defines the colour, left, right and parent of a node.
	//It also makes an element of Building structure which is accessed by point by reference.
	class RBT {

			Building elem;
			int colour = B;
			RBT l = empty, r = empty, uppernode = empty;

			RBT(Building elem) {
					this.elem = elem;
			}
	}

	RBT empty = new RBT(new Building(-1,-1,-1));
	RBT rt = empty;

//It finds a node which is to be found while performing any operation of the Red Black Tree.
// It recursively calls RBT find to find the exact node.
//It iterates the left subtree if the node to be found is less than the root.
//It iterates the right subtree if the node to be found is more then the root.
//It returns the root if the current is equal to the node to be found.
	RBT find(RBT find, RBT current) {
			if (rt == empty) {
					return null;
			}

			if (find.elem.bNo < current.elem.bNo) {
					if (current.l != empty) {
							return find(find, current.l);
					}
			} else if (find.elem.bNo > current.elem.bNo) {
					if (current.r != empty) {
							return find(find, current.r);
					}
			} else if (find.elem.bNo == current.elem.bNo) {
					return current;
			}
			return null;
	}

//It inserts the new node in the Red Black Tree.
//It inserts the new node as a red node and recursively calls the same function to find the perfect position in the RBT to put the node.
//It calls the fixinsertion function to fix the rank of the nodes.
	void insertrb(RBT current) {
			RBT temp = rt;
			if (rt == empty) {
					rt = current;
					current.colour = B;
					current.uppernode = empty;
			} else {
					current.colour = R;
					while (true) {
							if (current.elem.bNo < temp.elem.bNo) {
									if (temp.l == empty) {
											temp.l = current;
											current.uppernode = temp;
											break;
									} else {
											temp = temp.l;
									}
							} else if (current.elem.bNo >= temp.elem.bNo) {
									if (temp.r == empty) {
											temp.r = current;
											current.uppernode = temp;
											break;
									} else {
											temp = temp.r;
									}
							}
					}
					fixinsertion(current);
			}
	}

	//Takes as argument the newly inserted node.
	//It fixes the ranks of the nodes when a new node is inserted of any node is deleted from the RBT.
	//It calls rotate right or rotate left according to the conditions.
	void fixinsertion(RBT current) {
			while (current.uppernode.colour == R) {
					RBT aunt = empty;
					if (current.uppernode == current.uppernode.uppernode.l) {
							aunt = current.uppernode.uppernode.r;

							if (aunt != empty && aunt.colour == R) {
									current.uppernode.colour = B;
									aunt.colour = B;
									current.uppernode.uppernode.colour = R;
									current = current.uppernode.uppernode;
									continue;
							}
							if (current == current.uppernode.r) {
									current = current.uppernode;
									rL(current);
							}
							current.uppernode.colour = B;
							current.uppernode.uppernode.colour = R;
							rR(current.uppernode.uppernode);
					} else {
							aunt = current.uppernode.uppernode.l;
							 if (aunt != empty && aunt.colour == R) {
									current.uppernode.colour = B;
									aunt.colour = B;
									current.uppernode.uppernode.colour = R;
									current = current.uppernode.uppernode;
									continue;
							}
							if (current == current.uppernode.l) {
									//Double rotation needed
									current = current.uppernode;
									rR(current);
							}
							current.uppernode.colour = B;
							current.uppernode.uppernode.colour = R;
							rL(current.uppernode.uppernode);
					}
			}
			rt.colour = B;
	}

	void rL(RBT current) {                      								//Rotate current node in left direction
			if (current.uppernode != empty) {
					if (current == current.uppernode.l) {
							current.uppernode.l = current.r;
					} else {
							current.uppernode.r = current.r;
					}
					current.r.uppernode = current.uppernode;
					current.uppernode = current.r;
					if (current.r.l != empty) {
							current.r.l.uppernode = current;
					}
					current.r = current.r.l;
					current.uppernode.l = current;
			} else {																							//Need to rotate root
					RBT r = rt.r;
					rt.r = r.l;
					r.l.uppernode = rt;
					rt.uppernode = r;
					r.l = rt;
					r.uppernode = empty;
					rt = r;
			}
	}

	void rR(RBT current) { 																						//Rotate current node in right direction
			if (current.uppernode != empty) {
					if (current == current.uppernode.l) {
							current.uppernode.l = current.l;
					} else {
							current.uppernode.r = current.l;
					}

					current.l.uppernode = current.uppernode;
					current.uppernode = current.l;
					if (current.l.r != empty) {
							current.l.r.uppernode = current;
					}
					current.l = current.l.r;
					current.uppernode.r = current;
			} else {																												//Need to rotate rt
					RBT l = rt.l;
					rt.l = rt.l.r;
					l.r.uppernode = rt;
					rt.uppernode = l;
					l.r = rt;
					l.uppernode = empty;
					rt = l;
			}
	}

	void transplant(RBT target, RBT w){                      //It connects the tree when a node is deleted and two separate trees are formed.
				if(target.uppernode == empty){
						rt = w;
				}else if(target == target.uppernode.l){
						target.uppernode.l = w;
				}else
						target.uppernode.r = w;
				w.uppernode = target.uppernode;
	}

//When a print command(range) comes, this function is invoked which in turn calls
//the recurprint function and returns the output string to the main class.
	String printRange(int b1,int b2)
	{
		RBT cur=rt;
		String s="";
		while(cur!=empty){
			if(cur.elem.bNo>=b1 && cur.elem.bNo<=b2){
				return(recurPrint(cur,b1,b2));
			}
			else if(cur.elem.bNo<b1){
				cur=cur.r;
			}
			else{
				cur=cur.l;
			}
		}
		return s;
	}

//It recursively finds the nodes which are in the range of the print command and appends them in a string and returns it.
	String recurPrint(RBT cur,int b1,int b2){
		if(cur == empty) return "";
		if(cur.elem.bNo == b1){
			return("("+cur.elem.bNo+","+ cur.elem.etime+","+ cur.elem.ttime+"),"+recurPrint(cur.r,b1,b2));
		}
		else if(cur.elem.bNo==b2){
			return(recurPrint(cur.l,b1,b2)+"("+cur.elem.bNo+","+ cur.elem.etime+","+ cur.elem.ttime+"),");
		}
		else
		{
			String str="";
			if(cur.l!= null && inRange(cur.l.elem.bNo, b1,b2))
				str += recurPrint(cur.l,b1,b2);

			if(inRange(cur.elem.bNo,b1,b2))
				str += "("+cur.elem.bNo+","+ cur.elem.etime+","+ cur.elem.ttime+"),";

			if(cur.r!= null && inRange(cur.r.elem.bNo,b1,b2))
				str += recurPrint(cur.r,b1,b2);
			return str;
		}
	}

//It checks if the node is in the range.
	boolean inRange(int target,int l,int h){
		if(target>=l && target<=h)
			return true;
		return false;
	}

//It deletes the node from the tree and calls the transplant function to find the parent of the child of deleted node.
//It takes the node to be deleted as a parameter.
	boolean deleterb(RBT t3){
			if((t3 = find(t3, rt))==null)return false;
			RBT t1;
			RBT t2 = t3;
			int t2_original_colour = t2.colour;

			if(t3.l == empty){
					t1 = t3.r;
					transplant(t3, t3.r);
			}else if(t3.r == empty){
					t1 = t3.l;
					transplant(t3, t3.l);
			}else{
					t2 = treeMinimum(t3.r);
					t2_original_colour = t2.colour;
					t1 = t2.r;
					if(t2.uppernode == t3)
							t1.uppernode = t2;
					else{
							transplant(t2, t2.r);
							t2.r = t3.r;
							t2.r.uppernode = t2;
					}
					transplant(t3, t2);
					t2.l = t3.l;
					t2.l.uppernode = t2;
					t2.colour = t3.colour;
			}
			if(t2_original_colour==B)
					deleteFixup(t1);
			return true;
	}

//It fixes the ranks of the nodes when a node is deleted.
//It calls the rotate left or rotate right according to the condition satisfied.
	void deleteFixup(RBT t1){
			while(t1!=rt && t1.colour == B){
					if(t1 == t1.uppernode.l){
							RBT w = t1.uppernode.r;
							if(w.colour == R){
									w.colour = B;
									t1.uppernode.colour = R;
									rL(t1.uppernode);
									w = t1.uppernode.r;
							}
							if(w.l.colour == B && w.r.colour == B){
									w.colour = R;
									t1 = t1.uppernode;
									continue;
							}
							else if(w.r.colour == B){
									w.l.colour = B;
									w.colour = R;
									rR(w);
									w = t1.uppernode.r;
							}
							if(w.r.colour == R){
									w.colour = t1.uppernode.colour;
									t1.uppernode.colour = B;
									w.r.colour = B;
									rL(t1.uppernode);
									t1 = rt;
							}
					}else{
							RBT w = t1.uppernode.l;
							if(w.colour == R){
									w.colour = B;
									t1.uppernode.colour = R;
									rR(t1.uppernode);
									w = t1.uppernode.l;
							}
							if(w.r.colour == B && w.l.colour == B){
									w.colour = R;
									t1 = t1.uppernode;
									continue;
							}
							else if(w.l.colour == B){
									w.r.colour = B;
									w.colour = R;
									rL(w);
									w = t1.uppernode.l;
							}
							if(w.l.colour == R){
									w.colour = t1.uppernode.colour;
									t1.uppernode.colour = B;
									w.l.colour = B;
									rR(t1.uppernode);
									t1 = rt;
							}
					}
			}
			t1.colour = B;
	}

//Finds the left most child i.e. the smallest element in the tree.
	RBT treeMinimum(RBT subTreeRoot){
			while(subTreeRoot.l!=empty){
					subTreeRoot = subTreeRoot.l;
			}
			return subTreeRoot;
	}

//When a print command comes to print a particular building it finds it in the BST and returns its values.
	String printspecific(int bno){
		RBT cur=rt;
		String str="";
		while(cur!=empty){
			if(cur.elem.bNo == bno){
				str = "("+cur.elem.bNo+","+ cur.elem.etime+","+ cur.elem.ttime+")";
				return str;
			}
			else if(bno>cur.elem.bNo)
				cur=cur.r;
			else
				cur=cur.l;
		}
		return str;
	}

//It creates a new node and calls the insertrb function.
	void insertrbt(Building b)
	{
		RBT current = new RBT(b);
		insertrb(current);
	}

//It deletes a node and calls the deleterb function.
	void deleterbt(Building b)
	{
		RBT current = new RBT(b);
		if (!deleterb(current)) {
			System.out.print(b.bNo+": does not exist!");
		}
	}


	//MinHeap implementation
  Building top() //Top of the Minheap
	{
		return Heap[1];
	}

	int lc(int index) //Returns left child of the current node
	{
		return (2*index);
	}

	int rc(int index) //Returns right child of the current node
	{
		return (2*index)+1;
	}

	boolean isLeaf(int index) //Checks if the current node is a leaf
	{
		if (index > (size / 2) && index <= size) {
			return true;
		}
		return false;
	}

	void nodeswap(int findex, int sindex) 	// Swaps two nodes of the heap
	{
		Building tmp;
		tmp = Heap[findex];
		Heap[findex] = Heap[sindex];
		Heap[sindex] = tmp;
	}

	// Brings the least executed building to the top
	//if the executed time is same then sorts according to building no
	void Heapify(int curr)
	{
		if(!isLeaf(curr)){                                    //Cheks if current node is leaf node
			if (Heap[lc(curr)] != null && Heap[rc(curr)] != null)  //Checks if both child exists
			{
				//Checks if either of the child's executed time is less than parent's executed time
				if (Heap[curr].etime > Heap[lc(curr)].etime || Heap[curr].etime > Heap[rc(curr)].etime)
				{
					//Checks if executed time of left child is less than executed time of right child
					if (Heap[lc(curr)].etime < Heap[rc(curr)].etime)
					{
						nodeswap(curr, lc(curr));
						Heapify(lc(curr));
					}
					//Checks if executed time of right child is less than executed time of left child
					else if (Heap[lc(curr)].etime > Heap[rc(curr)].etime)
					{
						nodeswap(curr, rc(curr));
						Heapify(rc(curr));
					}
					//Checks if executed time of right child is equal to execution time of right child
					else if (Heap[lc(curr)].etime == Heap[rc(curr)].etime)
					{
						if(Heap[lc(curr)].bNo < Heap[rc(curr)].bNo)
						{
							nodeswap(curr, lc(curr));
							Heapify(lc(curr));
						}
						else
						{
							nodeswap(curr, rc(curr));
							Heapify(rc(curr));
						}
					}
				}
				//Checks if parents and both the children have same execution time
				else if(Heap[curr].etime == Heap[lc(curr)].etime && Heap[curr].etime == Heap[rc(curr)].etime)
				{
					if (Heap[rc(curr)].bNo < Heap[curr].bNo && Heap[rc(curr)].bNo < Heap[lc(curr)].bNo)
					{
						nodeswap(curr, rc(curr));
						Heapify(rc(curr));
					}
					else if (Heap[lc(curr)].bNo < Heap[curr].bNo && Heap[rc(curr)].bNo > Heap[lc(curr)].bNo)
					{
						nodeswap(curr, lc(curr));
						Heapify(lc(curr));
					}
				}
				//Checks if executed time of left child is equal to executed time of parent
				else if(Heap[curr].etime == Heap[lc(curr)].etime)
				{
					if (Heap[lc(curr)].bNo < Heap[curr].bNo)
					{
						nodeswap(curr, lc(curr));
						Heapify(lc(curr));
					}
				}
				//Checks if executed time of right child is equal to executed time of parent
				else if(Heap[curr].etime == Heap[rc(curr)].etime)
				{
					if (Heap[rc(curr)].bNo < Heap[curr].bNo)
					{
						nodeswap(curr, rc(curr));
						Heapify(rc(curr));
					}
				}
			}
			else if(Heap[lc(curr)] != null && Heap[rc(curr)] == null)
			{
				if (Heap[curr].etime > Heap[lc(curr)].etime)
				{
						nodeswap(curr, lc(curr));
						Heapify(lc(curr));
				}
				else if(Heap[curr].etime == Heap[lc(curr)].etime)
				{
					if (Heap[curr].bNo > Heap[lc(curr)].bNo)
					{
							nodeswap(curr, lc(curr));
							Heapify(lc(curr));
					}
				}
			}
			else if(Heap[rc(curr)] != null && Heap[lc(curr)] == null)
			{
				if (Heap[curr].etime > Heap[rc(curr)].etime)
				{
						nodeswap(curr, rc(curr));
						Heapify(rc(curr));
				}
				else if(Heap[curr].etime == Heap[rc(curr)].etime)
				{
					if (Heap[curr].bNo > Heap[rc(curr)].bNo)
					{
							nodeswap(curr, rc(curr));
							Heapify(rc(curr));
					}
				}
			}
		}
}

	// Function to insert a node into the heap
	void insertminheap(Building element)
	{
		if (size >= maximum) {
			return;
		}
		Heap[++size] = element;
		int current = size;
	}

	// Function to build the min heap using
	// the Heapify
	void minHeap()
	{
		for (int i = (size / 2); i >= 1; i--) {
			Heapify(i);
		}
	}

	// Function to remove and return the minimum
	// element from the heap
	Building remove()
	{
		Building popped = Heap[FRONT];
		if(size>=1)
		{
			Heap[FRONT] = Heap[size--];
			Heap[size+1] = null;
			Heapify(FRONT);
		}
		return popped;
	}

	//Main class
	public static void main(String[] args) throws IOException
	{
		risingCity risingCity = new risingCity(2001);
		BufferedWriter writer = new BufferedWriter(new FileWriter("output_file.txt")); //Creates a file writer to write the output
		File file = new File(args[0]);                                                 //Reads the file from command line
  	BufferedReader br = new BufferedReader(new FileReader(file));                  //Starts a BufferedReader
  	String st=br.readLine();                                                       //Reads the first line
		String contents[] = st.split("\\W");																					 //Splits the input according to word and non-word characters in a string array
		Building b;
		ArrayList<Building> waitList= new ArrayList<Building>();											//Initialize ArrayList of Building
		b = new Building(Integer.parseInt(contents[3]),0,Integer.parseInt(contents[4]));
    risingCity.insertminheap(b);																									//Put the first elements in both the structures
		risingCity.insertrbt(b);
    int time = 1;																																	//global counter
    st = br.readLine();																														//reads next line
  	while (st != null || risingCity.size > 0)
		{
		if(risingCity.size == 0)
		{
			if(waitList.size() > 0)
			{
				while(waitList.size() > 0)
				{
					Building g = waitList.remove(0);
					risingCity.insertminheap(g);																						//Adds all the elements pending when heap is empty to the heap
				}
			}
			risingCity.minHeap();																												//Min Heapifies the min Heap
		}
    if(risingCity.size > 0 && risingCity.top().etime == risingCity.top().ttime){  //When the root of the minHeap has finished execution it removes the element from both the structures and prints it
        if(risingCity.size > 0)
				{
					b = risingCity.remove();
					risingCity.deleterbt(b);
					String aa = "("+b.bNo+","+(time-1)+")\n";
					writer.write(aa);
				}
    }
    if(risingCity.size > 0 && risingCity.top().etime % 5 == 0 && risingCity.top().etime != 0){ //When the execution time of the top is a multiple of 5 and not 0 then it inserts all the remaining elements in the arraylist in the minHeap
				if(waitList.size() > 0)
				{
					while(waitList.size() > 0)
					{
						Building g = waitList.remove(0);
						risingCity.insertminheap(g);
					}
				}
				risingCity.minHeap();
		}
		if(risingCity.size > 0)																															//if minHeap is not empty then increase the executed time of top of the heap by 1
				risingCity.Heap[1].etime++;
		if(st != null)																																			//if files has not finished executing
		{
	      contents = st.split("\\W");
	      if(Integer.parseInt(contents[0]) == time)
				{
	        st = br.readLine();
	        if(contents[2].equals("PrintBuilding") || contents[2].equals("Print"))				//if it is a print command
					{
						if(contents.length == 4 && risingCity.size>0)																// if it is a print range command
						{
							String aa = risingCity.printspecific(Integer.parseInt(contents[3]))+"\n";
							writer.write(aa);
						}
						else if(risingCity.size >0)																									//if the command is to print a specific building
						{
							String aa = risingCity.printRange(Integer.parseInt(contents[3]),Integer.parseInt(contents[4]));
					    aa = aa.substring(0,aa.length()-1);
							aa = aa + "\n";
							writer.write(aa);
						}
						else																																				//if there are no elements remaining in the structure
						{
							writer.write("(0,0,0)\n");
						}
	        }
					else																																					//if it is an insert command
					{
	            	b = new Building(Integer.parseInt(contents[3]),0,Integer.parseInt(contents[4]));
								waitList.add(b);																												//adds it to array list
								risingCity.insertrbt(b);																								//adds it to the RBT
					}
				}
      }
			time++;																																						//increment global counter by 1
		}
		writer.close();																																			//finished execution, closes the filewriter
	}
}
