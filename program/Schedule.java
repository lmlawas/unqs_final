import java.util.LinkedList;

interface Schedule {

	/* Constants */
	final public static int FIFO = 0;
	final public static int PQ = 1;
	final public static int FQ = 2;

	/* Methods */
	abstract void addPacket(Packet p);
	abstract void dropPacket(Packet p);
	abstract void info();
	abstract void switchPacket(Packet p);	
	abstract void process(int bandwidth, int current_time, int timeout, LinkedList<Packet> packets);	
}