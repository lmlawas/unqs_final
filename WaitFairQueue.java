import java.io.*;
import java.util.LinkedList;

public class WaitFairQueue implements Schedule {

	/* Attributes */	
	public int packets_dropped_cnt;
	public int packets_switched_cnt;
	public int total_wait_time;
	public double packets_dropped_size;
	public double packets_switched_size;
	public NetworkBuffer priority_buffer;
	public NetworkBuffer wait_buffer;

	/* Constructor */
	public WaitFairQueue() {
		// for throughput
		packets_dropped_size = 0;
		packets_switched_size = 0;

		// for total packets
		packets_dropped_cnt = 0;
		packets_switched_cnt = 0;

		// for average wait time
		total_wait_time = 0;

		// for buffers
		priority_buffer = new NetworkBuffer();
		wait_buffer = new NetworkBuffer();
	}

	/* Methods */
	public double throughput(int duration) {
		return (packets_switched_size / (double)duration);
	}

	public void addPacket(Packet p) {
		if(p.priority == 0) priority_buffer.add(p);
		else wait_buffer.add(p);
	}

	public void dropPacket(Packet p) {
		if(p.priority == 0) priority_buffer.remove();
		else wait_buffer.remove();
		packets_dropped_size += p.size;
		packets_dropped_cnt++;
	}

	public void info(int bandwidth, int duration, String dateAsText) {
		System.out.println("\n\n------[ WFQ SIMULATION SUMMARY ]------");
		System.out.println("TIMESTAMP = " + dateAsText);
		System.out.println("BANDWIDTH = " + bandwidth + " bps\n");
		System.out.println("packets_dropped_size = " + packets_dropped_size + " b");
		System.out.println("packets_dropped_cnt = " + packets_dropped_cnt + " packets\n");
		System.out.println("packets_switched_size = " + packets_switched_size + " b");
		System.out.println("packets_switched_cnt = " + packets_switched_cnt + " packets\n");
		System.out.println("total_wait_time = " + total_wait_time + " seconds");
		System.out.println("throughput = " + throughput(duration) + " bps");
	}

	public void saveResults(int bandwidth, int duration, String dateAsText) throws IOException {
		FileWriter fw = new FileWriter("results/"+dateAsText+"_"+bandwidth+".txt", true);
		fw.write("\n\n------[ WFQ SIMULATION SUMMARY ]------");
		fw.write("\nTIMESTAMP = " + dateAsText);
		fw.write("\nBANDWIDTH = " + bandwidth + " bps\n");
		fw.write("\n\tpackets_dropped_size = " + packets_dropped_size + " b");
		fw.write("\n\tpackets_dropped_cnt = " + packets_dropped_cnt + " packets\n");
		fw.write("\n\tpackets_switched_size = " + packets_switched_size + " b");
		fw.write("\n\tpackets_switched_cnt = " + packets_switched_cnt + " packets\n");
		fw.write("\n\ttotal_wait_time = " + total_wait_time + " seconds");
		fw.write("\n\tthroughput = " + throughput(duration) + " bps");
		fw.close();
	}

	public void switchPacket(Packet p) {
		if(p.priority == 0) priority_buffer.remove();
		else wait_buffer.remove();
		packets_switched_size += p.size;
		packets_switched_cnt++;
	}

	public void process(int bandwidth, int current_time, int timeout, LinkedList<Packet> packets) {
		double temp_buffer_size = 0;

		if (packets != null) {
			for (Packet p : packets) {
				addPacket(p);
			}
		}

		// wait factor
		while(!wait_buffer.isEmpty()){
			Packet p = wait_buffer.peekFirst();
			if(p.waitTime(current_time) != timeout && p.waitTime(current_time) >= 55){
				switchPacket(p);
				total_wait_time += p.waitTime(current_time);
			} else break;
		}

		while (!priority_buffer.isEmpty()) {
			Packet p = priority_buffer.peekFirst();
			if (p.waitTime(current_time) == timeout) {
				dropPacket(p);
				total_wait_time += p.waitTime(current_time);
			} else if (p.size + temp_buffer_size <= bandwidth) {
				switchPacket(p);
				total_wait_time += p.waitTime(current_time);
			} else break;
		}

		while (!wait_buffer.isEmpty()) {
			Packet p = wait_buffer.peekFirst();
			if (p.waitTime(current_time) == timeout) {
				dropPacket(p);
				total_wait_time += p.waitTime(current_time);
			} else if (p.size + temp_buffer_size <= bandwidth) {
				switchPacket(p);
				total_wait_time += p.waitTime(current_time);
			} else break;
		}

		// if(!priority_buffer.isEmpty()){
		// 	for(Packet p: priority_buffer){
		// 		total_wait_time += p.waitTime(current_time);
		// 	}
		// }

		// if(!wait_buffer.isEmpty()) {
		// 	for(Packet p: wait_buffer){
		// 		total_wait_time += p.waitTime(current_time);
		// 	}
		// }

	}

	public boolean queueEmpty(){		
		return (priority_buffer.isEmpty() && wait_buffer.isEmpty());
	}
}