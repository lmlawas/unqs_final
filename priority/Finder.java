import java.io.File;
import java.io.FileWriter;
import java.util.regex.*;
import java.util.Scanner;

public class Finder{

	public static void main(String[] args){
		findWord(args[0], args[1], new File("service-names-port-numbers.csv"));
	}

	public static void findWord(String word, String priority, File file) {
		// int lineNo = 0;		
		try{
			Scanner scanner = new Scanner(file);
			FileWriter fw = new FileWriter(priority+".txt", true);
			while (scanner.hasNextLine()) {
				// lineNo++;
			    String nextToken = scanner.nextLine();
			    Pattern p = Pattern.compile(".*"+word+".*", Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(nextToken);
			    if (m.matches()){
			    	// System.out.println("line "+ lineNo + ": " + nextToken);
			    	String[] portProtocols = nextToken.split(",");
			    	for(int i=0; i<4; i++){
			    		if(i<=portProtocols.length){
			    			fw.write(portProtocols[i]+",");
			    		}
			    	}
			    	fw.write("\n");
			    }			    	
			    	
			}
			fw.close();
		}catch(Exception e){}		
	}

}
