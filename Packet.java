public class Packet {

	/* Attributes */
	public int first_switched;
	public int priority;
	public double size;

	/* Constructors */
	public Packet() {
	}

	public Packet(int first_switched, int priority, double size) {
		this.first_switched = first_switched;
		this.priority = priority;
		this.size = size;
	}

	/* Methods */
	public double virtualFinishTime(int current_time) {
		int max_time = current_time;

		if (this.first_switched > max_time) {
			max_time = this.first_switched;
		}

		return max_time + this.size;
	}

	public int waitTime(int current_time) {
		return (current_time - first_switched);
	}

	public void info(){
		System.out.println("------------\npacket info:\n------------");
		System.out.println("\tfirst_switched = " + first_switched);
		System.out.println("\tpriority = " + priority);
		System.out.println("\tsize = " + size + "\n------------\n");
	}
}