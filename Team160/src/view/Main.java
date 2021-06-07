
	package view;

	import java.io.IOException;

	
	import exceptions.FullHandException;

	public class Main implements Runnable{
		
		View view = new View();

		public static void main(String[] args) throws FullHandException, CloneNotSupportedException, IOException {
			
			new Thread (new Main()).start();
			
		}

		@Override
		public void run() {

				
			}
			
		}



