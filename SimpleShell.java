import java.io.*;
import java.util.*;

public class SimpleShell {

	public static void main(String[] args) throws java.io.IOException {
		String commandLine;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		ProcessBuilder pb = new ProcessBuilder();
		File file = new File(System.getProperty("user.dir"));
		pb.directory(file);
		ArrayList<String> historyList = new ArrayList<String>();
		int count = 0;
		int target;

		// we break out with <control><C>
		try {
			while (true) {
				// read what the user entered
				System.out.print("jsh" + pb.directory() + ">");
				commandLine = console.readLine();

				// if the user entered a return, just loop again
				if (commandLine.equals(""))
					continue;

				String[] stringArray = commandLine.split(" ");

				ArrayList<String> list = new ArrayList<String>();
				Collections.addAll(list, stringArray);

				if (commandLine.equals("exit") | commandLine.equals("quit")) {
					System.out.println("Goodbye");
					System.exit(0);
				}

				if (commandLine.equals("history")) {

					for (int j = 0; j < count; j++) {
						System.out.println(j + "  " + historyList.get(j));
					}
					continue;
				}

				if (commandLine.equals("!!")) {
					if (count == 0) {
						System.out.println("There is not previous command");
						continue;
					}
					list.clear();
					list.add(historyList.get(count - 1));
				} else if (commandLine.contains("!")) {
					list.clear();
					list.add(historyList.get((int) (commandLine.charAt(1)) - 48));
				}

				if (list.contains("cd")) { // implementation of cd

					if (list.get(list.size() - 1).equals("cd")) { // if
																	// commandline
																	// equals cd
						File file_2 = new File(System.getProperty("user.home")); // change
																					// directory
																					// to
																					// user.home
						System.out.println("Home directory : " + file_2); // i.e)
																			// /Users/bag-ilgwon
						pb.directory(file_2);
						historyList.add(commandLine);
						count++;
						continue;
					} else { // implementation of relative address
						if (commandLine.equals("cd ..")) {
							File newFile3 = pb.directory().getParentFile();
							pb.directory(newFile3);
							continue;
						}
						if (!list.get(list.size() - 1).contains("/")) {
							String newAdress = list.get(list.size() - 1);
							String updatedAdress = pb.directory() + "/" + newAdress;
							File newFile = new File(updatedAdress);
							if (!newFile.exists()) {
								System.out.println("Error !! :  you typed wrong directory");
								continue;
							}
							file = newFile;
							pb.directory(file);
							historyList.add(commandLine);
							count++;
							continue;
						}
					}

					String newAdress = list.get(list.size() - 1); // implementation
																	// of
																	// absolute
																	// address
					String updatedAdress = newAdress;
					File newFile = new File(updatedAdress);
					if (!newFile.exists()) {
						System.out.println("Error !! :  you typed wrong directory");
						continue;
					}
					pb.directory(newFile);
					historyList.add(commandLine);
					count++;
					continue;
				}
				historyList.add(commandLine);
				count++;

				pb.command(list);
				Process process = pb.start();

				// obtain the input stream
				InputStream is = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);

				// read the output of the process
				String line;
				while ((line = br.readLine()) != null)
					System.out.println(line);

				br.close();

				// **The steps are :
				// (1) parse the input to obtain the command and any parameters
				// (2) create a ProcessBuilder object
				// (3) start the process
				// (4) obtain the output stream
				// (5) output the contents returned by the command */
			}

		} catch (Exception e) {
			System.out.println("You typed wrong command");
		}
	}
}
