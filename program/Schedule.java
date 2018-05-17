import java.util.LinkedList;

public abstract class Schedule {

	/* Constants */
	final public static int FIFO = 0;
	final public static int PQ = 1;
	final public static int FQ = 2;

	/* Attributes */
	public double packets_dropped_size;
	public double packets_switched_size;
	public int packets_dropped_cnt;
	public int packets_switched_cnt;
	public int total_wait_time;

	/* Constructor */
	public Schedule() {
		// for throughput
		packets_dropped_size = 0;
		packets_switched_size = 0;

		// for total packets
		packets_dropped_cnt = 0;
		packets_switched_cnt = 0;

		// for average wait time
		total_wait_time = 0;
	}

	/* Methods */
	abstract void addPacket(Packet p);
	abstract void dropPacket(Packet p);
	abstract void switchPacket(Packet p);
	abstract void process(int bandwidth, int current_time, LinkedList<Packet> packets);
}