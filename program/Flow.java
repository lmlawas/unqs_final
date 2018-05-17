import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Flow {

	/* Attributes */
	public int first_switched,
	       no_of_packets,
	       priority,
	       size;

	/* Constructors */
	public Flow() {
	}

	public Flow(int first_switched, int no_of_packets, int size) {
		this.first_switched = first_switched;
		this.no_of_packets = no_of_packets;
		this.priority = 0;
		this.size = size;
	}

	public Flow(int first_switched, int no_of_packets, int protocol, int size) {
		this.first_switched = first_switched;
		this.no_of_packets = no_of_packets;
		this.priority = getPriority(protocol);
		this.size = size;
	}

	/* Methods */
	public LinkedList<Packet> convertToPackets(int schedule_type) {
		LinkedList<Packet> packets = new LinkedList<Packet>();
		int i, quotient = size / no_of_packets, remainder = size % no_of_packets;

		for (i = 0; i < no_of_packets; i++) {
			if (size % no_of_packets > 0 && i == no_of_packets - 1) {
				packets.add(new Packet(first_switched, priority, size + remainder));
			} else {
				packets.add(new Packet(first_switched, priority, quotient));
			}
			size = size - quotient;
		}

		return packets;
	}

	public boolean matchCommas(String line) {
		boolean match = false;

		match = Pattern.matches("(.*),([0-9]+)(,.*)*", line);
		return match;
	}

	public int getPriority(int protocol) {
		try {
			int i;
			for (i = 1; i < 3; i++) {
				String file_location = "../priority/" + Integer.toString(i) + ".txt";
				FileReader fp = new FileReader(file_location);
				BufferedReader br = new BufferedReader(fp);
				String line = null;

				while ((line = br.readLine()) != null) {
					if (matchCommas(line)) {
						String[] tokens = line.split(",");
						int port = Integer.parseInt(tokens[1]);
						if (protocol == port) {
							return i-1;
						}
					}
				}
				br.close();
				fp.close();
			}
			return i-1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}