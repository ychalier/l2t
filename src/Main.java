import java.io.File;

import controller.Controller;
import tools.Config;
import tools.Logger;

/*
 * Main class. Reads arguments and config, before
 * starting the controller.
 * 
 * arguments
 * 
 * -c  --config  [PATH]   Loads a config file
 * 
 * @author Yohan Chalier
 *
 */
public class Main {

	/**
	 * @param args
	 *        -h  --help		    Display help message
	 * 		  -l  --log             Activate the logger (into file .log)
	 *        -c  --config   [path] Loads a config file
	 *        -p  --priority [int]  Log priority level (0-4)
	 */
	public static void main(String[] args) {
		
		// Reading arguments
		boolean log = false;
		String configPath = null;
		int priority = 0;
		int i = 0;
		while (i < args.length) {
			if (args[i].equals("-l") || args[i].equals("--log"))
				log = true;
			else if ((args[i].equals("-c") || args[i].equals("--config")) && i < args.length-1) {
				configPath = args[i+1];
				i++;
			}
			else if ((args[i].equals("-p") || args[i].equals("--priority")) && i < args.length-1) {
				priority = Integer.parseInt(args[i+1]);
				i++;
			}
			else if ((args[i].equals("-h") || args[i].equals("--help"))) {
				System.out.println("usage: java -jar [jarfile] [options]\n"
						+ "Options and arguments:\n"
						+ "-c --config   [filename] : load a config file\n"
						+ "-p --priority [int]      : log prioriry level (0-4)\n"
						+ "-l --log                 : activate the logger (logfile set in config)\n"
						+ "-h --help                : show this message");
				return ;
			}
			else {
				System.out.println("invalid parameter: " + args[i] + "\nTry with -h for help.");
				return;
			}
			i++;
		}
		
		// Load configuration
		if (configPath != null) {
			Logger.wrI("MAIN", "Loading config file: " + configPath);
			Config.load(new File(configPath));
		} else {
			Logger.wrI("MAIN", "No config file specified. Using default values.");
		}
		
		// Initialize logger
		new Logger(log, priority);
		
		Controller controller = new Controller();
		controller.init();
		controller.start();
		
				
	}
}
