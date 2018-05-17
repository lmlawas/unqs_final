import java.util.LinkedList;

public class FirstInFirstOut implements Schedule {

	/* Attributes */
	public int packets_dropped_size;
	public int packets_switched_size;
	public int packets_dropped_cnt;
	public int packets_switched_cnt;
	public int total_wait_time;
	public NetworkBuffer buffer;

	/* Constructor */
	public FirstInFirstOut() {
		// for throughput
		packets_dropped_size = 0;
		packets_switched_size = 0;

		// for total packets
		packets_dropped_cnt = 0;
		packets_switched_cnt = 0;

		// for average wait time
		total_wait_time = 0;

		// for buffer
		buffer = new NetworkBuffer();
	}

	/* Methods */
	public void addPacket(Packet p) {
		buffer.add(p);
	}

	public void dropPacket(Packet p) {
		buffer.remove();
		packets_dropped_size += p.size;
		packets_dropped_cnt++;
	}

	public void info() {
		System.out.println("\n\n--------\nResults:\n--------");
		System.out.println("packets_dropped_size = " + packets_dropped_size + " B");
		System.out.println("packets_switched_size = " + packets_switched_size + " B");
		System.out.println("packets_dropped_cnt = " + packets_dropped_cnt + " packets");
		System.out.println("packets_switched_cnt = " + packets_switched_cnt + " packets");
		System.out.println("total_wait_time = " + total_wait_time + " seconds");
	}

	public void switchPacket(Packet p) {
		buffer.remove();
		packets_switched_size += p.size;
		packets_switched_cnt++;
	}

	public void process(int bandwidth, int current_time, int timeout, LinkedList<Packet> packets) {
		int temp_buffer_size = 0;
		for (Packet p : packets) {
			// p.info();
			addPacket(p);			
		}
		while (!buffer.isEmpty()) {
			Packet p = buffer.peekFirst();
			if (p.waitTime(current_time) == timeout) {
				dropPacket(p);
				total_wait_time += p.waitTime(current_time);
			} else if (p.size + temp_buffer_size <= bandwidth) {
				switchPacket(p);
				total_wait_time += p.waitTime(current_time);
			} else break;
		}
	}
}