package Http;

import java.util.ArrayList;
import java.util.List;

public class http {
	public static void main( String[] args) throws InterruptedException{
		List<MyThread> threads = new ArrayList<MyThread>();
		for(int i=0;i<300;i++) {
			MyThread th = new MyThread();
			th.start();
			threads.add(th);
		}
		for(MyThread th: threads)
			th.join();
		System.out.println("success:" + global.success + ";" + "failed:" + global.failed);
	}
}
