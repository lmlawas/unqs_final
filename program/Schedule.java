import java.io.*;
import java.util.LinkedList;

interface Schedule {

	/* Constants */
	final public static int FIFO = 0;
	final public static int PQ = 1;
	final public static int WFQ = 2;

	/* Methods */
	abstract double throughput(int current_time);
	abstract void addPacket(Packet p);
	abstract void dropPacket(Packet p);
	abstract void info(int bandwidth, int duration, String dateAsText);	
	abstract void saveResults(int bandwidth, int duration, String dateAsText) throws IOException;
	abstract void switchPacket(Packet p);	
	abstract void process(int bandwidth, int current_time, int timeout, LinkedList<Packet> packets);
	abstract boolean queueEmpty();
}