import java.io.*;
import java.util.LinkedList;

public class FirstInFirstOut implements Schedule {

	/* Attributes */
	public int flows_dropped_cnt;
	public int flows_switched_cnt;
	public int total_wait_time;
	public double flows_dropped_size;
	public double flows_switched_size;
	public NetworkBuffer buffer;

	/* Constructor */
	public FirstInFirstOut() {
		// for throughput
		flows_dropped_size = 0;
		flows_switched_size = 0;

		// for total flows
		flows_dropped_cnt = 0;
		flows_switched_cnt = 0;

		// for average wait time
		total_wait_time = 0;

		// for buffer
		buffer = new NetworkBuffer();
	}

	/* Methods */
	public boolean queueEmpty() {
		if (buffer.isEmpty()) return true;
		return false;
	}

	public double throughput(int duration) {
		// System.out.println(flows_switched_size);
		// System.out.println(duration);
		return (flows_switched_size / (double)duration);
	}

	public double process(int bandwidth, int current_time, int timeout, LinkedList<Flow> flows) {
		double temp_duration = 0;
		if (flows != null) {
			for (Flow f : flows) {
				addFlow(f);
			}
		}

		// check if there are timed out flows waiting in queue
		while (!buffer.isEmpty()) {
			Flow f = buffer.peekFirst();
			if (f.waitTime(current_time) == timeout) {
				dropFlow(f);
				total_wait_time += f.waitTime(current_time);
			} else break;
		}

		// switch flows that fit the bandwidth
		while (!buffer.isEmpty()) {
			Flow f = buffer.peekFirst();
			temp_duration = (double)((f.size + f.no_of_packets)/bandwidth);
			if (temp_duration < (double)timeout) {
				switchFlow(f);
				total_wait_time += f.waitTime(current_time);				
			} else{
				dropFlow(f);
				total_wait_time += f.waitTime(current_time);
			}
		}

		return temp_duration;
	}

	public void addFlow(Flow f) {
		buffer.add(f);
	}

	public void dropFlow(Flow f) {
		buffer.remove();
		flows_dropped_size += f.size;
		flows_dropped_cnt++;
	}

	public void info(int bandwidth, int duration, String dateAsText) {
		System.out.println("\n\n------[ FIFO SIMULATION SUMMARY ]------");
		System.out.println("TIMESTAMP = " + dateAsText);
		System.out.println("BANDWIDTH = " + bandwidth + " bps\n");
		System.out.println("flows_dropped_size = " + flows_dropped_size + " b");
		System.out.println("flows_dropped_cnt = " + flows_dropped_cnt + " flows\n");
		System.out.println("flows_switched_size = " + flows_switched_size + " b");
		System.out.println("flows_switched_cnt = " + flows_switched_cnt + " flows\n");
		System.out.println("total_wait_time = " + total_wait_time + " seconds");
		System.out.println("throughput = " + throughput(duration) + " bps");
	}

	public void saveResults(int bandwidth, int duration, String dateAsText) throws IOException {
		FileWriter fw = new FileWriter("results/" + dateAsText + "_" + bandwidth + ".txt", true);
		fw.write("\n\n------[ FIFO SIMULATION SUMMARY ]------");
		fw.write("\nTIMESTAMP = " + dateAsText);
		fw.write("\nBANDWIDTH = " + bandwidth + " bps\n");
		fw.write("\n\tflows_dropped_size = " + flows_dropped_size + " b");
		fw.write("\n\tflows_dropped_cnt = " + flows_dropped_cnt + " flows\n");
		fw.write("\n\tflows_switched_size = " + flows_switched_size + " b");
		fw.write("\n\tflows_switched_cnt = " + flows_switched_cnt + " flows\n");
		fw.write("\n\ttotal_wait_time = " + total_wait_time + " seconds");
		fw.write("\n\tthroughput = " + throughput(duration) + " bps");
		fw.close();
	}

	public void switchFlow(Flow f) {
		buffer.remove();
		flows_switched_size += f.size;
		flows_switched_cnt++;
	}
}